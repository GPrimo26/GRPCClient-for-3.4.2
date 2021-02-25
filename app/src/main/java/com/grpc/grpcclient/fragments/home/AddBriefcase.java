package com.grpc.grpcclient.fragments.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.grpc.grpcclient.MainActivity;
import com.grpc.grpcclient.fragments.auth_reg.AuthViewModel;
import com.grpc.grpcclient.tables.Briefcase;
import com.grpc.grpcclient.tables.User;

import java.util.ArrayList;
import java.util.List;

import io.grpc.grpcclient.R;

public class AddBriefcase extends DialogFragment {
    public AddBriefcase(Briefcase briefcase) {
        this.briefcase=briefcase;
    }

    public AddBriefcase(){}

    private Briefcase briefcase;
    private OnDismissListener mListener;


    public interface OnDismissListener{
        void onDismiss(Briefcase briefcase);
    }
    public void setOnDismissClickListener(OnDismissListener dismisslistener) {
        mListener = dismisslistener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_briefcase, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar=view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        TextView title_tv=view.findViewById(R.id.title_tv);
        EditText name_et=view.findViewById(R.id.name);
        EditText balance_et=view.findViewById(R.id.balance);
        Button button=view.findViewById(R.id.save_btn);
        List<EditText> editTexts=new ArrayList<>();
        editTexts.add(name_et);
        editTexts.add(balance_et);
        if (briefcase!=null) {
            title_tv.setText("Редактировать портфель");
            name_et.setText(briefcase.getName());
            balance_et.setText(String.valueOf(briefcase.getBalance()));
        }
        button.setOnClickListener(v->{
            boolean flag=false;
            for (EditText editText:editTexts){
                if (editText.getText().toString().equals("")){
                    editText.setError("Это поле должно быть заполнено.");
                    flag=true;
                }
            }
            if(!flag){
                if (briefcase==null) {
                    AuthViewModel authViewModel=new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
                    briefcase = new Briefcase();
                    briefcase.setName(name_et.getText().toString());
                    briefcase.setBalance(Float.parseFloat(balance_et.getText().toString().trim()));
                    briefcase.setSize(0);
                    briefcase.setUserID(authViewModel.getUser().getValue().getUserId());
                }else {
                    briefcase.setName(name_et.getText().toString());
                    briefcase.setBalance(Float.parseFloat(balance_et.getText().toString().trim()));
                }
                mListener.onDismiss(briefcase);
                dismiss();
            }
        });

    }
}
