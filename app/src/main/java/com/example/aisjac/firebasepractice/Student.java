package com.example.aisjac.firebasepractice;

import java.io.Serializable;

public class Student implements Serializable {
    private String id;

    private String breakfast,name_initial, dinner, daily_shop, date;

    public Student(String id,String name_initial, String breakfast, String dinner, String daily_shop, String date) {
        this.id = id;
        this.name_initial = name_initial;
        this.breakfast = breakfast;
        this.dinner = dinner;
        this.daily_shop = daily_shop;
        this.date = date;
    }

    public Student() {

    }

    public String getId() {
        return id;
    }

    public String getName_initial() {
        return name_initial;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public String getDinner() {
        return dinner;
    }

    public String getDaily_shop() {
        return daily_shop;
    }

    public String getDate() {
        return date;
    }
}
