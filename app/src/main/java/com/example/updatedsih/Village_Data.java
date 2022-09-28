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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Village_Data extends AppCompatActivity {
    ArrayList<String> village_list= new ArrayList<>();
    FirebaseFirestore firestore;
    StateAdapter stateAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView villagecount;
    FloatingActionButton floating_maps_button;
    private StateAdapter.RecyclerViewListner listner;
    String schemename,statename,districtname;
    long assetcount1 = 0;
    long amountcount1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_village_data);


        recyclerView=findViewById(R.id.recyclerView_village);
        floating_maps_button=findViewById(R.id.floating_maps_button);
        villagecount=findViewById(R.id.villages_numbers);
        firestore=FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            schemename = extras.getString("scheme_name");
            statename = extras.getString("state_name");
            districtname = extras.getString("district_name");
            String pathstate="DESCRIPTION/"+schemename+"/states/"+statename+"/districts/"+districtname+"/villages";

            setOnClickListner();
            recyclerView.setLayoutManager(new LinearLayoutManager(Village_Data.this));
            stateAdapter = new StateAdapter(village_list, listner);
            recyclerView.setAdapter(stateAdapter);



            firestore.collection(pathstate).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            village_list.add(document.getId().toString());
                            System.out.println(document.getId().toString());
                        }
                        stateAdapter.notifyDataSetChanged();
                        villagecount.setText(String.valueOf(village_list.size()));
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
                    Intent intent = new Intent(Village_Data.this,DataView.class);
                    intent.putExtra("code",3);
                    intent.putExtra("para_scheme",schemename);
                    intent.putExtra("para_statename",statename);
                    intent.putExtra("para_districtname",districtname);
                    startActivity(intent);
                }
            });

//////////////////////////////


            firestore.collection("DESCRIPTION")
                    .document(schemename)
                    .collection("states").document(statename)
                    .collection("districts").document(districtname)
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
                                    assetnumberrrrrrrr.setText(String.valueOf(var));
                                    amountnumberrrrrrrr.setText(String.valueOf(var1));
                                }

                            }
                        }
                    });
//

        }
    }

    private void setOnClickListner() {
        listner= new StateAdapter.RecyclerViewListner() {
            @Override
            public void onClick(View v, int position) {
                Intent intent= new Intent(getApplicationContext(),UserList.class);
                intent.putExtra("village_name",village_list.get(position));
                intent.putExtra("district_name",districtname);
                intent.putExtra("state_name",statename);
                intent.putExtra("scheme_name",schemename);
                startActivity(intent);
            }
        };
    }
}