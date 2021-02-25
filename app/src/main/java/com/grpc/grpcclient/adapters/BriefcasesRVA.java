package com.grpc.grpcclient.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.grpc.grpcclient.MainActivity;
import com.grpc.grpcclient.database.DataBase;
import com.grpc.grpcclient.fragments.home.AddBriefcase;
import com.grpc.grpcclient.tables.Briefcase;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.grpc.grpcclient.R;

public class BriefcasesRVA extends RecyclerView.Adapter<BriefcasesRVA.ViewHolder> {
    public BriefcasesRVA(MainActivity mainActivity, List<Briefcase> briefcases) {
        this.context=mainActivity;
        this.briefcases=briefcases;
    }

    private List<Briefcase> briefcases;
    private final Context context;
    private static OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onBriefcaseClick(Briefcase briefcase);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public BriefcasesRVA.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.card, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onBindViewHolder(@NonNull BriefcasesRVA.ViewHolder holder, int position) {
        if (briefcases.get(position).getName()!=null){
            if (!briefcases.get(position).getName().equals("")) {
                holder.name_tv.setText(briefcases.get(position).getName());
            }else {
                holder.name_tv.setText("Не указано");
            }
        }else {
            holder.name_tv.setText("Не указано");
        }

        if (briefcases.get(position).getBalance()!=null){
            holder.balance_tv.setText(String.valueOf(briefcases.get(position).getBalance()));
        }else {
            holder.balance_tv.setText("Не указано");
        }

        if (briefcases.get(position).getSize()!=null){
            holder.count_tv.setText(String.valueOf(briefcases.get(position).getSize()));
        }else {
            holder.count_tv.setText("Не указано");
        }
        holder.main_ll.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onBriefcaseClick(briefcases.get(position));
            }
        });

        holder.main_ll.setOnLongClickListener(v -> {
            PopupMenu popupMenu=new PopupMenu(context, v);
            popupMenu.inflate(R.menu.popup);
            try {
                Field[] fields = popupMenu.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if ("mPopup".equals(field.getName())) {
                        field.setAccessible(true);
                        Object menuPopupHelper = field.get(popupMenu);
                        Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                        Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                        setForceIcons.invoke(menuPopupHelper, true);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            popupMenu.setOnMenuItemClickListener(item -> {
                DataBase dataBase = DataBase.getInstance(context);
                switch (item.getItemId()) {
                    case R.id.delete:
                        dataBase.mainDao().deleteBriefcase(briefcases.get(position));
                        briefcases.remove(position);
                        notifyItemRemoved(position);
                        break;
                    case R.id.edit:
                        AddBriefcase dialog = new AddBriefcase(briefcases.get(position));
                        if (((MainActivity) context).getSupportFragmentManager() != null) {
                            dialog.setOnDismissClickListener(briefcase -> {
                                briefcases.set(position, briefcase);
                                dataBase.mainDao().updateBriefcase(briefcase.getName(), briefcase.getUserID(), briefcase.getSize(),
                                        briefcase.getBalance(), briefcase.getBriefcaseID());
                                notifyDataSetChanged();
                            });
                            dialog.show(((MainActivity) context).getSupportFragmentManager(), "addBr");
                            break;
                        }
                }
                return false;
            });
            popupMenu.show();
            return false;
        });    }

    @Override
    public int getItemCount() {
        return briefcases.size();
    }

    public void setBriefcases(List<Briefcase> briefcases_){
        briefcases=new ArrayList<>(briefcases_);
        notifyDataSetChanged();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name_tv, balance_tv, count_tv;
        private LinearLayout main_ll;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name_tv=itemView.findViewById(R.id.name_tv);
            balance_tv=itemView.findViewById(R.id.balance_tv);
            count_tv=itemView.findViewById(R.id.count_tv);
            main_ll=itemView.findViewById(R.id.main_ll);
        }
    }
}
