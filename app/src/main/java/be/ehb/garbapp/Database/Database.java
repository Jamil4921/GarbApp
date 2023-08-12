package be.ehb.garbapp.Database;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import be.ehb.garbapp.Report;

public class Database {
    private MutableLiveData<List<Report>> allReports = new MutableLiveData<>();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://garbapp-ab823-default-rtdb.europe-west1.firebasedatabase.app/").getReference("reports");

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
        return allReports;
    }
}
