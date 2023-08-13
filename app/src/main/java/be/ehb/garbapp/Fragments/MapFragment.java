package be.ehb.garbapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import be.ehb.garbapp.Database.Database;
import be.ehb.garbapp.R;
import be.ehb.garbapp.Report;
import be.ehb.garbapp.databinding.FragmentMapBinding;


public class MapFragment extends Fragment {

    private SupportMapFragment supportMapFragment;
    private FragmentMapBinding binding;
    private Database database = new Database();

    @Override
    public View onCreateView(

            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.mapView.onCreate(savedInstanceState);
        supportMapFragment = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.mapView, supportMapFragment).commit();
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                database.getAllReports().observe(getViewLifecycleOwner(), new Observer<List<Report>>() {
                    @Override
                    public void onChanged(List<Report> reports) {
                        for(Report report : reports){
                            googleMap.addMarker(new MarkerOptions().position(new LatLng(report.getLati(), report.getLongti())).title("Spotted trash by user : " + report.getRepostUserName()));
                        }
                        //googleMap.addMarker(new MarkerOptions().position(new LatLng(pokemon.getLati(), pokemon.getLongti())).title("Pokemon spotting : " + pokemon.getPokemon()).icon(pokeBall(getActivity(), R.drawable.ic_baseline_catching_pokemon_24)));

                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.mapView.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.mapView.onDestroy();
        binding = null;
    }
}
