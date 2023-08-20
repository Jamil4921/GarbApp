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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import be.ehb.garbapp.Adapter.ReportAdapter;
import be.ehb.garbapp.R;
import be.ehb.garbapp.Report;
import be.ehb.garbapp.ViewModel.ReportViewModel;
import be.ehb.garbapp.databinding.FragmentAchievementBinding;
import be.ehb.garbapp.databinding.FragmentProfileBinding;


public class AchievementFragment extends Fragment {

    private FragmentAchievementBinding binding;
    private TextView text_Report_5_trash, text_Report_10_trash, text_Report_15_trash, text_report_points5_10, text_report_points15_35, text_report_points40_75;
    private ReportViewModel reportViewModel;
    private int reportCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(

            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentAchievementBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        text_Report_5_trash = root.findViewById(R.id.Report_5_trash);
        text_Report_10_trash = root.findViewById(R.id.Report_10_trash);
        text_Report_15_trash = root.findViewById(R.id.Report_15_trash);

        text_report_points5_10 = root.findViewById(R.id.report_points5_10);
        text_report_points15_35 = root.findViewById(R.id.report_points15_35);
        text_report_points40_75 = root.findViewById(R.id.report_points40_75);

        reportViewModel = new ViewModelProvider(requireActivity()).get(ReportViewModel.class);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userUid = currentUser.getUid();

            reportViewModel.getUserTotalPoints(userUid).observe(getViewLifecycleOwner(), new Observer<Integer>() {
                @Override
                public void onChanged(Integer totalPoints) {
                    updateTextViewColors(reportCount, totalPoints);
                }
            });
        }


        return root;
    }

    private void updateTextViewColors(int reportCount, int totalPoints) {
        if (reportCount >= 5) {
            text_Report_5_trash.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }
        if (reportCount >= 10) {
            text_Report_10_trash.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }
        if (reportCount >= 15) {
            text_Report_15_trash.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }

        if (totalPoints >= 5 && totalPoints <= 10) {
            text_report_points5_10.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }
        if (totalPoints >= 15 && totalPoints <= 35) {
            text_report_points15_35.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }
        if (totalPoints >= 40 && totalPoints <= 75) {
            text_report_points40_75.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }
    }
}