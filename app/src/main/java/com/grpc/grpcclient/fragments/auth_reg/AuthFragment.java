package com.grpc.grpcclient.fragments.auth_reg;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.grpc.grpcclient.MainActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import gprimo.grpc.userauth.User;
import io.grpc.grpcclient.R;

public class AuthFragment extends Fragment {

    private AuthViewModel mViewModel;
    private EditText login_et;
    private EditText password_et;
    private TextView signIn_tv;
    private MaterialButton logIn_btn;

    public static AuthFragment newInstance() {
        return new AuthFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.auth_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        mViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (mViewModel.getUser().getValue()!=null){
                ((MainActivity)requireActivity()).progressDialog.setMessage(getResources().getString(R.string.authorization_progress));
                ((MainActivity)requireActivity()).progressDialog.show();
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                        .navigate(R.id.action_authFragment_to_homeFragment);
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findIDs(view);
        setActions();
    }

    private void findIDs(@NotNull View view) {
        login_et=view.findViewById(R.id.login_et);
        password_et=view.findViewById(R.id.password_et);
        signIn_tv=view.findViewById(R.id.reg_tv);
        logIn_btn=view.findViewById(R.id.login_btn);

    }

    private void setActions(){
        logIn_btn.setOnClickListener(v->{
            if((!login_et.getText().toString().trim().equals(""))
                    &&(!password_et.getText().toString().trim().equals(""))){
                mViewModel.signIn(login_et.getText().toString(), password_et.getText().toString());
            }
        });
        signIn_tv.setOnClickListener(v-> Navigation.findNavController(requireActivity(),
                R.id.nav_host_fragment).navigate(R.id.action_authFragment_to_registrationFragment));
    }
}