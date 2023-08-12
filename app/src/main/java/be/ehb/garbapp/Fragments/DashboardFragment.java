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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import be.ehb.garbapp.Adapter.ReportAdapter;
import be.ehb.garbapp.R;
import be.ehb.garbapp.Report;
import be.ehb.garbapp.ViewModel.ReportViewModel;
import be.ehb.garbapp.databinding.FragmentDashboardBinding;
import be.ehb.garbapp.databinding.FragmentProfileBinding;


public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(

            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ReportViewModel reportViewModel = new ViewModelProvider(getActivity()).get(ReportViewModel.class);
        ReportAdapter reportAdapter = new ReportAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rvRepostList.setAdapter(reportAdapter);
        binding.rvRepostList.setLayoutManager(layoutManager);
        reportViewModel.getAllReports().observe(getViewLifecycleOwner(), new Observer<List<Report>>() {
            @Override
            public void onChanged(List<Report> reportList) {
                reportAdapter.addItems(reportList);
            }
        });
        return root;
    }
}