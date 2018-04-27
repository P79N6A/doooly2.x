package com.doooly.entity.report;

import java.util.Date;

/**
 * Created by john on 18/4/27.
 */
public class SendPackRecord {
    private long id;
    private long user_id;
    private String open_id;
    private long source_user_id;
    private String source_open_id;
    private byte[] source_nike_name;
    private String channel;
    private String result;
    private Date create_date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getOpen_id() {
        return open_id;
    }

    public void setOpen_id(String open_id) {
        this.open_id = open_id;
    }

    public long getSource_user_id() {
        return source_user_id;
    }

    public void setSource_user_id(long source_user_id) {
        this.source_user_id = source_user_id;
    }

    public String getSource_open_id() {
        return source_open_id;
    }

    public void setSource_open_id(String source_open_id) {
        this.source_open_id = source_open_id;
    }

    public byte[] getSource_nike_name() {
        return source_nike_name;
    }

    public void setSource_nike_name(byte[] source_nike_name) {
        this.source_nike_name = source_nike_name;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    @Override
    public String toString() {
        return "SendPackRecord{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", open_id='" + open_id + '\'' +
                ", source_user_id=" + source_user_id +
                ", source_open_id='" + source_open_id + '\'' +
                ", source_nike_name='" + source_nike_name + '\'' +
                ", channel='" + channel + '\'' +
                ", result='" + result + '\'' +
                ", create_date=" + create_date +
                '}';
    }
}