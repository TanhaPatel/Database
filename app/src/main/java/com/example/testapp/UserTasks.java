package com.example.testapp;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class UserTasks {
private String task_1;
    public UserTasks(){}

    public UserTasks(String task_1) {
        this.task_1 = task_1;
    }

    public String getTask_1() {
        return task_1;
    }

    public void setTask_1(String task_1) {
        this.task_1 = task_1;
    }
}