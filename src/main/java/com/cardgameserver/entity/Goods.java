package com.cardgameserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Goods {
    private Integer id;
    private Integer price;
    private Double happybean;
    private String goodName;
}
