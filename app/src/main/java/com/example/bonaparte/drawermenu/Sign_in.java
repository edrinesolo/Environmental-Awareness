package com.example.bonaparte.drawermenu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sign_in extends AppCompatActivity {
    private EditText usernameInput;
    private EditText emailfield;
    private EditText Password;
    private ProgressDialog mProgress;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        mAuth=FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference("Users");//.child("Users");
        mProgress=new ProgressDialog(this);
        usernameInput=(EditText)findViewById(R.id.name_input);
        emailfield=(EditText)findViewById(R.id.emailInput);
        Password=(EditText)findViewById(R.id.passwordInput);

        Button regbutton=(Button)findViewById(R.id.registerButton);
        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegistering();
            }
        });

    }

    private void startRegistering() {

        final String username=usernameInput.getText().toString().trim();
        String useremail=emailfield.getText().toString().trim();
        String userpassword=Password.getText().toString().trim();


        if(!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(useremail)&&!TextUtils.isEmpty(userpassword)){
            mProgress.setMessage("Registering...");
            mProgress.show();

            mAuth.createUserWithEmailAndPassword(useremail,userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isComplete()){
                        if(task.isSuccessful()){

                            String user_id=mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb= mDatabase.child(user_id);
                            currentUserDb.child("User_Name").setValue(username);
                            currentUserDb.child("User_image").setValue("default");

                            mProgress.dismiss();

                            Intent mainIntent=new Intent(Sign_in.this,MainActivity.class);
                            mainIntent.addFlags(mainIntent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainIntent);

                        }else {
                            Toast.makeText(Sign_in.this, "Registration failed try again later", Toast.LENGTH_LONG).show();
                            mProgress.dismiss();
                        }
                    }

                }
            });




        }

    }


}

