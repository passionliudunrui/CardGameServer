package com.cardgameserver.netty;

import com.cardgameserver.dao.AccountDao;
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
import com.cardgameserver.zset.SkipList;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
public class MyServerHandlerInfo extends SimpleChannelInboundHandler<MessagePOJO.Message> {



    private static UserService userService;
    private static NoteService noteService;
    private static AccountService accountService;
    private static SkipList skipList;

    static {
        userService= SpringUtil.getBean(UserService.class);
        noteService=SpringUtil.getBean(NoteService.class);
        accountService=SpringUtil.getBean(AccountService.class);
        skipList=SpringUtil.getBean(SkipList.class);
    }

    private ChannelHandlerContext ctx;

    private UserVo userVo=new UserVo();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessagePOJO.Message message) throws Exception {
        this.ctx=ctx;
        int id1=message.getId1();
        int id2=message.getId2();
        String context=message.getContext();
        if(id1==1||id1==2||id1==3||id1==4){
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







            }
        }
        else{

            ctx.fireChannelRead(message);

        }
    }



    /**
     * 处理注册功能
     * @param context
     */
    private void apply(String context){
        System.out.println(context);
        String[]data=context.split(",");
        Long id=Long.valueOf(data[0]);
        String nickName=data[1];
        String passwd=data[2];
        System.out.println(id);
        System.out.println(nickName);
        System.out.println(passwd);
        String password = MD5Util.inputPassToFromPass(passwd);

        User user=new User(id,nickName,password);
        int ans = userService.insert(user);

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
     * 处理登录功能
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
            this.userVo.setUser(user);
//            this.userVo.setAccount(accoun);
            this.userVo.setAccount(accountService.findById(user.getId()));

            message1=Transfrom.transform(2,1,"登陆成功");
        }

        ctx.writeAndFlush(message1);
    }

    /**
     *
     */
    private void showNote(){
        String ans="";
        List<Note> notes = noteService.findById(userVo.getUser().getId());
        for(Note note:notes){
            ans+=note.toString()+";";
        }
        MessagePOJO.Message message1 = Transfrom.transform(3, ans);
        ctx.writeAndFlush(message1);
    }

    /**
     * 查看TopTen
     */
    private void showTopTen() {
        skipList.dumpTenDesc();
    }









}
