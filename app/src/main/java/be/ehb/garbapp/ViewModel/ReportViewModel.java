package be.ehb.garbapp.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import be.ehb.garbapp.Report;

public class ReportViewModel extends AndroidViewModel {

    DatabaseReference databaseReference;
    private MutableLiveData<List<Report>> allReports;


    public ReportViewModel(@NonNull Application application) {
        super(application);
        allReports = new MutableLiveData<>();
        databaseReference = FirebaseDatabase.getInstance("https://garbapp-ab823-default-rtdb.europe-west1.firebasedatabase.app/").getReference("reports");
    }

    public MutableLiveData<List<Report>> getAllReports() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Report> reportList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Report report = postSnapshot.getValue(Report.class);
                    reportList.add(report);

                }
                allReports.setValue(reportList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        System.out.println(allReports.toString());
        return allReports;
    }

    public MutableLiveData<List<Report>> getReportsByUser(String userUid) {
        MutableLiveData<List<Report>> filteredReports = new MutableLiveData<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Report> reportList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Report report = postSnapshot.getValue(Report.class);
                    if (report.getUserUid().equals(userUid)) {
                        reportList.add(report);
                    }
                }
                filteredReports.setValue(reportList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return filteredReports;
    }

    public MutableLiveData<List<Report>> getUnapprovedReports() {
        MutableLiveData<List<Report>> unapprovedReports = new MutableLiveData<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Report> reportList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Report report = postSnapshot.getValue(Report.class);
                    if (!report.isApproved()) {
                        reportList.add(report);
                    }
                }
                unapprovedReports.setValue(reportList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        return unapprovedReports;
    }

}
