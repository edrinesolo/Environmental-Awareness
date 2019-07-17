package com.example.bonaparte.drawermenu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//
import android.app.ProgressDialog;
import android.net.Uri;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class Sensitise extends AppCompatActivity {
    private FirebaseAuth mAuth;
    //
    public static final int GALLERY_REQUEST = 1;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Posts");
    private StorageReference mStorageImage;
    private Button add_Sense;
    private EditText post_title;
    private EditText post_desc;
    private EditText due_date;
    private ProgressDialog mProgress;
    private Uri mImageUri = null;
    private ImageButton pickd_image;
  //  private FirebaseUser mCurrentUser;
  FirebaseUser user;
    private DatabaseReference mDatabasePosts;
    private CoordinatorLayout main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensitise);

        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sensitise Other Users");
        main = (CoordinatorLayout) findViewById(R.id.main);
        mProgress = new ProgressDialog(this);

      // FirebaseUser user = mAuth.getCurrentUser();

//check if user is logged in
        mAuth = FirebaseAuth.getInstance();
         user = mAuth.getCurrentUser();
        if (user == null) {
            Intent i=new Intent(Sensitise.this,Login.class);
            startActivity(i);
            finish();
        }
        else {
            mDatabasePosts = FirebaseDatabase.getInstance().getReference("Users")/*.child("Users")*/.child(user.getUid());

            //


            post_title = (EditText) findViewById(R.id.event_title);
            post_desc = (EditText) findViewById(R.id.event_description);

            mStorageImage = FirebaseStorage.getInstance().getReference().child("Poster_Images");
            add_Sense=(Button)findViewById(R.id.post_event_button);
            add_Sense.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startAccountSetUp();
                }
            });


            pickd_image = (ImageButton) findViewById(R.id.picked_img);


            pickd_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.setType("image/*");
                    startActivityForResult(i, GALLERY_REQUEST);
                }
            });
        }

        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1, 1).start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();
                pickd_image.setImageURI(mImageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


    private void startAccountSetUp() {
        mProgress.setMessage("Posting.....");
        mProgress.show();
        final String title_val = post_title.getText().toString().trim();
        final String desc_val = post_desc.getText().toString().trim();
//        final String event_date = due_date.getText().toString().trim();
        if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val)  && mImageUri != null) {

            StorageReference filepath = mStorageImage.child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final String post_imageUri = taskSnapshot.getDownloadUrl().toString();
                    final DatabaseReference newPost = myRef.push();


                    mDatabasePosts.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            newPost.child("title").setValue(title_val);
                            newPost.child("description").setValue(desc_val);
                            newPost.child("image").setValue(post_imageUri);

                            newPost.child("Userid").setValue(user.getUid());
                            newPost.child("User_name").setValue(dataSnapshot.child("User_Name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mProgress.dismiss();
                                        Toast.makeText(Sensitise.this, "Posted successfully", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(Sensitise.this, MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                    } else {

                                        Snackbar.make(main, "sorry sign in error!", Snackbar.LENGTH_LONG).show();

                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mProgress.dismiss();
                    Toast.makeText(Sensitise.this,"Posting not successfull ",Toast.LENGTH_LONG).show();
                }
            });

        }else {
            Toast.makeText(Sensitise.this,"fill all fields ",Toast.LENGTH_LONG).show();
        }

    }

}






