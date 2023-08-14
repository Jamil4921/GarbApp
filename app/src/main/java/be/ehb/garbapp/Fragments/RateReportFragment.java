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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import be.ehb.garbapp.Adapter.ReportAdapter;
import be.ehb.garbapp.R;
import be.ehb.garbapp.Report;
import be.ehb.garbapp.ViewModel.ReportViewModel;
import be.ehb.garbapp.databinding.FragmentAdminBinding;
import be.ehb.garbapp.databinding.FragmentRateReportBinding;
import be.ehb.garbapp.databinding.FragmentReportBinding;

public class RateReportFragment extends Fragment {
    private TextView reportTitleTextView, reportPostIdtextView;
    private ImageView repostImageView;
    private FragmentRateReportBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(

            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentRateReportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        reportTitleTextView = root.findViewById(R.id.rate_title);
        reportPostIdtextView = root.findViewById(R.id.rate_postid);
        repostImageView = root.findViewById(R.id.rate_imageView);
        reportTitleTextView.setText("Loading...");

        Bundle bundle = getArguments();
        if (bundle != null) {
            String reportIdString = bundle.getString("reportId");
            if (reportIdString != null) {
                int reportId = Integer.parseInt(reportIdString);

                Report report = getReportById(reportId);

                if (report != null) {
                    reportTitleTextView.setText(report.getTitle());
                    reportPostIdtextView.setText(String.valueOf(report.getPostId()));

                    if (report.getPictureUrl() != null && !report.getPictureUrl().isEmpty()) {
                        Glide.with(requireContext())
                                .load(report.getPictureUrl())
                                .into(repostImageView);
                    } else {
                        repostImageView.setVisibility(View.GONE); // Hide the ImageView
                    }
                } else {
                    reportTitleTextView.setText("Report not found");
                    reportPostIdtextView.setText("");
                    repostImageView.setVisibility(View.GONE);
                }

            }

        }



        return root;
    }



    private Report getReportById(int reportId) {
        ReportViewModel reportViewModel = new ViewModelProvider(requireActivity()).get(ReportViewModel.class);

        List<Report> reportList = reportViewModel.getAllReports().getValue();
        if (reportList != null) {
            for (Report report : reportList) {
                if (report.getPostId() == reportId) {
                    return report;
                }
            }
        }

        return null;
    }
}