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

public class States_Data extends AppCompatActivity {
   private  String schemename;
   private FloatingActionButton floating_maps_button;
   ArrayList<String> stateList= new ArrayList<>();
   FirebaseFirestore firestore;
   StateAdapter stateAdapter;
   TextView statecount;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private StateAdapter.RecyclerViewListner listner_state;

    long assetcount1 = 0;
    long amountcount1 = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.states_data);
        // initializeation of variables
        recyclerView= findViewById(R.id.recyclerView_states);
        floating_maps_button= findViewById(R.id.floating_maps_button);
        statecount= findViewById(R.id.state_numbers);
        firestore=FirebaseFirestore.getInstance();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            schemename = extras.getString("scheme_name");
            String pathstate="DESCRIPTION/"+schemename+"/states";
//            getActionBar().setTitle("SCHEME:"+value);

            setOnClickListner();
            recyclerView.setLayoutManager(new LinearLayoutManager(States_Data.this));
            stateAdapter = new StateAdapter(stateList, listner_state);
            recyclerView.setAdapter(stateAdapter);

            firestore.collection(pathstate).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            stateList.add(document.getId().toString());
                            System.out.println(document.getId().toString());
                        }
                        statecount.setText(String.valueOf(stateList.size()));
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
                    Intent intent = new Intent(States_Data.this,DataView.class);
                    intent.putExtra("code",1);
                    intent.putExtra("para_scheme",schemename);
                    startActivity(intent);
                }
            });




            /////////////////////////////////

            firestore.collection("DESCRIPTION").
                    document(schemename).collection("states")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot document: task.getResult()
                                ) {

                                    firestore.collection("DESCRIPTION")
                                            .document(schemename)
                                            .collection("states").document(document.getId().toString())
                                            .collection("districts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful())
                                                    {
                                                        for (QueryDocumentSnapshot document1: task.getResult()
                                                        ){

                                                            firestore.collection("DESCRIPTION")
                                                                    .document(schemename)
                                                                    .collection("states").document(document.getId().toString())
                                                                    .collection("districts").document(document1.getId().toString())
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




///////////////
                                    firestore.collection("DESCRIPTION")
                                            .document(schemename)
                                            .collection("states").document(document.getId().toString())
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

        listner_state= new StateAdapter.RecyclerViewListner() {
            @Override
            public void onClick(View v, int position) {
                Intent intent= new Intent(getApplicationContext(),District_Data.class);
                intent.putExtra("state_name",stateList.get(position));
                intent.putExtra("scheme_name",schemename);
                startActivity(intent);
            }
        };
    }

}
