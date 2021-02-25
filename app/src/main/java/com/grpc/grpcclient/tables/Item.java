package com.grpc.grpcclient.tables;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "Items", foreignKeys = @ForeignKey(entity = Briefcase.class, parentColumns = "BriefcaseID", childColumns = "BriefcaseID", onDelete = CASCADE),
        indices = {@Index(value = {"Name"}, unique = true)})
public class Item implements Serializable {

    @ColumnInfo(name = "ItemID")
    @PrimaryKey(autoGenerate = true)
    private Integer itemID;

    @ColumnInfo(name = "Name")
    private String name;

    @ColumnInfo(name = "BriefcaseID")
    private Integer briefcaseID;

    @ColumnInfo(name = "Cost")
    private Float cost;

    @ColumnInfo(name = "PaymentTime")
    private String paymentTime;

    @ColumnInfo(name = "Count")
    private Integer count;

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer ID) {
        this.itemID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBriefcaseID() {
        return briefcaseID;
    }

    public void setBriefcaseID(Integer briefcaseID) {
        this.briefcaseID = briefcaseID;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
