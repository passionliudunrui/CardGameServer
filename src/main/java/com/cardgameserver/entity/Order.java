package com.cardgameserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Order {

    private Integer id;
    private Long userId;
    private Integer goodsId;
    private Integer price;
    private Integer happybean;
    private String goodsName;

}
