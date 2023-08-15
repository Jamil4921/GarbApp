package be.ehb.garbapp.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import be.ehb.garbapp.GarbUser;
import be.ehb.garbapp.Report;

public class GarbUserViewModel extends AndroidViewModel {
    DatabaseReference databaseReference;
    private MutableLiveData<List<GarbUser>> allGarbUsers;

    public GarbUserViewModel(@NonNull Application application) {
        super(application);
        allGarbUsers = new MutableLiveData<>();
        databaseReference = FirebaseDatabase.getInstance("https://garbapp-ab823-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Registered User");
    }



    public MutableLiveData<List<GarbUser>> getAllGarbUsers() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<GarbUser> gardUsersList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    GarbUser garbUser = postSnapshot.getValue(GarbUser.class);
                    gardUsersList.add(garbUser);

                }
                allGarbUsers.setValue(gardUsersList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        System.out.println(allGarbUsers.toString());
        return allGarbUsers;
    }

  


}
