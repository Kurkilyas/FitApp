package com.fitapp;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fitapp.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class LoginActivity extends AppCompatActivity {


    private EditText editEmail, editPassword,editUpdateName,editUpdatePassword;
    private String txtEmail, txtPassword, txtUpdateName,txtUpdatePassword;

    private FirebaseAuth mAuth;

    private FirebaseUser mUser;

    private DatabaseReference mReference;

    private HashMap<String, Object> mData;

    TextView forgotPassword;

    SharedPreferences sharedPreferences;
    private static final String Shared_prefence_name="mypref";
    private static final String Key_Email="email";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail= (EditText) findViewById(R.id.giris_mail);
        editPassword =(EditText) findViewById(R.id.giris_password);

        forgotPassword = findViewById(R.id.forgot_password);
        sharedPreferences=getSharedPreferences(Shared_prefence_name,MODE_PRIVATE);



        mAuth = FirebaseAuth.getInstance();

        mUser = mAuth.getCurrentUser();



    }


    public void girisYap(View view){

        txtEmail = editEmail.getText().toString();
        txtPassword = editPassword.getText().toString();


        if (!TextUtils.isEmpty(txtEmail) && !TextUtils.isEmpty(txtPassword)){



            mAuth.signInWithEmailAndPassword(txtEmail,txtPassword)
                    .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            mUser = mAuth.getCurrentUser();
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString(Key_Email,editEmail.getText().toString());
                                editor.apply();


                            assert mUser != null;
                            verileriGetir(mUser.getUid());

                            Intent intent = new Intent(LoginActivity.this, MainActivityTemp.class);
                           startActivity(intent);

                        }
                    }).addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    });


        }else
            Toast.makeText(this,"Böyle bir kullanıcı yok",Toast.LENGTH_SHORT).show();


    }

    private void verileriGetir(String uid){
        mReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snp: snapshot.getChildren()){
                    System.out.println(snp.getKey() + " = " + snp.getValue());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(LoginActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

public void uyeOl(View view)
{

    Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
    startActivity(intent);
}
    private void veriGuncelle(HashMap<String,Object> hashMap, final String uid){

    mReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);

    mReference.updateChildren(hashMap)
            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){

                        Toast.makeText(LoginActivity.this, "Data updates successfully",Toast.LENGTH_SHORT).show();
                        System.out.println("------ Güncellenen Veriler --------");
                        verileriGetir(uid);
                    }

                }
            });

    }




    public void isimGuncelle(View view){
        txtUpdateName = editUpdateName.getText().toString();


        if(!TextUtils.isEmpty(txtUpdateName)){

            mData = new HashMap<>();

            mData.put("Username", txtUpdateName);


            assert mUser != null;
            veriGuncelle(mData,mUser.getUid());
        }else
            Toast.makeText(this,"The Value to be updated cannot be empty",Toast.LENGTH_SHORT).show();
    }

    public void sifreGuncelle(View view){
        txtUpdatePassword = editUpdatePassword.getText().toString();


        if(!TextUtils.isEmpty(txtUpdatePassword)){

            mData = new HashMap<>();

            mData.put("UserPassword", txtUpdatePassword);


            assert mUser != null;
            veriGuncelle(mData,mUser.getUid());
        }else
            Toast.makeText(this,"The Value to be updated cannot be empty",Toast.LENGTH_SHORT).show();
    }



    public void veriSil(View view){
        mReference=FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid());

        mReference.removeValue()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                            Toast.makeText(LoginActivity.this,"Data Başarıyla Silindi",Toast.LENGTH_SHORT).show();
                        else
                                Toast.makeText(LoginActivity.this,"Data Silinemedi",Toast.LENGTH_SHORT).show();

                    }
                });


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.activity_forgot_password, null);
                EditText emailBox = dialogView.findViewById(R.id.emailBox);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userEmail = emailBox.getText().toString();
                        if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                            Toast.makeText(LoginActivity.this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, "Check your email", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Unable to send, failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                if (dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });




    }




}