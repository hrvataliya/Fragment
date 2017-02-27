package com.brainstorm.hardik.allwishas.model;

import java.util.ArrayList;

/**
 * Created by Ashish on 23/02/2017.
 */

public class AllDataRespones {

    public String status;
    public String message;
    ArrayList<TextData> Text;
    ArrayList<ImageData> Image;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<TextData> getText() {
        return Text;
    }

    public void setText(ArrayList<TextData> text) {
        Text = text;
    }

    public ArrayList<ImageData> getImage() {
        return Image;
    }

    public void setImage(ArrayList<ImageData> image) {
        Image = image;
    }
}
