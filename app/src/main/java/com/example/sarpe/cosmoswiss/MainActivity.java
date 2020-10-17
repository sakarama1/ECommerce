package com.example.sarpe.cosmoswiss;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //definitions
    //buttons
    Button herrenButton;
    Button damenButton;
    Button haareButton;
    Button warenKorbButton;
    Button makeupButton;
    Button korperPflegeButton;
    Button zubehorButton;
    Button newsButton;

    private FirebaseAuth mAuth;

    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();


        //initialize
        //buttons
        herrenButton = findViewById(R.id.herrenButton);
        damenButton = findViewById(R.id.damenButton);
        haareButton = findViewById(R.id.haareButton);
        warenKorbButton = findViewById(R.id.warenKorbButton);
        makeupButton = findViewById(R.id.makeupButton);
        korperPflegeButton = findViewById(R.id.korperPflegeButton);
        zubehorButton = findViewById(R.id.zubehorButton);
        newsButton = findViewById(R.id.newsButton);


        //floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();

                Intent intent = new Intent(getApplicationContext(), ControlPanelActivity.class);
                startActivity(intent);
            }
        });

        //for drawer layout
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.userEmailDrawerEditText);
        if(currentUser != null)
        navUsername.setText(currentUser.getEmail());
        TextView header = (TextView) headerView.findViewById(R.id.headerDrawerEditText);
        header.setText("Cosmo Swiss");
        navigationView.setNavigationItemSelectedListener(this);
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //if you implement this drawer use this
//        if (id == R.id.nav_home) {
//            Intent intent = intent = new Intent(getApplicationContext(), ShowUserInformationActivity.class);
//            startActivity(intent);
//        }

        //Mein profil
        if (id == R.id.nav_user_info) {

            if(currentUser != null){
                Intent intent = intent = new Intent(getApplicationContext(), ShowUserInformationActivity.class);
                startActivity(intent);
            }
            else {
                Intent intent = intent = new Intent(getApplicationContext(), EinloggenActivity.class);
                startActivity(intent);
            }

        }
        else if (id == R.id.nav_fav) {
            if(currentUser != null){
                Intent intent = intent = new Intent(getApplicationContext(), FavActivity.class);
                intent.putExtra("info", "noAdd");
                startActivity(intent);
            }
            else {
                Intent intent = intent = new Intent(getApplicationContext(), EinloggenActivity.class);
                startActivity(intent);
            }

        }
        else if (id == R.id.nav_bestellung) {
            if(currentUser != null){
                Intent intent = intent = new Intent(getApplicationContext(), HistoryActivity.class);
                intent.putExtra("info", "noAdd");
                startActivity(intent);
            }
            else {
                Intent intent = intent = new Intent(getApplicationContext(), EinloggenActivity.class);
                startActivity(intent);
            }

        }
        else if (id == R.id.nav_suchen) {
            Intent intent = intent = new Intent(getApplicationContext(), ShowListsActivity.class);
            intent.putExtra("category", "all");
            startActivity(intent);
        }

        //Uber uns
        else if (id == R.id.nav_gallery) {
            Intent intent = intent = new Intent(getApplicationContext(), EinloggenActivity.class);
            startActivity(intent);
        }

        else if (id == R.id.nav_info) {
            Intent intent = intent = new Intent(getApplicationContext(), AboutUsActivity.class);
            startActivity(intent);
        }

        ///////////////////////////////
        else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            currentUser = null;
            Toast toast = Toast.makeText(getApplicationContext(), "logged out", Toast.LENGTH_SHORT);
            toast.show();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //make a method for all category buttons after every resource is settled

    //ShowlistsActivity
    public void damen(View view) {
        Intent intent = new Intent(getApplicationContext(), ShowListsActivity.class);
        intent.putExtra("category", "DAMEN");
        startActivity(intent);
    }
    public void herren(View view){
        Intent intent = new Intent(getApplicationContext(), ShowListsActivity.class);
        intent.putExtra("category", "HERREN");
        startActivity(intent);
    }
    public void haare(View view){
        Intent intent = new Intent(getApplicationContext(), ShowListsActivity.class);
        intent.putExtra("category", "HAARE");
        startActivity(intent);
    }

    //CartActivity
    public void warenKorb(View view){
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), CartActivity.class);
            intent.putExtra("info", "noAdd");
            startActivity(intent);
        }
        else {
            Intent intent = intent = new Intent(getApplicationContext(), EinloggenActivity.class);
            startActivity(intent);
        }
    }

    //ShowlistsActivity
    public void makeup(View view){
        Intent intent = new Intent(getApplicationContext(), ShowListsActivity.class);
        intent.putExtra("category", "MAKEUP");
        startActivity(intent);
    }
    public void korperPflege(View view){
        Intent intent = new Intent(getApplicationContext(), ShowListsActivity.class);
        intent.putExtra("category", "KORPERPFLEGE");
        startActivity(intent);
    }
    public void zubehor(View view){
        Intent intent = new Intent(getApplicationContext(), ShowListsActivity.class);
        intent.putExtra("category", "ZUBEHOR");
        startActivity(intent);
    }

    //NewsActivity
    public void news(View view){
        Intent intent = new Intent(getApplicationContext(), ShowListsActivity.class);
        intent.putExtra("category", "marken");
        startActivity(intent);
    }

}
