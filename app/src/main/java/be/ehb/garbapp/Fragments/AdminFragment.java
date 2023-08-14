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

import be.ehb.garbapp.Adapter.ReportAdapter;
import be.ehb.garbapp.R;
import be.ehb.garbapp.Report;
import be.ehb.garbapp.ViewModel.ReportViewModel;
import be.ehb.garbapp.databinding.FragmentAdminBinding;
import be.ehb.garbapp.databinding.FragmentDashboardBinding;
import be.ehb.garbapp.databinding.FragmentProfileBinding;


public class AdminFragment extends Fragment implements ReportAdapter.OnItemClickListener{

    private FragmentAdminBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onItemClick(Report report) {
        Bundle bundle = new Bundle();
        bundle.putString("reportId", String.valueOf(report.getPostId()));
        RateReportFragment fragment = new RateReportFragment();
        fragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_admin, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public View onCreateView(

            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentAdminBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle bundle = getArguments();
        if (bundle != null) {
            String reportIdString = bundle.getString("reportId");
            if (reportIdString != null) {
                int reportId = Integer.parseInt(reportIdString);
            }
        }

        ReportViewModel reportViewModel = new ViewModelProvider(getActivity()).get(ReportViewModel.class);
        ReportAdapter reportAdapter = new ReportAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rvRepostList.setAdapter(reportAdapter);
        binding.rvRepostList.setLayoutManager(layoutManager);
        reportViewModel.getUnapprovedReports().observe(getViewLifecycleOwner(), new Observer<List<Report>>() {
            @Override
            public void onChanged(List<Report> reportList) {
                reportAdapter.addItems(reportList);
            }
        });

        reportAdapter.setOnItemClickListener(this);

        return root;
    }


}