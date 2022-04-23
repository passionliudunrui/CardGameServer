package com.cardgameserver.vo;

import com.cardgameserver.entity.Account;
import com.cardgameserver.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserVo {
    private User user;
    private Account account;
    private boolean topTen;//标记这个user是不是排名前十名  或者是在前20人中  在skipList中

}
