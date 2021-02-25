/*
 * Copyright 2015 The gRPC Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.grpc.grpcclient;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.grpc.grpcclient.database.DataBase;
import com.grpc.grpcclient.fragments.auth_reg.AuthFragment;
import com.grpc.grpcclient.fragments.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

import io.grpc.grpcclient.R;

public class MainActivity extends AppCompatActivity {

  public BottomNavigationView navView;
  public ProgressDialog progressDialog;
  boolean doubleBackToExitPressedOnce = false;
  public DataBase dataBase;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    findIds();
    dataBase= DataBase.getInstance(MainActivity.this);
    progressDialog = new ProgressDialog(this,
            R.style.AppTheme_Dark_Dialog);
    progressDialog.setIndeterminate(true);
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
    NavigationUI.setupWithNavController(navView, navController);
  }

  private void findIds() {
    navView = findViewById(R.id.nav_view);

  }

    /*new GrpcTask(this)
        .execute(
            hostEdit.getText().toString(),
            messageEdit.getText().toString(),
            portEdit.getText().toString());*/

  /*private static class GrpcTask extends AsyncTask<String, Void, String> {
    private final WeakReference<Activity> activityReference;
    private ManagedChannel channel;

    private GrpcTask(Activity activity) {
      this.activityReference = new WeakReference<Activity>(activity);
    }

    @Override
    protected String doInBackground(String... params) {
      String host = params[0];
      String message = params[1];
      String portStr = params[2];
      int port = TextUtils.isEmpty(portStr) ? 0 : Integer.valueOf(portStr);
      try {
        host="127.0.0.1";
        channel = ManagedChannelBuilder.forAddress("10.0.2.2", 80).usePlaintext().build();
        GreeterGrpc.GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(channel);
        HelloRequest request = HelloRequest.newBuilder().setName(message).build();
        HelloReply reply = stub.sayHello(request);
        return reply.getMessage();
      } catch (Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.flush();
        return String.format("Failed... : %n%s", sw);
      }
    }

    @Override
    protected void onPostExecute(String result) {
      try {
        channel.shutdown().awaitTermination(1, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
      Activity activity = activityReference.get();
      if (activity == null) {
        return;
      }
      TextView resultText = (TextView) activity.findViewById(R.id.grpc_response_text);
      Button sendButton = (Button) activity.findViewById(R.id.send_button);
      resultText.setText(result);
      Log.d("TAG", result);
      sendButton.setEnabled(true);
    }
  }*/

  @Override
  public void onBackPressed() {
    List<Fragment> fragments = new ArrayList<>(getSupportFragmentManager().getFragments().get(0).getChildFragmentManager().getFragments());
    if ((fragments.get(0) instanceof AuthFragment) || (fragments.get(0) instanceof HomeFragment)) {
      if (doubleBackToExitPressedOnce) {
        finish();
        return;
      }
      this.doubleBackToExitPressedOnce = true;
      Toast.makeText(this, "Нажмите еще раз, чтобы выйти", Toast.LENGTH_SHORT).show();

      new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
    } else {
      super.onBackPressed();
    }
  }
}
