package com.grpc.grpcclient.fragments.auth_reg;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import gprimo.grpc.userauth.AuthorizationGrpc;
import gprimo.grpc.userauth.LoginRequest;
import gprimo.grpc.userauth.User;
import gprimo.grpc.userreg.RegistrationGrpc;
import gprimo.grpc.userreg.Response;
import gprimo.grpc.userreg.SignInRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class RegistrationViewModel extends ViewModel {
    public RegistrationViewModel() {
        responseMutableLiveData=new MutableLiveData<>();
    }

    public interface OnTaskCompleted{
        void onTaskCompleted(Response response);
    }
    private MutableLiveData<Response> responseMutableLiveData;
    private OnTaskCompleted onTaskCompleted;

    public void registrate(String email, String password, String phoneNumber, String firstName,
                               String lastName){
        onTaskCompleted= response -> responseMutableLiveData.setValue(response);
        new GRPCRegTask(onTaskCompleted).execute(email, password, phoneNumber, firstName, lastName);
    }

    public LiveData<Response> getResponse(){
        return responseMutableLiveData;
    }

    private static class GRPCRegTask extends AsyncTask<String, Void, Response> {
        private final OnTaskCompleted onTaskCompleted;
        public GRPCRegTask(OnTaskCompleted onTaskCompleted) {
            this.onTaskCompleted=onTaskCompleted;
        }

        @Override
        protected Response doInBackground(String... strings) {
            ManagedChannel channel = ManagedChannelBuilder.forAddress("10.0.2.2", 80)
                    .usePlaintext().build();
            RegistrationGrpc.RegistrationBlockingStub stub=RegistrationGrpc.newBlockingStub(channel);
            SignInRequest request=SignInRequest.newBuilder().setEmail(strings[0]).setPassword(strings[1])
                    .setPhoneNumber(strings[2]).setFirstName(strings[3]).setLastName(strings[4]).build();
            return stub.signIn(request);
        }

        @Override
        protected void onPostExecute(Response response) {
            onTaskCompleted.onTaskCompleted(response);
        }
    }
}