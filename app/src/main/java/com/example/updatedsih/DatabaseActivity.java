package com.example.updatedsih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class DatabaseActivity extends AppCompatActivity {
    EditText scheme,state,district,village,assets,no_of_assets,holderUserid,capital_invested,latitude,longitude,farmersassociated,milkproduction;
    Button submit,findgc;
    FirebaseFirestore db;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        db=FirebaseFirestore.getInstance();

        scheme=findViewById(R.id.scheme);
        state=findViewById(R.id.state);
        district=findViewById(R.id.district);
        village=findViewById(R.id.village);
        assets=findViewById(R.id.assets);
        no_of_assets=findViewById(R.id.noofassets);


        holderUserid=findViewById(R.id.holderUserid);
        capital_invested=findViewById(R.id.capitalinvested);
        longitude=findViewById(R.id.longitude);
        latitude=findViewById(R.id.latitude);
        submit=findViewById(R.id.submit);
        findgc=findViewById(R.id.findgc);

        findgc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentLocation();

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //////////////////////////////////
                String Scheme=scheme.getText().toString();
                String State=state.getText().toString();
                String District=district.getText().toString();
                String Village=village.getText().toString();
                String Assets=assets.getText().toString();
                int No_of_assets = Integer.parseInt(no_of_assets.getText().toString());
                String HolderUserid=holderUserid.getText().toString();
                double Capital_invested= Double.parseDouble(capital_invested.getText().toString());
                double Longitude= Double.parseDouble(longitude.getText().toString());
                double Latitude= Double.parseDouble(latitude.getText().toString());
                Map<String,Object> Add=new HashMap<>();
                Add.put("Scheme",Scheme);
                Add.put("State",State);
                Add.put("District",District);
                Add.put("Village",Village);
                Add.put("Assets",Assets);
                Add.put("No_of_assets",No_of_assets);
                Add.put("HolderUserid",HolderUserid);
                Add.put("Capital_invested",Capital_invested);
                Add.put("Longitude",Longitude);
                Add.put("Latitude",Latitude);
                Map<String,Object> Add1=new HashMap<>();
                Add1.put("No_of_assets",No_of_assets);
                Add1.put("Total_capital",Capital_invested);



                db.collection("ASSETS").add(Add)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                DocumentReference DB = db.collection("SCHEME").document(Scheme).collection("STATES").document(State)
                                        .collection("DISTRICT").document(District).collection("VILLAGE").document(Village);

                                //.document("assetsdoc");// if you use add function than a new document will be created instead
                                //we will retrive the existing data of village


                                DB.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {

                                                double existingassets =  document.getDouble("No_of_assets");
                                                double existingcapital =  document.getDouble("Total_capital");

                                                Map<String,Object> newdata=new HashMap<>();
                                                newdata.put("No_of_assets",No_of_assets + existingassets);
                                                newdata.put("Total_capital",Capital_invested + existingcapital);

//                                                db.collection("SCHEME").document(Scheme).collection("STATES").document(State)
//                                                        .collection("DISTRICT").document(District).collection("VILLAGE").document(Village)
//                                                        .update(newdata);

                                                DB.set(newdata, SetOptions.merge());
                                            }
                                            else{
                                                Map<String,Object> olddata=new HashMap<>();
                                                olddata.put("No_of_assets",No_of_assets );
                                                olddata.put("Total_capital",Capital_invested );
                                                DB.set(olddata);
                                            }
                                        }else{
                                            Toast.makeText(DatabaseActivity.this, "task not succesfull", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DatabaseActivity.this, "failed to add data", Toast.LENGTH_SHORT).show();
                            }
                        });
                Toast.makeText(DatabaseActivity.this, "Done", Toast.LENGTH_SHORT).show();


/////////////////////////////////////


            }
        });





    }

    private void getCurrentLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(DatabaseActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {


                    LocationServices.getFusedLocationProviderClient(DatabaseActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(DatabaseActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        double latitude1 = locationResult.getLocations().get(index).getLatitude();
                                        double longitude1 = locationResult.getLocations().get(index).getLongitude();
                                        latitude.setText(String.valueOf(latitude1));
                                        longitude.setText(String.valueOf(longitude1));
                                        /////////////////////send to maps////////////////////////////
                                        String query="geo:"+latitude+","+longitude;
                                        System.out.println(query);

//                                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                                        intent.setData(Uri.parse(query));
//                                        Intent chooser = Intent.createChooser(intent,"Launch Maps");
//
//                                        AddressText.setText("Latitude: "+ latitude + "\n" + "Longitude: "+ longitude);
//                                        startActivity(chooser);
////                                        System.out.println(intent.setData((Uri.parse("geo:{latitude},{longitude}")));
                                        ////////////////////////////////////////
                                    }
                                }
                            }, Looper.getMainLooper());

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }


    private void turnOnGPS() {



        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(DatabaseActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(DatabaseActivity.this, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }

}

