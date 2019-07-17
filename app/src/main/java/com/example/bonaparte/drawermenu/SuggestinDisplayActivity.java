package com.example.bonaparte.drawermenu;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SuggestinDisplayActivity extends AppCompatActivity  {
    private FloatingActionButton fab;
    private RecyclerView sugesttList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabaseusers;
    private FirebaseAuth.AuthStateListener mAuthListener;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestin_display);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SuggestinDisplayActivity.this, Suggestions.class);
                startActivity(intent);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference("Inquires")/*.child()*/;
        mDatabaseusers = FirebaseDatabase.getInstance().getReference("Users")/*.child()*/;
        if (mDatabaseusers != null) {
            mDatabaseusers.keepSynced(true);
        }
        if (mDatabase != null) {
            mDatabase.keepSynced(true);
        }
        sugesttList = (RecyclerView) findViewById(R.id.listSuggest);
        sugesttList.setHasFixedSize(true);
        sugesttList.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    protected void onStart() {
        try {
            super.onStart();
            //set up authstatelistener
            user = mAuth.getCurrentUser();
            // mAuth.addAuthStateListener(mAuthListener);
            FirebaseRecyclerAdapter<Suggest, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Suggest, PostViewHolder>(
                    Suggest.class,
                    R.layout.cardsuggest,
                    PostViewHolder.class,
                    mDatabase
            ) {

                @Override
                protected void populateViewHolder(PostViewHolder viewHolder, Suggest model, int position) {

                    final String event_key = getRef(position).getKey();

                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setDescription(model.getDescription());
                    // viewHolder.setEventDate(model.getEventdate());

                    viewHolder.setUserName(model.getUser_name());
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Toast.makeText(SuggestinDisplayActivity.this, event_key, Toast.LENGTH_LONG).show();
                            //Intent intent = new Intent(MainActivity.this, EventDetail.class);
                            //intent.putExtra("event_key", event_key);
                            // startActivity(intent);
                        }
                    });
                }
            };
            //pass the adapter to our event view appearance
            sugesttList.setAdapter(firebaseRecyclerAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }
}

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDescription(String description) {

            TextView Tdescription = (TextView) mView.findViewById(R.id.detail2);
            Tdescription.setText(description);

        }

        public void setUserName(String UserName) {

            TextView theuser = (TextView) mView.findViewById(R.id.username3);
            theuser.setText(UserName);

        }

        public void setTitle(String title) {
            TextView mtitle = (TextView) mView.findViewById(R.id.title1);
            mtitle.setText(title);

        }


    }}
