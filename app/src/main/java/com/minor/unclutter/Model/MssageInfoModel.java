package com.minor.unclutter.Model;

import java.io.Serializable;

public class MssageInfoModel implements Serializable {
    String sender;
    String day;
    String message;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
