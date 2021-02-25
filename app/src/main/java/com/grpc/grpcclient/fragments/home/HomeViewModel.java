package com.grpc.grpcclient.fragments.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.grpc.grpcclient.database.DataBase;
import com.grpc.grpcclient.tables.Briefcase;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<List<Briefcase>> briefcases;

    public HomeViewModel() {
        briefcases = new MutableLiveData<>();
    }

    public LiveData<List<Briefcase>> getBriefcases(int id, DataBase dataBase){
        List<Briefcase> briefcaseList=dataBase.mainDao().getAllBriefcases(id);
        briefcases.setValue(briefcaseList);
        return briefcases;
    }

    public void setBriefcases(List<Briefcase> briefcases_){
        briefcases.setValue(briefcases_);
    }


}