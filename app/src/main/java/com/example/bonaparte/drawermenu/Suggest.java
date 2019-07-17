package com.example.bonaparte.drawermenu;

/**
 * Created by Bonaparte on 3/27/2018.
 */

public class Suggest {
    public String title, description,user_name;

    public Suggest() {
    }

    public Suggest(String title, String description,  String user_name) {
        this.title = title;
        this.description = description;

        this.user_name = user_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_namee(String user_name) {
        this.user_name = user_name;
    }
}
