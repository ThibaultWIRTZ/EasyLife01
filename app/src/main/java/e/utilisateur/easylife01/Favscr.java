package e.utilisateur.easylife01;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Utilisateur on 21/03/2018.
 */

public class Favscr extends AppCompatActivity {

    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ListView lstFav;
    private DatabaseReference itemVal;
    private List<String> lstf;
    private List<String> lstRef;
    private String Cat,ID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_everything);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        lstf = new ArrayList<>();
        lstRef = new ArrayList<>();

        lstFav = findViewById(R.id.lstCateg);

        lstFav.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Intent intent = new Intent(Favscr.this, Detailsscr.class);
                intent.putExtra("ref",lstRef.get(position));
                startActivity(intent);
                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row

            }
        });

        refreshVue();

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void refreshVue(){
        lstRef.clear();
        lstf.clear();
        //Go through fav and check for same "normal" in database
        mDataBase.child("Users").child(user.getUid()).child("Fav").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot fav) {
                //Find category
                for(DataSnapshot categ : fav.getChildren()){
                    //Find type
                    if(categ.getKey().equals("University")){
                        for(DataSnapshot name : categ.getChildren()){
                            String dbr = mDataBase.child("Places").child(categ.getKey()).child(name.getKey()).getRef().toString();
                            lstRef.add(dbr);
                            lstf.add(name.getKey());
                        }
                        //Adapteur lstView
                        ArrayAdapter adptFav = new ArrayAdapter<>(Favscr.this, android.R.layout.simple_list_item_1,lstf);
                        lstFav.setAdapter(adptFav);
                    }
                    else{
                        for(DataSnapshot type : categ.getChildren()){
                            for(DataSnapshot id : type.getChildren()){
                                DatabaseReference dbr = mDataBase.child("Places").child(categ.getKey()).child(type.getKey()).child(id.getKey());

                                //Get the reference for ID
                                dbr.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        lstf.add(dataSnapshot.child("Name").getValue(String.class));
                                        lstRef.add(dataSnapshot.getRef().toString());
                                        //Adapteur lstView
                                        ArrayAdapter adptFav = new ArrayAdapter<>(Favscr.this, android.R.layout.simple_list_item_1,lstf);
                                        lstFav.setAdapter(adptFav);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
