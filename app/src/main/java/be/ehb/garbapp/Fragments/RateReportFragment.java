package be.ehb.garbapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import be.ehb.garbapp.Adapter.ReportAdapter;
import be.ehb.garbapp.GarbUser;
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
    private Button reportButtonRemove, reportButtonApprove;
    private DatabaseReference databaseReference;
    private RadioButton radioButtonSmall, radioButtonMedium,radioButtonLarge;
    private double garbPoints = 0.0;

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

        reportButtonRemove = root.findViewById(R.id.rate_btn_remove);
        reportButtonApprove = root.findViewById(R.id.rate_btn_approve);
        reportTitleTextView = root.findViewById(R.id.rate_title);
        reportPostIdtextView = root.findViewById(R.id.rate_postid);
        repostImageView = root.findViewById(R.id.rate_imageView);
        radioButtonSmall = root.findViewById(R.id.radioSmallTrash);
        radioButtonMedium = root.findViewById(R.id.radioMediumTrash);
        radioButtonLarge = root.findViewById(R.id.radioLargeTrash);
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


                    radioButtonSmall.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (radioButtonSmall.isChecked()) {
                                garbPoints = 5.0;
                                Log.d("RateReportFragment", "Small: " + radioButtonSmall.isChecked() + " / " + garbPoints);
                            }
                        }
                    });

                    radioButtonMedium.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (radioButtonMedium.isChecked()) {
                                garbPoints = 10.0;
                                Log.d("RateReportFragment", "Medium: " + radioButtonMedium.isChecked() + " / " + garbPoints);
                            }
                        }
                    });

                    radioButtonLarge.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (radioButtonLarge.isChecked()) {
                                garbPoints = 25.0;
                                Log.d("RateReportFragment", "Large: " + radioButtonLarge.isChecked() + " / " + garbPoints);
                            }
                        }
                    });


                    reportButtonRemove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteReport(report);
                        }
                    });

                    reportButtonApprove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            approveReport(report);
                        }
                    });
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

    public void deleteReport(Report report) {
        databaseReference = FirebaseDatabase.getInstance("https://garbapp-ab823-default-rtdb.europe-west1.firebasedatabase.app/").getReference("reports");
        DatabaseReference reportRef = databaseReference.child(String.valueOf(report.getPostId()));
        reportRef.removeValue();
        Toast.makeText(getContext(), "Successfully removed report ", Toast.LENGTH_LONG).show();
        showAdminFragment();

    }

    public void approveReport(Report report) {
        databaseReference = FirebaseDatabase.getInstance("https://garbapp-ab823-default-rtdb.europe-west1.firebasedatabase.app/").getReference("reports");
        DatabaseReference reportRef = databaseReference.child(String.valueOf(report.getPostId()));
        report.setApproved(true);
        report.setGarbPoints(garbPoints);

        reportRef.setValue(report);

        updateUserTotalPoints(report.getUserUid()); // Move this line up

        Toast.makeText(getContext(), "Successfully approved report ", Toast.LENGTH_LONG).show();
    }

    private void updateUserTotalPoints(String userUid) {
        DatabaseReference userRef = FirebaseDatabase.getInstance("https://garbapp-ab823-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Registered User").child(userUid);
        DatabaseReference reportsRef = FirebaseDatabase.getInstance("https://garbapp-ab823-default-rtdb.europe-west1.firebasedatabase.app/").getReference("reports");

        reportsRef.orderByChild("userUid").equalTo(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalPoints = 0.0;

                for (DataSnapshot reportSnapshot : dataSnapshot.getChildren()) {
                    Report report = reportSnapshot.getValue(Report.class);
                    if (report != null) {
                        totalPoints += report.getGarbPoints();
                    }
                }

                userRef.child("totalPoints").setValue(totalPoints);
                showAdminFragment();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("RateReportFragment", "Error fetching reports: " + databaseError.getMessage());
            }
        });
    }



    public void showAdminFragment(){
        AdminFragment adminFragment = new AdminFragment();
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_rate_report, adminFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}