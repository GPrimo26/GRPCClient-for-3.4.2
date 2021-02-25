package com.grpc.grpcclient.fragments.home;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.grpc.grpcclient.MainActivity;
import com.grpc.grpcclient.adapters.BriefcasesRVA;
import com.grpc.grpcclient.fragments.auth_reg.AuthViewModel;
import com.grpc.grpcclient.fragments.items.ItemsViewModel;
import com.grpc.grpcclient.tables.Briefcase;
import com.grpc.grpcclient.tables.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import gprimo.grpc.userauth.AuthorizationGrpc;
import gprimo.grpc.userauth.LoginRequest;
import gprimo.grpc.userauth.LogoutRequest;
import gprimo.grpc.userauth.Response;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.grpcclient.R;

public class HomeFragment extends Fragment {

    private AuthViewModel mViewModel;
    private Toolbar toolbar;
    private FloatingActionButton add_fab;
    private RecyclerView briefcases_rv;
    private List<Briefcase> briefcases;
    private BriefcasesRVA adapter;
    private HomeViewModel homeViewModel;
    private OnTaskCompleted onTaskCompleted;
    public interface OnTaskCompleted{
        void onTaskCompleted(String message);
    }


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        mViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (toolbar!=null) {
                if (user != null) {
                    toolbar.setTitle(new StringBuilder(user.getFirstName()).append(" ").append(user.getLastName()));
                } else {
                    toolbar.setTitle(getResources().getString(R.string.loading));
                }
            }
        });
        homeViewModel=new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        User user=new User();
        if(((MainActivity)requireActivity()).dataBase.mainDao()
                .getUser(mViewModel.getUser().getValue().getUserId())==null){
            user.setID(mViewModel.getUser().getValue().getUserId());
            user.setEmail(mViewModel.getUser().getValue().getEmail());
            user.setFName(mViewModel.getUser().getValue().getFirstName());
            user.setLName(mViewModel.getUser().getValue().getLastName());
            user.setPhone(mViewModel.getUser().getValue().getPhoneNumber());
            ((MainActivity)requireActivity()).dataBase.mainDao().createUser(user);
        }else {
            user=((MainActivity)requireActivity()).dataBase.mainDao()
                    .getUser(mViewModel.getUser().getValue().getUserId());
        }
        briefcases=((MainActivity)requireActivity()).dataBase.mainDao().getAllBriefcases(user.getID());
        adapter=new BriefcasesRVA((MainActivity)requireActivity(), briefcases);
        adapter.setOnItemClickListener(briefcase -> {
            ItemsViewModel itemsViewModel=new ViewModelProvider(requireActivity()).get(ItemsViewModel.class);
            itemsViewModel.setBriefcase(briefcase);
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                    .navigate(R.id.action_homeFragment_to_itemsFragment);
        });
        homeViewModel.getBriefcases(user.getID(), ((MainActivity)requireActivity()).dataBase)
                .observe(getViewLifecycleOwner(), briefcases_ -> {
            briefcases=briefcases_;
            if (adapter!=null){
                adapter.setBriefcases(briefcases);
            }
        });
        return inflater.inflate(R.layout.home_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findIDs(view);
        setInfo();
        setActions();
        ((MainActivity) requireActivity()).progressDialog.dismiss();
    }

    private void findIDs(@NotNull View view) {
        toolbar=view.findViewById(R.id.home_toolbar);
        add_fab=view.findViewById(R.id.add_briefc_fab);
        briefcases_rv=view.findViewById(R.id.briefcases_rv);
    }

    private void setInfo(){
        toolbar.setTitle(new StringBuilder(mViewModel.getUser().getValue().getFirstName()).append(" ")
                .append(mViewModel.getUser().getValue().getLastName()));
        briefcases_rv.setLayoutManager(new LinearLayoutManager(getContext(),  RecyclerView.VERTICAL,
                false));
        briefcases_rv.setAdapter(adapter);
        toolbar.inflateMenu(R.menu.home_menu);

    }
    private void setActions(){
        add_fab.setOnClickListener(v->{
            AddBriefcase dialog=new AddBriefcase();
            if(requireActivity().getSupportFragmentManager()!=null){
                dialog.setOnDismissClickListener(briefcase -> {
                    ((MainActivity)requireActivity()).dataBase.mainDao().createBriefcase(briefcase);
                    briefcases=homeViewModel.getBriefcases(mViewModel.getUser().getValue().getUserId(),
                            ((MainActivity)requireActivity()).dataBase).getValue();
                    homeViewModel.setBriefcases(briefcases);
                });
                dialog.show(requireActivity().getSupportFragmentManager(), "addBr");
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onTaskCompleted= message -> {
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                    mViewModel.setUser(null);
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                            .navigate(R.id.action_homeFragment_to_authFragment);
                };
                new GRPCExitTask(onTaskCompleted).execute(mViewModel.getUser().getValue().getUserId());
                return false;
            }
        });
    }

    private static class GRPCExitTask extends AsyncTask<Integer, Void, Response>{
        private final OnTaskCompleted onTaskCompleted;
       public GRPCExitTask(OnTaskCompleted onTaskCompleted) {
            this.onTaskCompleted=onTaskCompleted;
        }

        @Override
        protected Response doInBackground(Integer... integers) {
            ManagedChannel channel = ManagedChannelBuilder.forAddress("10.0.2.2", 80).usePlaintext().build();
            AuthorizationGrpc.AuthorizationBlockingStub stub=AuthorizationGrpc.newBlockingStub(channel);
            LogoutRequest request=LogoutRequest.newBuilder().setUserId(integers[0]).build();
            return stub.logOut(request);
        }

        @Override
        protected void onPostExecute(Response response) {
           if (response.getCode()==1) {
               onTaskCompleted.onTaskCompleted(response.getMessage());
           }
        }
    }
}