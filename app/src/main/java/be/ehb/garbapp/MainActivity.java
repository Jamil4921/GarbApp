package be.ehb.garbapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import android.os.Bundle;
import android.view.MenuItem;

import be.ehb.garbapp.Fragments.DashboardFragment;
import be.ehb.garbapp.Fragments.MapFragment;
import be.ehb.garbapp.Fragments.ProfileFragment;
import be.ehb.garbapp.Fragments.RankingFragment;
import be.ehb.garbapp.Fragments.ReportFragment;
import be.ehb.garbapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new MapFragment()).commit();
        BottomNavigationView nav_bottom = findViewById(R.id.BottomNavigationView);
        nav_bottom.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new ProfileFragment()).commit();
                        return true;

                    case R.id.Ranking:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new RankingFragment()).commit();
                        return true;

                    case R.id.Repost:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new ReportFragment()).commit();
                        return true;

                    case R.id.Dashboard:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new DashboardFragment()).commit();
                        return true;

                    case R.id.Home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new MapFragment()).commit();
                        return true;
                }
                return false;
            }
        });
    }

}