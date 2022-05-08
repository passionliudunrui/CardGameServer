package com.cardgameserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

public class Seckillgoods {

    private Integer id;
    private Integer price;
    private Double happybean;
    private Integer stock;
    private Integer version;
    private String goodName;
    private Date startDate;
    private Date endDate;

    public Seckillgoods() {
    }

    public Seckillgoods(Integer price, Double happybean, Integer stock, Integer version, String goodName, Date startDate, Date endDate) {
        this.price = price;
        this.happybean = happybean;
        this.stock = stock;
        this.version = version;
        this.goodName = goodName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Seckillgoods{" +
                "id=" + id +
                ", price=" + price +
                ", happybean=" + happybean +
                ", stock=" + stock +
                ", version=" + version +
                ", goodName='" + goodName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
