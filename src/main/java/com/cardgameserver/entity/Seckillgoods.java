package com.cardgameserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seckillgoods {

    private Integer id;
    private Integer price;
    private Integer happybean;
    private Integer stock;
    private Integer version;
    private String goodName;
    private Date startDate;
    private Date endDate;

}
