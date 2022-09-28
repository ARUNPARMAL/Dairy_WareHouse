package com.example.updatedsih;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserList extends AppCompatActivity {
    FirebaseFirestore firestore;
    List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
    MapView mapView_;
    private MapboxMap mapboxMap;
    TextView schemeName,stateName,districtName,villageName,assetCount,assetAmount,Milkproduced,farmerscount;
    CameraPosition cameraPosition;
    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";

    ArrayList<Map> userlist=new ArrayList<>();
    UserListAdapter userListAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_user_list);
        recyclerView=findViewById(R.id.recyclerview_userlist);
        firestore=FirebaseFirestore.getInstance();
//        cameraPosition=mapboxMap.getCameraPosition();
        // initialization of variables
        mapView_=findViewById(R.id.mapView_);
        schemeName=findViewById(R.id.schemeName);
        stateName=findViewById(R.id.stateName);
        districtName=findViewById(R.id.districtName);
        villageName=findViewById(R.id.villageName);
        assetCount=findViewById(R.id.assetCount);
        assetAmount=findViewById(R.id.assetAmount);
        Milkproduced=findViewById(R.id.Milkproduced);
        farmerscount=findViewById(R.id.farmerscount);
        Bundle extras = getIntent().getExtras();
        if (extras!=null){
            String schemename= extras.getString("scheme_name");
            String statename= extras.getString("state_name");
            String districtname= extras.getString("district_name");
            String villagename= extras.getString("village_name");
            String pathstate="DESCRIPTION/"+schemename+"/states/"+statename+"/districts/"+districtname+"/villages";


            schemeName.setText(schemename);
            stateName.setText(statename);
            districtName.setText(districtname);
            villageName.setText(villagename);



            recyclerView.setLayoutManager(new LinearLayoutManager(UserList.this));
            userListAdapter = new UserListAdapter(userlist);
            recyclerView.setAdapter(userListAdapter);

           firestore.collection(pathstate).document(String.valueOf(villagename)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
               @Override
               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                   assetCount.setText(task.getResult().get("assetcount").toString());
                   assetAmount.setText(task.getResult().get("totalamount").toString());

               }
           });


            firestore.collection("ASSETS").whereEqualTo("Village",villagename).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        double farmercount=0;
                        double milkproduced=0;
                        for (QueryDocumentSnapshot documentSnapshot: task.getResult()){


                               farmercount=farmercount+documentSnapshot.getDouble("Farmers");
                               milkproduced=milkproduced+documentSnapshot.getDouble("Milk");
                            double longitude=documentSnapshot.getDouble("Longitude");
                            double lattitude=documentSnapshot.getDouble("Latitude");
                            userlist.add(documentSnapshot.getData());
                            symbolLayerIconFeatureList.add(Feature.fromGeometry(
                                    Point.fromLngLat(longitude,lattitude)));
//

                        }
                        userListAdapter.notifyDataSetChanged();

                        farmerscount.setText(String.valueOf(farmercount));
                        Milkproduced.setText(milkproduced+"Litre");

                        mapView_.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(@NonNull final MapboxMap mapboxMap) {

                                mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")

                                        // Add the SymbolLayer icon image to the map style
                                        .withImage(ICON_ID, BitmapFactory.decodeResource(UserList.this.getResources(), R.drawable.red_marker))

                                        // Adding a GeoJson source for the SymbolLayer icons.
                                        .withSource(new GeoJsonSource(SOURCE_ID,
                                                FeatureCollection.fromFeatures(symbolLayerIconFeatureList)))


                                        .withLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                                                .withProperties(
                                                        iconImage(ICON_ID),
                                                        iconAllowOverlap(true),//value is true
                                                        iconIgnorePlacement(true)//value is true
                                                )
                                        ),
                                        new Style.OnStyleLoaded() {
                                    @Override
                                    public void onStyleLoaded(@NonNull Style style) {


                                    }
                                }
                                );

                            }
                        });
                    }


                }
            });


        }

    }
}