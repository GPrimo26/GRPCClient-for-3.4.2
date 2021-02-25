package com.grpc.grpcclient.fragments.items;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.grpc.grpcclient.MainActivity;
import com.grpc.grpcclient.adapters.ItemsRVA;
import com.grpc.grpcclient.tables.Briefcase;
import com.grpc.grpcclient.tables.Item;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.grpc.grpcclient.R;

public class ItemsFragment extends Fragment {

    private ItemsViewModel mViewModel;
    private Briefcase briefcase;
    private RecyclerView items_rv;
    private ItemsRVA adapter;
    private List<Item> items;
    private TextView title_tv, subTitle_tv;
    private FloatingActionButton addItem_fab;


    public static ItemsFragment newInstance() {
        return new ItemsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel=new ViewModelProvider(requireActivity()).get(ItemsViewModel.class);
        briefcase=mViewModel.getBriefcase();
        items=((MainActivity)requireActivity()).dataBase.mainDao().getAllItems(briefcase.getBriefcaseID());
        adapter=new ItemsRVA((MainActivity)requireActivity(), items, briefcase);
        mViewModel.getItems(briefcase.getBriefcaseID(), ((MainActivity)requireActivity()).dataBase)
                .observe(getViewLifecycleOwner(), items_ -> {
                    items=items_;
                    if (adapter!=null){
                        adapter.setItems(items);
                    }
                });

        return inflater.inflate(R.layout.items_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findIDs(view);
        setInfo();
        setActions();
    }

    private void findIDs(@NotNull View view) {
        title_tv=view.findViewById(R.id.title_tv);
        subTitle_tv=view.findViewById(R.id.subtitle_tv);
        items_rv=view.findViewById(R.id.items_rv);
        addItem_fab=view.findViewById(R.id.add_item_fab);

    }

    private void setInfo(){
        title_tv.setText(briefcase.getName());
        subTitle_tv.setText(String.valueOf(briefcase.getBalance()));
        items_rv.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        items_rv.setAdapter(adapter);
    }
    private void setActions(){
        addItem_fab.setOnClickListener(v->{
            AddItem dialog=new AddItem((MainActivity)requireActivity(), briefcase.getBriefcaseID());
            if(requireActivity().getSupportFragmentManager()!=null){
                dialog.setOnDismissClickListener(item1 -> {
                    ((MainActivity)requireActivity()).dataBase.mainDao().createItem(item1);
                    items=mViewModel.getItems(briefcase.getBriefcaseID(), ((MainActivity)requireActivity()).dataBase).getValue();
                    mViewModel.setItems(items);
                    int size=briefcase.getSize();
                    size++;
                    briefcase.setSize(size);
                    ((MainActivity)requireActivity()).dataBase.mainDao().updateBriefcase(
                            briefcase.getName(), briefcase.getUserID(), briefcase.getSize(),
                            briefcase.getBalance(), briefcase.getBriefcaseID());
                });
                dialog.show(requireActivity().getSupportFragmentManager(), "addBr");

            }
        });
    }
}