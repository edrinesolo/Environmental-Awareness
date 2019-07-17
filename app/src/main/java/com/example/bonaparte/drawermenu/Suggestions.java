package com.example.bonaparte.drawermenu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Suggestions extends AppCompatActivity {
    EditText title,details;
    Button inquire;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Inquires");
    FirebaseUser user;
    private DatabaseReference mDatabasePosts;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private CoordinatorLayout main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestions);
        title = (EditText) findViewById(R.id.suggestion_title);
        details = (EditText) findViewById(R.id.suggestion_detail);
        inquire = (Button) findViewById(R.id.post_inquire_button);
        mProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user == null) {
            Intent i = new Intent(Suggestions.this, Login.class);
            startActivity(i);
            finish();

        } else {
            mDatabasePosts = FirebaseDatabase.getInstance().getReference("Users")/*.child("Users")*/.child(user.getUid());


            inquire.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    post();
                }
            });
        }
    }

     void post() {
        ///posting data to firebase here.................

        final String Title = title.getText().toString().trim();
        final String Detail = details.getText().toString().trim();

         if (!TextUtils.isEmpty(Title) && !TextUtils.isEmpty(Detail)){
             mProgress.setMessage("Posting.....");
             mProgress.show();
             final DatabaseReference newPost = myRef.push();

             mDatabasePosts.addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(DataSnapshot dataSnapshot) {

                     newPost.child("title").setValue(Title);
                     newPost.child("description").setValue(Detail);


                     newPost.child("Userid").setValue(user.getUid());
                     newPost.child("User_name").setValue(dataSnapshot.child("User_Name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             if (task.isSuccessful()) {
                                 mProgress.dismiss();
                                 Toast.makeText(Suggestions.this, "Posted successfully", Toast.LENGTH_LONG).show();
                                 Intent i = new Intent(Suggestions.this, MainActivity.class);
                                 i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                 startActivity(i);
                             } else {

                                 Snackbar.make(main, " error!", Snackbar.LENGTH_LONG).show();

                             }
                         }
                     });
                 }


                 @Override
                 public void onCancelled(DatabaseError databaseError) {

                 }
             });


         }

        }}
