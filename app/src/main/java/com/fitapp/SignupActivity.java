package com.fitapp;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fitapp.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;


public class SignupActivity extends AppCompatActivity {


    private EditText editEmail, editPassword,editName;
    private String txtEmail, txtPassword, txtName;

    private FirebaseUser mUser;

    private FirebaseAuth mAuth;

    private DatabaseReference mReference;

    private HashMap<String, Object> mData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        editEmail= (EditText) findViewById(R.id.kayit_ol_mail);
        editPassword =(EditText) findViewById(R.id.kayit_ol_password);
        editName = (EditText) findViewById(R.id.kayit_ol_nickname);


        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();

    }

    public void kayitOl(View view){

        txtName = editName.getText().toString();
        txtEmail = editEmail.getText().toString();
        txtPassword = editPassword.getText().toString();

        if(!TextUtils.isEmpty(txtName) && !TextUtils.isEmpty(txtEmail) && !TextUtils.isEmpty(txtPassword)){
            mAuth.createUserWithEmailAndPassword(txtEmail,txtPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                mUser = mAuth.getCurrentUser();


                                mData = new HashMap<>();
                                mData.put("Username", txtName);
                                mData.put("User Mail : ", txtEmail);
                                mData.put("UserPassword", txtPassword);
                                mData.put("User Id", mUser.getUid());


                                mReference.child("Users").child(mUser.getUid())
                                        .setValue(mData)
                                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()){
                                                    Toast.makeText(SignupActivity.this,"Registratin Successful",Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                                    startActivity(intent);

                                                }else
                                                    Toast.makeText(SignupActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                                            }
                                        });

                            }

                            else
                                Toast.makeText(SignupActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    });
        }else
            Toast.makeText(this, "Email Ve Şifre Boş Olamaz",Toast.LENGTH_SHORT ).show();

    }





}