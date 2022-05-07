package com.cardgameserver.vo;

import com.cardgameserver.entity.Account;
import com.cardgameserver.entity.User;
import lombok.*;

import java.util.ArrayList;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {
    private Long id;
    private String nickName;
    private String password;
    private Integer balance;
    private Integer happybean;

    private boolean topTen;//标记这个user是不是排名前十名  或者是在前20人中  在skipList中

    private ArrayList<String> pokers;
    private boolean first;//标记哪个用户先出牌

    private UserVo opponent;//当前用户的对手
//    private Long opponentId;


    @Override
    public String toString() {
        return "UserVo{" +
                "id=" + id +
                ", nickName='" + nickName + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", happybean=" + happybean +
                ", topTen=" + topTen +
                ", pokers=" + pokers +
                ", first=" + first +
                ", opponent=" + opponent +
                '}';
    }
}
