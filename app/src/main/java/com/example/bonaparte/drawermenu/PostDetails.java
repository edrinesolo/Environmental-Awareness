package com.example.bonaparte.drawermenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PostDetails extends AppCompatActivity {
    private TextView dummy;
    private String post_id=null;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private RelativeLayout rl;
    private ImageView event_imageView;
    private TextView desc,title,place_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        Bundle bundle = getIntent().getExtras();
        if (bundle.isEmpty())
            return;
        String key = bundle.getString("event_key");;
        post_id=key;

        event_imageView=(ImageView)findViewById(R.id.event_imag);
        desc=(TextView)findViewById(R.id.description_tex);
        title=(TextView)findViewById(R.id.event_titl);
        place_name=(TextView)findViewById(R.id.place_name);



        mAuth=FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Posts");
        mDatabase.child(post_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String event_title= (String) dataSnapshot.child("title").getValue();
                String event_desc= (String) dataSnapshot.child("description").getValue();
                String event_image= (String) dataSnapshot.child("image").getValue();
                String place_uid= (String) dataSnapshot.child("Userid").getValue();
                String place_user= (String) dataSnapshot.child("User_Name").getValue();


                title.setText(event_title);
                desc.setText(event_desc);
                place_name.setText(place_user);

                Picasso.with(PostDetails.this).load(event_image).into(event_imageView);
                String userId=mAuth.getCurrentUser().getUid();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}