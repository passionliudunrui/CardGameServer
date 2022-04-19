package com.cardgameserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    private Long id;
    private Long idme;
    private Long idops;
    private Integer happybean;
    private Date date;

    public Note(Long idme, Long idops, Integer happybean, Date date){
        this.idme=idme;
        this.idops=idops;
        this.happybean=happybean;
        this.date=date;
    }

}
