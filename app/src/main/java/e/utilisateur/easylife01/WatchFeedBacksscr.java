package e.utilisateur.easylife01;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WatchFeedBacksscr extends AppCompatActivity{
    private String ref;
    private TextView lblDispFb,lblDispUs;
    private DatabaseReference itemRef;
    private DatabaseReference mDataBase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchfb);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        ref = getIntent().getStringExtra("ref");
        lblDispFb = findViewById(R.id.lblDispFb);
        lblDispUs = findViewById(R.id.lblDispUs);

        mDataBase = FirebaseDatabase.getInstance().getReference();
        itemRef = FirebaseDatabase.getInstance().getReferenceFromUrl(ref);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        itemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot id) {
                lblDispFb.setText(id.child("txt").getValue(String.class));
                String user = id.child("user").getValue(String.class);
                mDataBase.child("Users").child(user).child("Name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot name) {
                        String s = "From : " + name.getValue(String.class);
                        lblDispUs.setText(s);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wfb,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case(R.id.action_del):
                itemRef.setValue(null);

            case(R.id.action_store):
                itemRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot item) {
                        mDataBase.child("FeedBacks").child("Storage").child(itemRef.getKey()).setValue(item.getValue());
                        itemRef.setValue(null);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
