package be.ehb.garbapp.Fragments;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import be.ehb.garbapp.MainActivity;
import be.ehb.garbapp.R;
import be.ehb.garbapp.Report;
import be.ehb.garbapp.databinding.FragmentProfileBinding;
import be.ehb.garbapp.databinding.FragmentReportBinding;


public class ReportFragment extends Fragment {


    private FragmentReportBinding binding;
    private EditText editText_Title, editText_description;
    private Button add_picture, report_trash;
    private DatabaseReference databaseReference;
    private FirebaseAuth authProfile;
    private ImageView imageView_report_trash_preview;
    private String pictureUrl;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private final static int REQUEST_CODE = 100;
    private double latitude, longitude;
    private String address;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        editText_Title = root.findViewById(R.id.et_post_title);
        editText_description = root.findViewById(R.id.et_description);
        imageView_report_trash_preview = root.findViewById(R.id.imageView_report_trash_preview);
        add_picture = root.findViewById(R.id.report_button_picture);
        report_trash = root.findViewById(R.id.repost_trash);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

        report_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertTrash();
            }
        });

        add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        return root;
    }

    private void insertTrash() {
        try {

            String trashTitle = editText_Title.getText().toString().trim();
            String trashDescription = editText_description.getText().toString().trim();

            if (trashTitle.isEmpty()) {
                editText_Title.setError("Please add a title");
                editText_Title.requestFocus();
                return;
            }

            if (trashDescription.length() < 9) {
                editText_description.setError("Description should be 9 char long ");
                editText_description.requestFocus();
                return;
            }

            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }

            if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                    if(location != null){
                        askPermission();
                        Log.d("Location", "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                            address = addresses.get(0).getAddressLine(0).toString();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        String userUiD = currentUser.getUid();
                        String reportUserName = currentUser.getDisplayName();
                        int postId = generatePostId();
                        boolean approved = false;
                        Date createdPost = Calendar.getInstance().getTime();
                        Report report = new Report(postId, trashTitle, trashDescription, userUiD, reportUserName, approved, createdPost, pictureUrl, location.getLatitude(), location.getLongitude(), address);
                        Log.d("ReportFragment", "Report : " + trashTitle);
                        databaseReference = FirebaseDatabase.getInstance("https://garbapp-ab823-default-rtdb.europe-west1.firebasedatabase.app/").getReference("reports");
                        databaseReference.child(String.valueOf(postId)).setValue(report).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), "Successfully reposted trash. thank you for contributing. Points will be received once approved by admin", Toast.LENGTH_LONG).show();
                                    showMainActivity();
                                }else {
                                    Toast.makeText(getContext(), "Something went wrong please try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                    }
                });
            }



        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void uploadImageToFirebase(Bitmap bitmap) {
        String filename = "image_" + Calendar.getInstance().getTimeInMillis() + ".jpg";

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference("images/" + filename);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    pictureUrl = downloadUri.toString();
                    Toast.makeText(getContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(requireContext(), "Camera permission denied.", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                return;
            }else{
                Toast.makeText(getContext(),"Request Failed", Toast.LENGTH_LONG).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(requireActivity(),new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            getLocation();
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView_report_trash_preview.setImageBitmap(imageBitmap);

            uploadImageToFirebase(imageBitmap);
        }
    }

    private int generatePostId() {
        return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
    }

    private void showMainActivity(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);

    }


}