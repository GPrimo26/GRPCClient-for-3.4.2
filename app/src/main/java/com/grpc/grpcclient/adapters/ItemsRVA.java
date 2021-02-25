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
import com.grpc.grpcclient.fragments.items.AddItem;
import com.grpc.grpcclient.tables.Briefcase;
import com.grpc.grpcclient.tables.Item;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import io.grpc.grpcclient.R;

public class ItemsRVA extends RecyclerView.Adapter<ItemsRVA.ViewHolder> {
    public ItemsRVA(MainActivity mainActivity, List<Item> items, Briefcase briefcase){
        this.context=mainActivity;
        this.items=items;
        this.briefcase=briefcase;
    }
    private final Context context;
    private List<Item> items;
    private Briefcase briefcase;

    @NonNull
    @Override
    public ItemsRVA.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.card, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onBindViewHolder(@NonNull ItemsRVA.ViewHolder holder, int position) {
        holder.time_tv.setVisibility(View.VISIBLE);
        holder.name_tv.setText(items.get(position).getName());
        String text=items.get(position).getCost()+"P";
        holder.price_tv.setText(text);
        holder.count_tv.setText(String.valueOf(items.get(position).getCount()));
        holder.time_tv.setText(items.get(position).getPaymentTime());
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
                switch (item.getItemId()) {
                    case R.id.delete:
                        ((MainActivity)context).dataBase.mainDao().deleteItem(items.get(position));
                        int size=briefcase.getSize();
                        size--;
                        briefcase.setSize(size);
                        ((MainActivity)context).dataBase.mainDao().updateBriefcase(
                                briefcase.getName(), briefcase.getUserID(), briefcase.getSize(),
                                briefcase.getBalance(), briefcase.getBriefcaseID());
                        items.remove(position);
                        notifyItemRemoved(position);
                        break;
                    case R.id.edit:
                        AddItem dialog = new AddItem(context, items.get(position), briefcase.getBriefcaseID());
                        if (((MainActivity) context).getSupportFragmentManager() != null) {
                            dialog.setOnDismissClickListener(item1 -> {
                                items.set(position, item1);
                                ((MainActivity)context).dataBase.mainDao().updateItem(item1.getName(),
                                        item1.getBriefcaseID(), item1.getCost(), item1.getPaymentTime(),
                                        item1.getCount(), item1.getItemID());
                                notifyDataSetChanged();
                            });
                            dialog.show(((MainActivity) context).getSupportFragmentManager(), "addItem");
                            break;
                        }
                }
                return false;
            });
            popupMenu.show();
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Item> items_) {
        items=items_;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name_tv;
        private final TextView price_tv;
        private final TextView count_tv;
        private final TextView time_tv;
        private final LinearLayout main_ll;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name_tv=itemView.findViewById(R.id.name_tv);
            price_tv=itemView.findViewById(R.id.balance_tv);
            count_tv=itemView.findViewById(R.id.count_tv);
            time_tv=itemView.findViewById(R.id.time_tv);
            main_ll=itemView.findViewById(R.id.main_ll);
        }
    }
}
