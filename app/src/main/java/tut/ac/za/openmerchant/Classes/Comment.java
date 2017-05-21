package tut.ac.za.openmerchant.Classes;

import java.io.Serializable;

/**
 * Created by ProJava on 2/26/2017.
 */

public class Comment implements Serializable {

    private String userID;
    private String comment;
    private String date;
    private String email;

    public Comment()
    {
        super();
    }

    public Comment(String comment, String date, String userID, String email) {
        this.comment = comment;
        this.date = date;
        this.userID = userID;
        this.email = email;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
