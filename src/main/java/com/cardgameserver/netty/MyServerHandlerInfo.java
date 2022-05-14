package com.cardgameserver.netty;

import com.cardgameserver.entity.Account;
import com.cardgameserver.entity.Note;
import com.cardgameserver.entity.User;
import com.cardgameserver.proto.MessagePOJO;
import com.cardgameserver.service.AccountService;
import com.cardgameserver.service.NoteService;
import com.cardgameserver.service.UserService;
import com.cardgameserver.utils.MD5Util;
import com.cardgameserver.utils.SpringUtil;
import com.cardgameserver.utils.Transfrom;
import com.cardgameserver.vo.UserVo;
import com.cardgameserver.zset.Node;
import com.cardgameserver.zset.SkipList;
import com.github.pagehelper.PageHelper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * springboot集成netty的情况下
 * netty的Handler中 不能进行自动注册  不能@Resource和@Autowire
 * 通过写一个工具类从容器中直接获取对象
 */


@Slf4j
public class MyServerHandlerInfo extends SimpleChannelInboundHandler<MessagePOJO.Message> {
    private static UserService userService;
    private static NoteService noteService;
    private static AccountService accountService;
    private static SkipList skipList;
    public static ConcurrentHashMap<Long, Double> nowTopPlayers;
    private UserVo userVo;
    private ChannelHandlerContext ctx;


    static {
        userService= SpringUtil.getBean(UserService.class);
        noteService=SpringUtil.getBean(NoteService.class);
        accountService=SpringUtil.getBean(AccountService.class);
        skipList=MyServer.skipList;
        nowTopPlayers=MyServer.nowTopPlayers;
    }

    MyServerHandlerInfo(){
        userVo=new UserVo();
    }

    public void setUserVo(UserVo userVo){
        this.userVo=userVo;
        System.out.println(this.userVo);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.ctx=ctx;
        /**
         * 通道开启的时候也就意味着开始服务了 在这里将数据库中排名前20 插入到skiplist和concurrentMap中
         */
        List<Account>topPlayers=accountService.selectAll();
//        System.out.println("1111111111111");
//        for(Account a:topPlayers){
//            System.out.println(a.getId()+"  "+a.getHappybean());
//        }
//        System.out.println("1111111111111");
//        for(Account topPlayer:topPlayers){
//            nowTopPlayers.put(topPlayer.getId(),topPlayer.getHappybean());
//        }
        for(Account topPlayer:topPlayers){
            skipList.insert(topPlayer.getHappybean(),topPlayer.getId());
        }
        System.out.println("插入数据完成");
        skipList.dumpAllDesc();
        System.out.println("---------------------------------------");

        syncTime();

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessagePOJO.Message message) throws Exception {

        int id1=message.getId1();

        String context=message.getContext();
        if(id1==1||id1==2||id1==3||id1==4||id1==10||id1==0||id1==11){
            switch (id1){
                case 1:
                    log.info("客户端发来注册请求");
                    apply(context);
                    break;
                case 2:
                    log.info("客户端发来登录请求");
                    register(context);
                    break;
                case 3:
                    log.info("客户端查看历史记录");
                    showNote();
                    break;
                case 4:
                    log.info("客户端查看TopTen");
                    showTopTen();
                    break;
                case 10:
                    log.info("修改用户信息");
                    modifyInfo(context);
                    break;
                case 0:
                    log.info("用户发来同步时间的请求");
                    syncTime();
                    break;
                case 11:
                    log.info("用户刷新用户信息");
                    flushInfo();
                    break;
            }
        }
        else{
            MyServerHandlerPlay nextHandler = ctx.pipeline().get(MyServerHandlerPlay.class);
            nextHandler.setUserVo(this.userVo);
            ctx.fireChannelRead(message);
        }
    }




    /**
     * 处理注册功能
     * @param context
     */
    private void apply(String context){

        String[]data=context.split(",");
        Long id=Long.valueOf(data[0]);
        String nickName=data[1];
        String passwd=data[2];

        String password = MD5Util.inputPassToFromPass(passwd);

        User user=new User(id,nickName,password);
        int ans = userService.insert(user);

        int ans2 = accountService.insert(id);
        if(ans==1){
            log.info("成功");
            MessagePOJO.Message message1 = Transfrom.transform(1, 1, "注册成功");
            ctx.writeAndFlush(message1);
        }
        else{
            log.info("失败");
            MessagePOJO.Message message1 = Transfrom.transform(1, 0, "注册失败");
            ctx.writeAndFlush(message1);
        }
    }

    /**
     * 处理登录功能    redis的处理？
     * @param context
     */

    private void register(String context){
        String[]data=context.split(",");
        Long id=Long.valueOf(data[0]);
        String passwd=data[1];
        String password = MD5Util.inputPassToFromPass(passwd);
        User user = userService.check(id, password);

        MessagePOJO.Message message1=null;
        if(user==null){
             message1= Transfrom.transform(2, 0, "账号或者密码错误");
        }
        else{
            this.userVo.setId(user.getId());
            this.userVo.setNickName(user.getNickName());
            this.userVo.setPassword(user.getPassword());

            Account account = accountService.findById(user.getId());
            this.userVo.setBalance(account.getBalance());
            this.userVo.setHappybean(account.getHappybean());

            MyServer.players.put(this.userVo.getId(),ctx.channel());

            /**
             * 测试如何实现UserVo再不同handler的传递  实现userVo的传递
             */


            message1=Transfrom.transform(2,1,"登陆成功");
        }

        ctx.writeAndFlush(message1);
    }

    /**
     *查看历史游戏记录
     */
    private void showNote(){
        /**
         * 可以添加参数进行优化  用户查询那一页的数据
         * 默认是查询第一页的前10条数据
         */
        //添加了分页插件 来实现分页的功能
        PageHelper.startPage(0,10);
        String ans="";
        List<Note> notes = noteService.findById(userVo.getId());
        for(Note note:notes){
            ans+=note.toString()+";";
        }
        MessagePOJO.Message message1 = Transfrom.transform(3, ans);
        ctx.writeAndFlush(message1);
    }

    /**
     * 查看TopFive
     */
    private void showTopTen() {
        List<Map<Double, Long>> ans = skipList.dumpFiveDesc();
        String str="";
        for(Map<Double,Long>m:ans){
            str=str+m.toString()+",  ";

        }
//        for(Map.Entry<Double,Long>entry:ans.entrySet()){
//            str=str+entry.getKey().toString()+","+entry.getValue().toString()+";";
//        }
        MessagePOJO.Message message1 = Transfrom.transform(4, str);
        ctx.writeAndFlush(message1);
    }

    /**
     * 修改用户信息
     *
     * 删除redis中的缓存  更新mysql中的缓存
     *
     * @param context
     */
    private void modifyInfo(String context) {
        //context  中  nickName  password
        String[]data=context.split(",");

        String nickName=data[0];
        String passwd=data[1];
        String password = MD5Util.inputPassToFromPass(passwd);
        /*
        更新数据库和redis的数据
         */
        User user1=new User(userVo.getId(),nickName,password);
        int ans = userService.update(user1);

        /*
        更新当前项目缓存中的数据
         */
        userVo.setNickName(nickName);
        userVo.setPassword(password);

        MessagePOJO.Message message1 = Transfrom.transform(10, 1, "更新信息成功");
        ctx.writeAndFlush(message1);

    }

    /**
     * 处理同步客户端的同步时间
     * 获取每个周 周六晚上8点 和周六晚上8.30分的时间发送到客户端上面
     *
     */
    private void syncTime() {
        long time = new Date().getTime();
        String time2 = String.valueOf(time);

        Calendar cld=Calendar.getInstance(Locale.CHINA);
        cld.setFirstDayOfWeek(Calendar.MONDAY);

        cld.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
        cld.set(Calendar.HOUR_OF_DAY,20);
        cld.set(Calendar.MINUTE,0);
        cld.set(Calendar.SECOND,0);

        long time3=cld.getTime().getTime();

        cld.set(Calendar.MINUTE,30);
        long time4=cld.getTime().getTime();
        String time33=String.valueOf(time3);
        String time44=String.valueOf(time4);
        String text=time2+","+time33+","+time44;
        MessagePOJO.Message message1 = Transfrom.transform(0, text);
        ctx.writeAndFlush(message1);

    }

    /**
     * 当用户购买玩欢乐豆后 刷新用户的信息
     */
    private void flushInfo() {
    }

}
