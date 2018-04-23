package e.utilisateur.easylife01;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Thibault WIRTZ on 13/03/2018.
 */


public class Selcatscr extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;
    private String name;
    private FirebaseUser user;
    private boolean admin;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selcatscr);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Loading information...");
        mProgress.show();

        mAuth = FirebaseAuth.getInstance();

        mDataBase = FirebaseDatabase.getInstance().getReference("Users");

        user = mAuth.getCurrentUser();

        //Test if is admin and set Title
        mDataBase.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String v = dataSnapshot.child("Name").getValue(String.class);
                setTitle(v);
                try{
                    String tempAd = getIntent().getStringExtra("admin");

                    if(tempAd.equals("no")){
                        admin = false;
                    }
                    else{
                        admin = true;
                    }
                }
                catch(Exception e){
                    if(dataSnapshot.child("isAdmin").getValue(String.class).equals("yes")){
                        admin=true;
                    }
                    else{
                        admin=false;
                    }
                }

                mProgress.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main; this adds items to the action bar if it is present.

        if(admin){
            getMenuInflater().inflate(R.menu.menu_admincat, menu);
        }
        else{
            getMenuInflater().inflate(R.menu.menu_cat, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(admin==true){
            switch (id) {
                case R.id.action_disc:
                    mAuth.signOut();
                    Intent intent = new Intent(this, Logscr.class);
                    startActivity(intent);
                    return true;

                case R.id.action_fav:
                    Intent intent01 = new Intent(this, Favscr.class);
                    startActivity(intent01);
                    return true;

                case R.id.action_watchfeedbacks:
                    Intent intent02 = new Intent(this,LstFbscr.class);
                    startActivity(intent02);
                    return true;

                case R.id.action_request:
                    Intent intent03 = new Intent(this,AddPlacescr.class);
                    startActivity(intent03);
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        else{
            switch (id) {
                case R.id.action_disc:
                    Intent intent = new Intent(this, Logscr.class);
                    startActivity(intent);
                    return true;

                case R.id.action_fav:
                    Intent intent01 = new Intent(this, Favscr.class);
                    startActivity(intent01);
                    return true;

                case R.id.action_feedback:
                    Intent intent02 = new Intent(this,Feedbackscr.class);
                    startActivity(intent02);
                    return true;

                case R.id.action_request:
                    Intent intent03 = new Intent(this,AddPlacescr.class);
                    startActivity(intent03);
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }
        }


    }

    /*public void onClickBaseCat(View view) {
        Intent intent = new Intent(this, Periscr.class);
        startActivity(intent);
    }*/

    public void onClickUniv(View view) {
        int choice = 1;
        Intent intent = new Intent(this, Roomscr.class);
        intent.putExtra("choice", choice);
        startActivity(intent);
    }

    public void onClickResto(View view) {
        String choice = "Restaurants";
        Intent intent = new Intent(this, Typefood.class);
        intent.putExtra("choice", choice);
        startActivity(intent);
    }

    public void onClickPubBar(View view) {
        String choice = "PnB";
        Intent intent = new Intent(this, Typefood.class);
        intent.putExtra("choice", choice);
        startActivity(intent);
    }

    public void onClickShop(View view) {
        String choice = "Shops";
        Intent intent = new Intent(this, Typefood.class);
        intent.putExtra("choice", choice);
        startActivity(intent);
    }

    public void onEverythingClicked(View view) {
        Intent intent = new Intent(this,Everythingscr.class);
        startActivity(intent);
    }
}
