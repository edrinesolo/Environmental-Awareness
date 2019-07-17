package com.example.bonaparte.drawermenu;

/**
 * Created by Bonaparte on 3/2/2018.
 */

public class Posts {
    public String title, description, image, eventdate, user_name;


    //empty consqtructor if we ever h`ave to cre`ate objectsq `and set properties l`ater
    public Posts() {
    }

    public Posts(String title, String description, String image, String eventdate, String user_name) {
        this.title = title;
        this.description = description;
        this.image = image;
       // this.eventdate = eventdate;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEventdate() {
        return eventdate;
    }

    public void setEventdate(String eventdate) {
        this.eventdate = eventdate;
    }

    public String getPlace_name() {
        return user_name;
    }

    public void setPlace_name(String user_name) {
        this.user_name = user_name;
    }
}


