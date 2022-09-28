package com.example.updatedsih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
   Button maps,dataentry;
   FirebaseFirestore firestore;
   RecyclerView recyclerView;
   RecyclerView.LayoutManager layoutManager;
   SchemeAdapter schemeAdapter;
   TextView schemenum;
   NavigationView navigationView;
   DrawerLayout drawerLayout;

   private SchemeAdapter.RecyclerViewListner listner;

   ArrayList<String> schemesList= new ArrayList<>();
   ArrayList<String> discriptionList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firestore=FirebaseFirestore.getInstance();

//        maps=findViewById(R.id.maps);
//        dataentry=findViewById(R.id.dataentry);
        recyclerView= findViewById(R.id.recyclerView_s1);
        navigationView= findViewById(R.id.navigation_view);
        drawerLayout= findViewById(R.id.drawable_layout);
        schemenum= findViewById(R.id.scheme_numbers);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                userMenuSelector(item);
                return false;
            }
        });




        setOnClickListner();
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        schemeAdapter = new SchemeAdapter(schemesList,discriptionList, listner);
        recyclerView.setAdapter(schemeAdapter);

        firestore.collection("DESCRIPTION").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                         for (QueryDocumentSnapshot document: task.getResult()){
                             schemesList.add(document.getId().toString());
                             discriptionList.add(document.getString("details"));
                             System.out.println(document.getId().toString());
                            schemenum.setText(String.valueOf(discriptionList.size()));
                         }
                    schemeAdapter.notifyDataSetChanged();
                }
            }
        });
//

    }

    private void userMenuSelector(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_dataEntry :
                Toast.makeText(this, "this is your text", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,DatabaseActivity.class));
                break;
            case R.id.To_maps : break;
            default:
                break;

        }
    }


    private void setOnClickListner() {
        listner = new SchemeAdapter.RecyclerViewListner() {
            @Override
            public void onClick(View v, int position) {
                startActivity(new Intent(getApplicationContext(),States_Data.class).putExtra("scheme_name",schemesList.get(position)));
            }
        };
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteCache(this);
    }
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


}