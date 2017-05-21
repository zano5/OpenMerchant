package tut.ac.za.openmerchant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tut.ac.za.openmerchant.Classes.Ads;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,GoogleApiClient.OnConnectionFailedListener{

    private List<Ads> adsList;
    private DatabaseReference db;
    @BindView(R.id.ivAdvert1) SimpleDraweeView advert1;
    @BindView(R.id.ivAdvert2) SimpleDraweeView advert2;
    @BindView(R.id.ivAdvert3) SimpleDraweeView advert3;
    @BindView(R.id.ivAdvert4) SimpleDraweeView advert4;
    @BindView(R.id.ivAdvert5) SimpleDraweeView advert5;
    @BindView(R.id.flipper) ViewFlipper flipper;

    private FirebaseAuth mAuth;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUsername;
    private String mPhotoUrl;
    private GoogleApiClient mGoogleApiClient;
    private TextView tvName,tvEmail;
    private String mEmail;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        ButterKnife.bind(this);

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this,GmailSignActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            mEmail = mFirebaseUser.getEmail();
        }




        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();


        adsList = new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference().child("Ads").child("AdsList");


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for(DataSnapshot data : dataSnapshot.getChildren())
                {

                    Ads ad= data.getValue(Ads.class);
                    adsList.add(ad);
                }



                onlineAds(adsList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getHeaderView(0);


       ImageView ivImage = ButterKnife.findById(view,R.id.ivImage);
       ivImage.setImageURI(mFirebaseUser.getPhotoUrl());
        TextView tvName = ButterKnife.findById(view,R.id.tvName);
        tvName.setText(mFirebaseUser.getDisplayName());
        TextView tvEmail = ButterKnife.findById(view,R.id.tvEmail);
        tvEmail.setText(mAuth.getCurrentUser().getEmail());



        flipper.setFlipInterval(2000);
        flipper.startFlipping();
    }



    private void onlineAds(List<Ads> adsList)
    {

        Uri uri = Uri.parse(adsList.get(0).getUrl());
        advert1.setImageURI(uri);

        uri = Uri.parse(adsList.get(1).getUrl());
        advert2.setImageURI(uri);

        uri = Uri.parse(adsList.get(2).getUrl());

        advert3.setImageURI(uri);
        uri = Uri.parse(adsList.get(3).getUrl());
        advert4.setImageURI(uri);
        uri = Uri.parse(adsList.get(4).getUrl());
        advert5.setImageURI(uri);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       if(id== R.id.nav_about)
       {


           Intent intent = new Intent(MainActivity.this,AboutActivity.class);
           startActivity(intent);

       }else if(id == R.id.nav_feedback)
       {

           Intent intent = new Intent(MainActivity.this,FeedbackActivity.class);
           startActivity(intent);

       }else if(id == R.id.nav_order)
       {

           Intent intent = new Intent(MainActivity.this,OrderActivity.class);
           startActivity(intent);

       }else if(id == R.id.nav_store)
       {


                           Intent intent1 = new Intent(MainActivity.this,StoreActivity.class);
                           startActivity(intent1);





       }else if(id == R.id.nav_sign_out)
       {

           mAuth.signOut();

           Intent intent = new Intent(MainActivity.this,GmailSignActivity.class);
           startActivity(intent);
           finish();


       }/*else if(id == R.id.nav_comment)
       {


           AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

           CharSequence[] options = {"Add Comment","View Comments"};


           builder.setItems(options, new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {


                   switch (which)
                   {

                       case 0:

                           Intent intent = new Intent(MainActivity.this,CommentActivity.class);
                           startActivity(intent);

                           break;

                       case 1:

                           Intent intent1= new Intent(MainActivity.this,CommentReviewActivity.class);
                           startActivity(intent1);

                           break;
                   }
               }
           });


           Dialog dialog = builder.create();

           dialog.show();

       }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
