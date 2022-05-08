package com.cardgameserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class Order {

    private Integer id;
    private Long userId;
    private Integer goodsId;
    private Integer price;
    private Double happybean;
    private String goodsName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Double getHappybean() {
        return happybean;
    }

    public void setHappybean(Double happybean) {
        this.happybean = happybean;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Order() {
    }

    public Order(Long userId, Integer goodsId, Integer price, Double happybean, String goodsName) {

        this.userId = userId;
        this.goodsId = goodsId;
        this.price = price;
        this.happybean = happybean;
        this.goodsName = goodsName;
    }
}
