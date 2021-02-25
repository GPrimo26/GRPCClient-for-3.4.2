package com.grpc.grpcclient.database;

import android.content.Context;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.grpc.grpcclient.dao_interfaces.MainDao;
import com.grpc.grpcclient.tables.Briefcase;
import com.grpc.grpcclient.tables.Item;
import com.grpc.grpcclient.tables.User;


@Database(entities = {User.class, Briefcase.class, Item.class}, version = 2, exportSchema = false)
public abstract class DataBase extends RoomDatabase {
    private static DataBase dataBase;

    public synchronized static DataBase getInstance(Context context){
        if (dataBase==null){
            String DATABASE_NAME = "database";
            dataBase= Room.databaseBuilder(context.getApplicationContext(), DataBase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build();
        }
        return dataBase;
    }

    public abstract MainDao mainDao();
}
