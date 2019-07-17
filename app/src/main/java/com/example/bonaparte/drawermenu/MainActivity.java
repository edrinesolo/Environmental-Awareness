package com.example.bonaparte.drawermenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//
import android.content.Context;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView posttList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabaseusers;
    private FirebaseAuth.AuthStateListener mAuthListener;
    CoordinatorLayout mnl;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //checking database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference("Posts")/*.child()*/;
        mDatabase.keepSynced(true);
        mDatabaseusers = FirebaseDatabase.getInstance().getReference("Users")/*.child()*/;
        mDatabaseusers.keepSynced(true);
        if (mDatabaseusers != null) {
            mDatabaseusers.keepSynced(true);
        }
        if (mDatabase != null) {
            mDatabase.keepSynced(true);
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        posttList = (RecyclerView) findViewById(R.id.event_list);
        posttList.setHasFixedSize(true);
        posttList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {

        try {
            super.onStart();
            //set up authstatelistener
            user = mAuth.getCurrentUser();
           // mAuth.addAuthStateListener(mAuthListener);
            mDatabase.keepSynced(true);
            mDatabaseusers.keepSynced(true);
            FirebaseRecyclerAdapter<Posts, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posts, PostViewHolder>(
                    Posts.class,
                    R.layout.cardplaceholder,
                    PostViewHolder.class,
                    mDatabase

            ) {

                @Override
                protected void populateViewHolder(PostViewHolder viewHolder, Posts model, int position) {

                    final String event_key = getRef(position).getKey();

                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setDescription(model.getDescription());
                    // viewHolder.setEventDate(model.getEventdate());
                    viewHolder.setImage(getApplicationContext(), model.getImage());
                    viewHolder.setPlaceName(model.getPlace_name());
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Toast.makeText(MainActivity.this, event_key, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, PostDetails.class);
                            intent.putExtra("event_key", event_key);
                             startActivity(intent);
                        }
                    });
                }
            };
            //pass the adapter to our event view appearance
            posttList.setAdapter(firebaseRecyclerAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

        @Override
        public void onBackPressed () {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            MenuItem register = menu.findItem(R.id.logout);
            if(user == null)
            {
                register.setVisible(false);
            }
            else
            {
                register.setVisible(true);
            }

            return true;

        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){

            switch (item.getItemId()) {
                case R.id.logout:
                    mAuth = FirebaseAuth.getInstance();
                    user = mAuth.getCurrentUser();
                    mAuth.signOut();
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }





        }

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected (MenuItem item){
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.wetlands) {
                Intent w = new Intent(this, wetlands.class);
                startActivity(w);

            } else if (id == R.id.lakes) {
                Intent l = new Intent(this, lakes.class);
                startActivity(l);

            } else if (id == R.id.forests) {
                Intent f = new Intent(this, forests.class);
                startActivity(f);

            } else if (id == R.id.others) {
                Intent o = new Intent(this, Others.class);
                startActivity(o);
            } else if (id == R.id.sensitise) {
                Intent i = new Intent(this, Sensitise.class);
                startActivity(i);

            }else if(id== R.id.inquire){
                Intent inquires=new Intent(this,SuggestinDisplayActivity.class);
                startActivity(inquires);

            }else if(id== R.id.contact){
                Intent contact=new Intent(this,ContactUsActivity.class);
                startActivity(contact);

            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }


    public static class PostViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDescription(String description) {

            TextView Tdescription = (TextView) mView.findViewById(R.id.description_text);
            Tdescription.setText(description);

        }

        public void setPlaceName(String UserName) {

            TextView theuser = (TextView) mView.findViewById(R.id.uname);
            theuser.setText(UserName);

        }

        public void setTitle(String title) {
            TextView mtitle = (TextView) mView.findViewById(R.id.title);
            mtitle.setText(title);

        }
/*
        public void setEventDate(String eventDate) {
            TextView event_date = (TextView) mView.findViewById(R.id.event_date);
            event_date.setText(eventDate);

        }
*/

        public void setImage(final Context context, final String image) {
            final ImageView p_image = (ImageView) mView.findViewById(R.id.image);
            // Picasso.with(context).load(image).into(event_image);
            Picasso.with(context).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(p_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(image).into(p_image);
                }
            });
        }


    }
}
