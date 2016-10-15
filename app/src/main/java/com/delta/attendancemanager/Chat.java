package com.delta.attendancemanager;

/**
 * Handle the view  and the methods for the chat system
 */
public class Chat {
    private String time,date,msg;

    /**
     * Set the time of a chat message
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this        .date = date;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getMsg() {
        return msg;
    }
}
