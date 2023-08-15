package be.ehb.garbapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import be.ehb.garbapp.Adapter.GarbUserAdapter;
import be.ehb.garbapp.Adapter.ReportAdapter;
import be.ehb.garbapp.GarbUser;
import be.ehb.garbapp.R;
import be.ehb.garbapp.Report;
import be.ehb.garbapp.ViewModel.GarbUserViewModel;
import be.ehb.garbapp.ViewModel.ReportViewModel;
import be.ehb.garbapp.databinding.FragmentAdminBinding;
import be.ehb.garbapp.databinding.FragmentDashboardBinding;
import be.ehb.garbapp.databinding.FragmentRankingBinding;


public class RankingFragment extends Fragment {

    private FragmentRankingBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(

            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentRankingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        GarbUserViewModel garbUserViewModel = new ViewModelProvider(getActivity()).get(GarbUserViewModel.class);
        GarbUserAdapter garbUserAdapter = new GarbUserAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rvGarbUserList.setAdapter(garbUserAdapter);
        binding.rvGarbUserList.setLayoutManager(layoutManager);
        garbUserViewModel.getAllGarbUsers().observe(getViewLifecycleOwner(), new Observer<List<GarbUser>>() {
            @Override
            public void onChanged(List<GarbUser> garbUserList) {
                garbUserAdapter.addItems(garbUserList);
            }
        });

        return root;
    }
}