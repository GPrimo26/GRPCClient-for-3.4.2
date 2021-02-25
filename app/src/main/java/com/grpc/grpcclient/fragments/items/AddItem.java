package com.grpc.grpcclient.fragments.items;

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

import com.google.android.material.datepicker.MaterialDatePicker;
import com.grpc.grpcclient.MainActivity;
import com.grpc.grpcclient.tables.Item;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.grpc.grpcclient.R;

public class AddItem extends DialogFragment {

    public AddItem(MainActivity mainActivity, Integer briefcaseID) {
        this.mainActivity=mainActivity;
        this.briefcaseID=briefcaseID;
    }
    private MainActivity mainActivity;
    Integer briefcaseID;
    private Item item;
    private OnDismissListener mListener;

    public AddItem(Context context, Item item, Integer briefcaseID) {
        this.mainActivity=(MainActivity) context;
        this.item=item;
        this.briefcaseID=briefcaseID;
    }


    public interface OnDismissListener{
        void onDismiss(Item item);
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
        return inflater.inflate(R.layout.dialog_add_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar=view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        TextView title_tv=view.findViewById(R.id.title_tv);
        EditText name_et=view.findViewById(R.id.name);
        EditText cost_et=view.findViewById(R.id.cost);
        EditText count_et=view.findViewById(R.id.count);
        TextView time_tv=view.findViewById(R.id.time);
        Button button=view.findViewById(R.id.save_btn);
        List<EditText> editTexts=new ArrayList<>();
        editTexts.add(name_et);
        editTexts.add(cost_et);
        editTexts.add(count_et);
        if (item!=null) {
            title_tv.setText("Редактировать предмет");
            name_et.setText(item.getName());
            cost_et.setText(String.valueOf(item.getCost()));
            count_et.setText(String.valueOf(item.getCount()));
            time_tv.setText(item.getPaymentTime());
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
                if (item==null) {
                    item = new Item();
                }
                item.setName(name_et.getText().toString());
                item.setCost(Float.parseFloat(cost_et.getText().toString().trim()));
                item.setCount(Integer.parseInt(count_et.getText().toString().trim()));
                item.setPaymentTime(time_tv.getText().toString());
                item.setBriefcaseID(briefcaseID);
                mListener.onDismiss(item);
                dismiss();
            }
        });
        time_tv.setOnClickListener(v -> {
            MaterialDatePicker.Builder<Long> dateBuilder= MaterialDatePicker.Builder.datePicker();
            dateBuilder.setTitleText("Выберите дату сделки");
            final MaterialDatePicker<Long> materialDatePicker = dateBuilder.build();
            materialDatePicker.show(mainActivity.getSupportFragmentManager(), "MDP");
            materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                Date date=new Date(selection);
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd", new Locale("Ru"));
                String time=format.format(date);
                time_tv.setText(time);
            });
        });
    }
}
