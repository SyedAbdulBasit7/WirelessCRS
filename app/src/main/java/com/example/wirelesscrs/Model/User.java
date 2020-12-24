package com.example.wirelesscrs.Model;

public class User {


    public String name,semester,course,sect;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String semester, String course, String sect) {
        this.name = name;
        this.semester = semester;
        this.course = course;
        this.sect = sect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getcourse() {
        return course;
    }

    public void setcourse(String branch) {
        this.course = course;
    }

    public String getSect() {
        return sect;
    }

    public void setSect(String secti) {
        this.sect = secti;
    }
}