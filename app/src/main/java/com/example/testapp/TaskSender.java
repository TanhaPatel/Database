package com.example.testapp;

import java.util.Calendar;
import java.util.Date;

public class TaskSender {

    String id, task;
    Date datentime;

    public TaskSender(){}

    public TaskSender( Date datentime, String id, String task) {
        this.datentime = datentime;
        this.id = id;
        this.task = task;
    }

    public String getId() {
        return id;
    }

    public Date getDatentime() {
        return datentime;
    }

    public void setDatentime(Date datentime) {
        this.datentime = datentime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
