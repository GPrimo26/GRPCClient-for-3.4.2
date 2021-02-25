package com.grpc.grpcclient.fragments.items;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.grpc.grpcclient.database.DataBase;
import com.grpc.grpcclient.tables.Briefcase;
import com.grpc.grpcclient.tables.Item;

import java.util.List;

public class ItemsViewModel extends ViewModel {
    private MutableLiveData<List<Item>> items;
    private Briefcase briefcase;

    public ItemsViewModel() {
        items = new MutableLiveData<>();
    }

    public LiveData<List<Item>> getItems(int id, DataBase dataBase){
        List<Item> itemList=dataBase.mainDao().getAllItems(id);
        items.setValue(itemList);
        return items;
    }

    public void setItems(List<Item> items_){
        items.setValue(items_);
    }

    public Briefcase getBriefcase(){
        return briefcase;
    }

    public void setBriefcase(Briefcase briefcase_){
        briefcase=briefcase_;
    }

}