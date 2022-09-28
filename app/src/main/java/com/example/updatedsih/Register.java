package com.example.updatedsih;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db=FirebaseFirestore.getInstance();

        final EditText email = findViewById(R.id.EmailET);
        final EditText mobile = findViewById(R.id.Phone);
        final EditText password = findViewById(R.id.PasswordET);
        final EditText confirmpassword = findViewById(R.id.ConfirmPasswordET);

        final Button signupBtn = findViewById(R.id.SignUpBtn);
        final TextView signinBtn = findViewById(R.id.signinbtn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String getMobileTxt = mobile.getText().toString();
                final String getEmailTxt = email.getText().toString();
                String getpassword=password.getText().toString();
                String getconfirmpassword=confirmpassword.getText().toString();

                Map<String,Object> register=new HashMap<>();
                register.put("Email",getEmailTxt);
                register.put("Mobile", getMobileTxt);

                db.collection("Users").add(register).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(Register.this, "Registration completed", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent( Register.this, PhoneActivity.class);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                });



                //opening otp verifcation along with mobile


            }
        });

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Register.this, PhoneActivity.class);
                startActivity(intent);

            }
        });


    }
}