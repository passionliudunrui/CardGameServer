package com.cardgameserver.zset;


/**
 * 跳表中的节点
 */
public class Node {
    public  Double score;
    public Long id;

    public Node left;
    public Node right;
    public Node up;
    public Node down;

    public Node(Double score){
        this.score=score;
        left=up=right=down=null;
    }

    public Node(Double score,Long id){
        this.score=score;
        this.id=id;
        up=down=left=right=null;
    }


}
