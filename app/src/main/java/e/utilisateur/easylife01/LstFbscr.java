package e.utilisateur.easylife01;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LstFbscr extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private List<String> lstRef = new ArrayList<>();
    private List<String> lstFb = new ArrayList<>();
    private ListView lstViewFb;
    private String id;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_everything);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mDatabase = FirebaseDatabase.getInstance().getReference();

        lstViewFb = findViewById(R.id.lstCateg);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        lstViewFb.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Intent intent = new Intent(LstFbscr.this, WatchFeedBacksscr.class);
                intent.putExtra("ref",lstRef.get(position));
                startActivity(intent);
                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row

            }
        });

        mDatabase.child("FeedBacks").child("New").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot New) {
                for(DataSnapshot nb : New.getChildren()) {
                    if(!nb.getKey().equals("0")){
                        lstRef.add(nb.getRef().toString());
                        id = nb.child("user").getValue(String.class);
                        mDatabase.child("Users").child(id).child("Name").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot name) {
                                lstFb.add("From : " + name.getValue(String.class));
                                ArrayAdapter adptFb = new ArrayAdapter<String>(LstFbscr.this,android.R.layout.simple_list_item_1,lstFb);
                                lstViewFb.setAdapter(adptFb);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
