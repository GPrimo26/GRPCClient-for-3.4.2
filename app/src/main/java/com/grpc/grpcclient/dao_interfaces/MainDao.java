package com.grpc.grpcclient.dao_interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import com.grpc.grpcclient.tables.Briefcase;
import com.grpc.grpcclient.tables.Item;
import com.grpc.grpcclient.tables.User;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MainDao {

    @Insert(onConflict = REPLACE)
    void createUser(User userData);

    @Query("select * from Users where UserID=:id")
    User getUser(Integer id);

    @Insert(onConflict = REPLACE)
    void createBriefcase(Briefcase briefcase);

    @Delete
    void deleteBriefcase(Briefcase briefcase);

    @Query("update Briefcases set Name=:name, UserID=:userId, Size=:size, BriefcaseBalance=:balance where BriefcaseID=:id")
    void updateBriefcase(String name, Integer userId, Integer size, Float balance, Integer id);

    @Query("select * from Briefcases, Users where Users.UserID=:userId")
    List<Briefcase> getAllBriefcases(int userId);

    @Insert(onConflict = REPLACE)
    void createItem(Item item);

    @Delete
    void deleteItem(Item item);

    @Query("update Items set Name=:name, BriefcaseID=:briefcaseID, Cost=:cost, PaymentTime=:paymentTime, Count=:count where ItemID=:id")
    void updateItem(String name, Integer briefcaseID, Float cost, String paymentTime, Integer count, Integer id);

    @Query("select * from Items where BriefcaseID=:briefcaseId")
    List<Item> getAllItems(int briefcaseId);

}
