package com.pusilkom.hris.dto.view;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class AuthItemDetail {

    private Long id;
    private String name;
    private Integer type;
    private String description;
    private String userCreate;
    private String userUpdate;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date timestampCreate;
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private Date timestampUpdate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserCreate() {
        return userCreate;
    }

    public void setUserCreate(String userCreate) {
        this.userCreate = userCreate;
    }

    public String getUserUpdate() {
        return userUpdate;
    }

    public void setUserUpdate(String userUpdate) {
        this.userUpdate = userUpdate;
    }

    public Date getTimestampCreate() {
        return timestampCreate;
    }

    public void setTimestampCreate(Date timestampCreate) {
        this.timestampCreate = timestampCreate;
    }

    public Date getTimestampUpdate() {
        return timestampUpdate;
    }

    public void setTimestampUpdate(Date timestampUpdate) {
        this.timestampUpdate = timestampUpdate;
    }
}
