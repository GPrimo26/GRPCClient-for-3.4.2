package com.grpc.grpcclient.tables;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Users", indices = {@Index(value = {"Email"}, unique = true)})
public class User implements Serializable {

    @ColumnInfo(name = "UserID")
    @PrimaryKey()
    private int ID;

    @ColumnInfo(name="Email")
    private String email;

    @ColumnInfo(name = "FName")
    private String fName;

    @ColumnInfo(name="LName")
    private String lName;

    @ColumnInfo(name = "Phone")
    private String phone;

    @ColumnInfo(name = "Photo")
    private String photoPath;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFName() {
        return fName;
    }

    public void setFName(String fName) {
        this.fName = fName;
    }

    public String getLName() {
        return lName;
    }

    public void setLName(String lName) {
        this.lName = lName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
