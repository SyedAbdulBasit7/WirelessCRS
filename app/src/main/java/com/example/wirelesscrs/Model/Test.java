package com.example.wirelesscrs.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class Test implements Serializable{
    public String name;
    public ArrayList<Question> questions;
    public Long time;

    public Test() {
    }
    public Test(String name,ArrayList<Question> questions, Long time){
        this.name=name;
        this.questions=questions;
        this.time=time;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }
}