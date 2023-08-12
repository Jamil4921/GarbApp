package be.ehb.garbapp.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import be.ehb.garbapp.Adapter.ReportAdapter;
import be.ehb.garbapp.GarbUser;
import be.ehb.garbapp.R;
import be.ehb.garbapp.Report;
import be.ehb.garbapp.ViewModel.ReportViewModel;
import be.ehb.garbapp.databinding.FragmentMapBinding;
import be.ehb.garbapp.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {

    private TextView TvprofileName;
    private String name;
    private ImageView profile_image;
    private FirebaseAuth authProfile;
    private FragmentProfileBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(

            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TvprofileName = root.findViewById(R.id.profile_name);
        profile_image = root.findViewById(R.id.imageView_profile);
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        showUserProfile(firebaseUser);

        ReportViewModel reportViewModel = new ViewModelProvider(getActivity()).get(ReportViewModel.class);
        ReportAdapter reportAdapter = new ReportAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        binding.rvRepostList.setAdapter(reportAdapter);
        binding.rvRepostList.setLayoutManager(layoutManager);
        reportViewModel.getReportsByUser(firebaseUser.getUid()).observe(getViewLifecycleOwner(), new Observer<List<Report>>() {
            @Override
            public void onChanged(List<Report> reportList) {
                reportAdapter.addItems(reportList);
            }
        });


        return root;
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userid = firebaseUser.getUid();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance("https://garbapp-ab823-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Registered User");
        referenceProfile.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GarbUser garbUser = snapshot.getValue(GarbUser.class);
                if (garbUser != null) {

                    name = firebaseUser.getDisplayName();
                    Log.d("ProfileFragment", "User's first name: " + name);

                    TvprofileName.setText(name);

                    // Load and display the profile picture using Picasso
                    String profilePictureUrl = garbUser.getProfilePictureUrl();
                    if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                        Picasso.get().load(profilePictureUrl).into(profile_image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Please try again", Toast.LENGTH_LONG).show();
            }
        });
    }
}