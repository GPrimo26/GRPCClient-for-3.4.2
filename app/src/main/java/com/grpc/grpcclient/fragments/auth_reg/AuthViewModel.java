package com.grpc.grpcclient.fragments.auth_reg;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import gprimo.grpc.userauth.AuthorizationGrpc;
import gprimo.grpc.userauth.LoginRequest;
import gprimo.grpc.userauth.Response;
import gprimo.grpc.userauth.User;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;

public class AuthViewModel extends ViewModel {
    private final MutableLiveData<User> userMutableLiveData;

    public AuthViewModel() {
        userMutableLiveData=new MutableLiveData<>();
    }

    public interface OnTaskCompleted{
        void onTaskCompleted(User user);
    }

    public void signIn(String email, String password){
        OnTaskCompleted onTaskCompleted = userMutableLiveData::setValue;
        new GRPCAuthTask(onTaskCompleted).execute(email, password);
    }

    public LiveData<User> getUser(){
        return userMutableLiveData;
    }

    public void setUser(User user){
        userMutableLiveData.setValue(user);
    }

   private static class GRPCAuthTask extends AsyncTask<String, Void, User>{
       private final OnTaskCompleted onTaskCompleted;
       public GRPCAuthTask(OnTaskCompleted onTaskCompleted) {
           this.onTaskCompleted=onTaskCompleted;
       }

       @Override
       protected User doInBackground(String... strings) {
           ManagedChannel channel = ManagedChannelBuilder.forAddress("10.0.2.2", 80).usePlaintext().build();
           AuthorizationGrpc.AuthorizationBlockingStub stub=AuthorizationGrpc.newBlockingStub(channel);
           LoginRequest request=LoginRequest.newBuilder().setUserName(strings[0]).setPassword(strings[1]).build();
           Response response=stub.logIn(request);
           return response.getUser();
       }

       @Override
       protected void onPostExecute(User user) {
           onTaskCompleted.onTaskCompleted(user);
       }
   }
}