package com.grpc.grpcclient.fragments.auth_reg;

import androidx.activity.OnBackPressedCallback;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import gprimo.grpc.userreg.Response;
import io.grpc.grpcclient.R;

public class RegistrationFragment extends Fragment {
    private int backFlag=0;
    private RegistrationViewModel mViewModel;
    private LinearLayout first_ll, second_ll;
    private ConstraintLayout main_cl;
    private MaterialButton apply_btn;
    private EditText email_et, password_et, confirmPass_et, fname_et, lname_et, phone_et;
    private final View.OnClickListener onNextClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean isValid=true;
            if (email_et.getText().toString().trim().equals("")){
                email_et.setError("Заполните данное поле");
                isValid=false;
            }
            if (password_et.getText().toString().trim().equals("")){
                password_et.setError("Заполните данное поле");
                isValid=false;
            }
            if (confirmPass_et.getText().toString().trim().equals("")){
                confirmPass_et.setError("Заполните данное поле");
                isValid=false;
            };
            if (!password_et.getText().toString().trim().equals(confirmPass_et.getText().toString().trim())){
                confirmPass_et.setError("Пароли должны совпадать");
                isValid=false;
            }
            if (isValid) {
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(main_cl);
                constraintSet.clear(R.id.first_ll, ConstraintSet.END);
                constraintSet.clear(R.id.first_ll, ConstraintSet.START);
                constraintSet.connect(R.id.first_ll, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.START);
                TransitionManager.beginDelayedTransition(main_cl);
                constraintSet.applyTo(main_cl);

                constraintSet.clear(R.id.second_ll, ConstraintSet.START);
                constraintSet.connect(R.id.second_ll, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
                constraintSet.connect(R.id.second_ll, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
                TransitionManager.beginDelayedTransition(main_cl);
                constraintSet.applyTo(main_cl);
                backFlag = 1;
                apply_btn.setOnClickListener(applyClickListener);
            }
        }
    };
    private final View.OnClickListener applyClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            List<EditText> editTexts=new ArrayList<EditText>();
            editTexts.add(fname_et);
            editTexts.add(lname_et);
            editTexts.add(password_et);
            boolean isValid=true;
            for (EditText editText: editTexts){
                if (editText.getText().toString().trim().equals("")){
                    editText.setError("Данное поле должно быть заполнено");
                    isValid=false;
                }
            }
            if(isValid) {
                mViewModel.registrate(email_et.getText().toString().trim(), password_et.getText().toString().trim(),
                        phone_et.getText().toString().trim(), fname_et.getText().toString().trim(),
                        lname_et.getText().toString().trim());

            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (backFlag==1){
                        ConstraintSet constraintSet=new ConstraintSet();
                        constraintSet.clone(main_cl);
                        constraintSet.clear(R.id.second_ll, ConstraintSet.START);
                        constraintSet.clear(R.id.second_ll, ConstraintSet.END);
                        constraintSet.connect(R.id.second_ll, ConstraintSet.START, ConstraintSet.PARENT_ID,
                                ConstraintSet.END);
                        TransitionManager.beginDelayedTransition(main_cl);
                        constraintSet.applyTo(main_cl);

                        constraintSet.clear(R.id.first_ll, ConstraintSet.END);
                        constraintSet.connect(R.id.first_ll, ConstraintSet.END, ConstraintSet.PARENT_ID,
                                ConstraintSet.END);
                        constraintSet.connect(R.id.first_ll, ConstraintSet.START, ConstraintSet.PARENT_ID,
                                ConstraintSet.START);
                        TransitionManager.beginDelayedTransition(main_cl);
                        constraintSet.applyTo(main_cl);
                        backFlag=0;
                        apply_btn.setOnClickListener(onNextClickListener);
                }else {
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigateUp();
                }
            }
        });
    }

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(RegistrationViewModel.class);
        mViewModel.getResponse().observe(getViewLifecycleOwner(), new Observer<Response>() {
            @Override
            public void onChanged(Response response) {
                Toast.makeText(requireContext(), response.getMessage(),Toast.LENGTH_SHORT).show();
                if(response.getCode()==1){
                    email_et.setText("");
                    password_et.setText("");
                    confirmPass_et.setText("");
                    fname_et.setText("");
                    lname_et.setText("");
                    phone_et.setText("");
                    Navigation.findNavController(requireActivity(),R.id.nav_host_fragment).navigateUp();
                }
            }
        });
        return inflater.inflate(R.layout.registration_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findIDs(view);
        setActions();

    }

    private void findIDs(View view) {
        main_cl=view.findViewById(R.id.main_cl);
        first_ll=view.findViewById(R.id.first_ll);
        second_ll=view.findViewById(R.id.second_ll);
        apply_btn=view.findViewById(R.id.apply_btn);
        email_et=view.findViewById(R.id.login_et);
        password_et=view.findViewById(R.id.password_et);
        confirmPass_et=view.findViewById(R.id.confirm_pass_et);
        fname_et=view.findViewById(R.id.fname_et);
        lname_et=view.findViewById(R.id.lname_et);
        phone_et=view.findViewById(R.id.phone_et);
    }

    private void setActions() {
        apply_btn.setOnClickListener(onNextClickListener);
    }
}