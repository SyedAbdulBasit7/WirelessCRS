package com.example.wirelesscrs.Wi_Lect.activities.file;

public class filesUpload {
    public String name;
    public String url;
    public String f_id;
    public filesUpload(){}
    public filesUpload(String name,String url,String f_id){
        this.name=name;
        this.url=url;
        this.f_id=f_id;
    }



    public String getName() {
        return name;
    }

    public String getF_id() {
        return f_id;
    }

    public String getUrl() {
        return url;
    }
}
