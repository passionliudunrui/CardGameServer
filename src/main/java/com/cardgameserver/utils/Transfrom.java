package com.cardgameserver.utils;

import com.cardgameserver.proto.MessagePOJO;

/**
 * 函数重载
 */
public class Transfrom {


    public static MessagePOJO.Message transform(int id1,String context){

        MessagePOJO.Message message=null;

        message= MessagePOJO.Message.newBuilder().setId1(id1)
               .setContext(context).build();

        return message;

    }


    public static MessagePOJO.Message transform(int id1,int id2,String context){

        MessagePOJO.Message message=null;

        message= MessagePOJO.Message.newBuilder().setId1(id1)
                .setId2(id2).setContext(context).build();

        return message;

    }



    public static MessagePOJO.Message transform(int id1,int id2,int id3,String context){

        MessagePOJO.Message message=null;

        message= MessagePOJO.Message.newBuilder().setId1(id1)
                .setId2(id2).setId3(id3).setContext(context).build();

        return message;

    }


}
