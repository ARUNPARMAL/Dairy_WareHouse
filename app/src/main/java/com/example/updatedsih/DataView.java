package com.example.updatedsih;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
import com.mapbox.mapboxsdk.maps.MapboxMapOptions;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

public class DataView extends AppCompatActivity {
    MapView mapView;
    private MapboxMap mapboxMap;
    private MarkerView markerView;
    private MarkerViewManager markerViewManager;
    FirebaseFirestore firestore;
    List<Feature> symbolLayerIconFeatureList = new ArrayList<>();

    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_data_view);
        firestore= FirebaseFirestore.getInstance();
        mapView=findViewById(R.id.mapView1);
        mapView.onCreate(savedInstanceState);
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        Bundle extras =getIntent().getExtras();
        if (extras!=null){
            int code;
            code=extras.getInt("code");
            switch (code){
                case 1:
                    String schemen=extras.getString("para_scheme");
                    firestore.collection("ASSETS").whereEqualTo("Scheme",schemen).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                    System.out.println(documentSnapshot.getData());

                                    String assetname=documentSnapshot.getString("Assets");

                                    double longitude=documentSnapshot.getDouble("Longitude");
                                    double lattitude=documentSnapshot.getDouble("Latitude");



                                    symbolLayerIconFeatureList.add(Feature.fromGeometry(
                                            Point.fromLngLat(longitude,lattitude)));
                                    System.out.println("size of the array list is "+symbolLayerIconFeatureList.size());


                                }
                                mapView.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(@NonNull final MapboxMap mapboxMap) {

                                        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")

                                                // Add the SymbolLayer icon image to the map style
                                                .withImage(ICON_ID, BitmapFactory.decodeResource(DataView.this.getResources(), R.drawable.red_marker))

                                                // Adding a GeoJson source for the SymbolLayer icons.
                                                .withSource(new GeoJsonSource(SOURCE_ID,
                                                        FeatureCollection.fromFeatures(symbolLayerIconFeatureList)))

                                                // Adding the actual SymbolLayer to the map style. An offset is added that the bottom of the red
                                                // marker icon gets fixed to the coordinate, rather than the middle of the icon being fixed to
                                                // the coordinate point. This is offset is not always needed and is dependent on the image
                                                // that you use for the SymbolLayer icon.
                                                .withLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                                                        .withProperties(
                                                                iconImage(ICON_ID),
                                                                iconAllowOverlap(true),//value is true
                                                                iconIgnorePlacement(true)//value is true
                                                        )
                                                ), new Style.OnStyleLoaded() {
                                            @Override
                                            public void onStyleLoaded(@NonNull Style style) {

                                            }
                                        });

                                    }
                                });




                            }

                        }
                    });
                    break;
                case 2:
                    String schemen1=extras.getString("para_scheme");
                    String staten1=extras.getString("para_statename");
                    firestore.collection("ASSETS").whereEqualTo("Scheme",schemen1).whereEqualTo("State",staten1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                    System.out.println(documentSnapshot.getData());

                                    double longitude=documentSnapshot.getDouble("Longitude");
                                    double lattitude=documentSnapshot.getDouble("Latitude");

                                    symbolLayerIconFeatureList.add(Feature.fromGeometry(
                                            Point.fromLngLat(longitude,lattitude)));
                                    System.out.println("size of the array list is "+symbolLayerIconFeatureList.size());


                                }
                                mapView.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(@NonNull final MapboxMap mapboxMap) {

                                        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")

                                                // Add the SymbolLayer icon image to the map style
                                                .withImage(ICON_ID, BitmapFactory.decodeResource(DataView.this.getResources(), R.drawable.red_marker))

                                                // Adding a GeoJson source for the SymbolLayer icons.
                                                .withSource(new GeoJsonSource(SOURCE_ID,
                                                        FeatureCollection.fromFeatures(symbolLayerIconFeatureList)))


                                                .withLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                                                        .withProperties(
                                                                iconImage(ICON_ID),
                                                                iconAllowOverlap(true),//value is true
                                                                iconIgnorePlacement(true)//value is true
                                                        )
                                                ), new Style.OnStyleLoaded() {
                                            @Override
                                            public void onStyleLoaded(@NonNull Style style) {


                                            }
                                        });

                                    }
                                });




                            }

                        }
                    });
                    break;
                case 3:
                    String schemen2=extras.getString("para_scheme");
                    String staten2=extras.getString("para_statename");
                    String distn2=extras.getString("para_districtname");
                    firestore.collection("ASSETS").whereEqualTo("Scheme",schemen2).whereEqualTo("State",staten2).whereEqualTo("District",distn2).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                    System.out.println(documentSnapshot.getData());

                                    double longitude=documentSnapshot.getDouble("Longitude");
                                    double lattitude=documentSnapshot.getDouble("Latitude");

                                    symbolLayerIconFeatureList.add(Feature.fromGeometry(
                                            Point.fromLngLat(longitude,lattitude)));
                                    System.out.println("size of the array list is "+symbolLayerIconFeatureList.size());


                                }
                                mapView.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(@NonNull final MapboxMap mapboxMap) {

                                        mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")

                                                // Add the SymbolLayer icon image to the map style
                                                .withImage(ICON_ID, BitmapFactory.decodeResource(DataView.this.getResources(), R.drawable.red_marker))

                                                // Adding a GeoJson source for the SymbolLayer icons.
                                                .withSource(new GeoJsonSource(SOURCE_ID,
                                                        FeatureCollection.fromFeatures(symbolLayerIconFeatureList)))

                                                .withLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                                                        .withProperties(
                                                                iconImage(ICON_ID),
                                                                iconAllowOverlap(true),//value is true
                                                                iconIgnorePlacement(true)//value is true
                                                        )
                                                ), new Style.OnStyleLoaded() {
                                            @Override
                                            public void onStyleLoaded(@NonNull Style style) {
//

                                            }
                                        });

                                    }
                                });




                            }

                        }
                    });
                    break;
                default:
                    break;

            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        mapView.onStart();


    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}