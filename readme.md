##############################双人棋牌游戏服务端开发##############################
1.0版本  Socket+多线程
2.0版本  Socket+线程池+解耦合
3.0版本  Netty
4.0版本  当前版本

(一)技术栈说明
    Socket   多线程  线程池  动态线程池
    SpringBoot  Netty  Redis  MYSQL  Mybatis  RabbitMQ  

(二)yml文件配置说明
    服务端客户端通信端口 8888
    Redis和RabbitMQ 使用192.168.23.131 centos7虚拟机
    数据库为本地 cardgame

(三)五大模块 
    1.登陆注册  
    2.进入游戏
    3.查询游戏记录
    4.查询排行榜
    5.抢购欢乐豆  

(四)协议的制定
    使用ProtoBuf来作为协议
    id1   表明  登录注册等哪个功能模块的编号
    id2   表明  每个模块的信息
    id3   备用
    context   发送的消息String
    注册         1   -----handlerInfo   1  1 注册成功   1  2注册失败
    登录         2   -----handlerInfo   2  1 登录成功   2  2登陆失败
    查询游戏信息   3   -----handlerInfo
    查询排行榜    4   -----handlerInfo
    加入游戏     5    -----handlerPlay
    开始游戏     6    -----handlerPlay    服务端发给客户端  6.0.1 开始游戏(先出牌)  6.0.2开始游戏(后出牌)   6.1 context(用户出的牌)   6.2 context(用户的牌或者对手的牌出完了 本局比赛结束)
                                         客户端发给服务端  6.0 不要    6.1要
    退出游戏     7    -----handlerPlay
    抢购欢乐豆   8    -----handlerSecKill
    购买欢乐豆   9    -----handlerBuy    
    修改密码     10   -----handlerInfo
    刷新用户信息  11   -----handlerFlush   用于用户购买欢乐豆成功后刷新用户的balance happybean
    同步客户端和服务端的时间      0  ------handlerSyncTime












