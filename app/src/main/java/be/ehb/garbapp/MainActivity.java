package be.ehb.garbapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import be.ehb.garbapp.Fragments.DashboardFragment;
import be.ehb.garbapp.Fragments.MapFragment;
import be.ehb.garbapp.Fragments.ProfileFragment;
import be.ehb.garbapp.Fragments.RankingFragment;
import be.ehb.garbapp.Fragments.ReportFragment;
import com.bumptech.glide.Glide;


public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Profile_hamburger:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new ProfileFragment()).commit();
                        return true;

                    case R.id.Ranking_hamburger:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new RankingFragment()).commit();
                        return true;

                    case R.id.Repost_hamburger:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new ReportFragment()).commit();
                        return true;

                    case R.id.Dashboard_hamburger:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new DashboardFragment()).commit();
                        return true;

                    case R.id.Home_hamburger:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new MapFragment()).commit();
                        return true;
                }
                return false;
            }
        });



    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else
        {
            super.onBackPressed();
        }
    }


}