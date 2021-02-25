package com.grpc.grpcclient.tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "Briefcases",
        foreignKeys = {@ForeignKey(entity = User.class, parentColumns = "UserID", childColumns = "UserID",
                onDelete = CASCADE)},
        indices = {@Index(value = {"Name"}, unique = true)})
public class Briefcase implements Serializable {

    @ColumnInfo(name = "BriefcaseID")
    @PrimaryKey(autoGenerate = true)
    private Integer BriefcaseID;

    @ColumnInfo(name = "Name")
    private String name;

    @ColumnInfo(name = "UserID")
    private Integer userID;

    @ColumnInfo(name = "Size")
    private Integer size;

    @ColumnInfo(name = "BriefcaseBalance")
    private Float balance;

    public Integer getBriefcaseID() {
        return BriefcaseID;
    }

    public void setBriefcaseID(Integer briefcaseID) {
        BriefcaseID = briefcaseID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }
}
