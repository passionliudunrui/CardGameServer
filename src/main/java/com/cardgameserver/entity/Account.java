package com.cardgameserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private Long id;
    private Integer balance;
    private Double happybean;

}
