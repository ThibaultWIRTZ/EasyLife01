package e.utilisateur.easylife01;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Detailsscr extends AppCompatActivity {
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    private String curID, curItem, curCat, curType;
    private boolean b = false;
    private TextView lblDescr, lblTitlePlace;
    private DatabaseReference itemRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        lblDescr = findViewById(R.id.lblDescription);
        lblTitlePlace = findViewById(R.id.lblTitlePlace);

        curItem = getIntent().getStringExtra("ref");

        itemRef = FirebaseDatabase.getInstance().getReferenceFromUrl(curItem);

        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(itemRef.getParent().getKey().equals("University")){
                    String s = dataSnapshot.getKey();
                    s = s.replace("%20"," ");
                    lblTitlePlace.setText(s);
                    lblDescr.setText(dataSnapshot.child("Descr").getValue(String.class));
                }
                else{
                    lblTitlePlace.setText(dataSnapshot.child("Name").getValue(String.class));
                    lblDescr.setText(dataSnapshot.child("Descr").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*if(savedInstanceState!=null){
            curID=savedInstanceState.getString("curID");
            curCat=savedInstanceState.getString("curCat");
            curType=savedInstanceState.getString("curType");
        }*/


        //Set title, description and image
        /*mDataBase.child("Places").child(curCat).child(curType).child(curID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lblTitlePlace.setText(dataSnapshot.child("Name").getValue(String.class));
                lblDescr.setText(dataSnapshot.child("Descr").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }
}
/*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("curCat",curCat);
        outState.putString("curID",curID);
        outState.putString("curType",curType);
        super.onSaveInstanceState(outState);
    }

    public boolean isFav(){
        mDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String s = dataSnapshot.child("Users").child(mAuth.getCurrentUser().getUid()).child("Fav").child(curCat).child(curType).child(curID).getValue(String.class);
                if(s!=null)
                    b = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return b;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        if(isFav()){
            menu.findItem(R.id.action_addFav).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_addFav:
                DatabaseReference db = mDataBase.child("User").child(mAuth.getCurrentUser().getUid()).child(curCat).child(curType).child(curID);
                if(isFav()){
                    db.setValue(null);
                    item.setTitle(getResources().getString(R.string.notFav));
                }
                else{
                    db.setValue("normal");
                    item.setTitle(getResources().getString(R.string.inFav));
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
*/