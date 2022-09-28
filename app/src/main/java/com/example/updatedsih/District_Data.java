package com.example.updatedsih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class District_Data extends AppCompatActivity {

    ArrayList<String> district_List= new ArrayList<>();
    FirebaseFirestore firestore;
    StateAdapter stateAdapter;
    RecyclerView recyclerView;
    TextView distcount;
    RecyclerView.LayoutManager layoutManager;
    FloatingActionButton floating_maps_button;
    private StateAdapter.RecyclerViewListner listner;
    String schemename,statename;

    long assetcount1 = 0;
    long amountcount1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_district_data);

        recyclerView=findViewById(R.id.recyclerView_district);
        floating_maps_button=findViewById(R.id.floating_maps_button);
        distcount=findViewById(R.id.district_numbers);
         firestore=FirebaseFirestore.getInstance();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            schemename = extras.getString("scheme_name");
            statename = extras.getString("state_name");
            String pathstate="DESCRIPTION/"+schemename+"/states/"+statename+"/districts";
            System.out.println("This is the path "+pathstate);

            setOnClickListner();
            recyclerView.setLayoutManager(new LinearLayoutManager(District_Data.this));
            stateAdapter = new StateAdapter(district_List, listner);
            recyclerView.setAdapter(stateAdapter);

            firestore.collection(pathstate).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            district_List.add(document.getId().toString());
                            System.out.println(document.getId().toString());
                        }
                        distcount.setText((String.valueOf( district_List.size())));
                        stateAdapter.notifyDataSetChanged();

                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("error is the :"+e.getMessage().toString());
                }
            });



            floating_maps_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(District_Data.this,DataView.class);
                    intent.putExtra("code",2);
                    intent.putExtra("para_scheme",schemename);
                    intent.putExtra("para_statename",statename);
                    startActivity(intent);
                }
            });



            firestore.collection("DESCRIPTION").
                    document(schemename).collection("states")
                    .document(statename).collection("districts")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot document: task.getResult()
                                ) {

                                    firestore.collection("DESCRIPTION")
                                            .document(schemename)
                                            .collection("states").document(statename)
                                            .collection("districts").document(document.getId().toString())
                                            .collection("villages").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful())
                                                    {
                                                        for (QueryDocumentSnapshot document: task.getResult()
                                                        ){

                                                            assetcount1 = assetcount1 + document.getLong("assetcount");
                                                            amountcount1 = amountcount1 + document.getLong("totalamount");
                                                            TextView assetnumberrrrrrrr =  findViewById(R.id.asset_numbers);
                                                            TextView amountnumberrrrrrrr =  findViewById(R.id.scheme_capital);
                                                            int var =  Integer.parseInt(assetnumberrrrrrrr.getText().toString()) + Integer.parseInt(document.getLong("assetcount").toString());
                                                            long var1 =     Long.parseLong(amountnumberrrrrrrr.getText().toString())   + document.getLong("totalamount");
//                                                            int var1 =  Integer.parseInt(amountnumberrrrrrrr.getText().toString()) + Integer.parseInt(document.getLong("totalamount").toString());
                                                            assetnumberrrrrrrr.setText(String.valueOf(var));
                                                            amountnumberrrrrrrr.setText(String.valueOf(var1));
                                                        }

                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    });



        }






    }

    private void setOnClickListner() {

        listner= new StateAdapter.RecyclerViewListner() {
            @Override
            public void onClick(View v, int position) {
                Intent intent= new Intent(getApplicationContext(),Village_Data.class);
                intent.putExtra("district_name",district_List.get(position));
                intent.putExtra("state_name",statename);
                intent.putExtra("scheme_name",schemename);
                startActivity(intent);
            }
        };
    }

}



