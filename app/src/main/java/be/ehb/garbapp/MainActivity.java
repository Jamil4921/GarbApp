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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import be.ehb.garbapp.Fragments.AdminFragment;
import be.ehb.garbapp.Fragments.DashboardFragment;
import be.ehb.garbapp.Fragments.MapFragment;
import be.ehb.garbapp.Fragments.ProfileFragment;
import be.ehb.garbapp.Fragments.RankingFragment;
import be.ehb.garbapp.Fragments.ReportFragment;
import com.bumptech.glide.Glide;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    FirebaseUser firebaseUser;
    private FirebaseAuth authProfile;



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchUserRoleAndInitializeUI() {
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser currentUser = authProfile.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference userReference = FirebaseDatabase.getInstance("https://garbapp-ab823-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Registered User")
                    .child(currentUser.getUid());
            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        GarbUser user = dataSnapshot.getValue(GarbUser.class);
                        int userRole = user.getRole();
                        updateAdminMenuItemVisibility(userRole);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    private void updateAdminMenuItemVisibility(int userRole) {
        Menu navMenu = navigationView.getMenu();
        MenuItem adminMenuItem = navMenu.findItem(R.id.Admin_hamburger);

        if (userRole == 1) {
            adminMenuItem.setVisible(true);
        } else {
            adminMenuItem.setVisible(false);
        }
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


        fetchUserRoleAndInitializeUI();
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

                    case R.id.Admin_hamburger:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new AdminFragment()).commit();
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