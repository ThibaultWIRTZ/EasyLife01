package e.utilisateur.easylife01;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Everythingscr extends AppCompatActivity {

    private DatabaseReference mDataBase;
    private List<String> lst = new ArrayList<String>();
    private ListView lstAct;
    private String categAct = "University";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_everything);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mDataBase = FirebaseDatabase.getInstance().getReference();
        lstAct = findViewById(R.id.lstCateg);


        if (savedInstanceState != null) {
            categAct = savedInstanceState.getString("cat");
        }

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        refreshVue();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("cat",categAct);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_Univ:
                categAct="University";
                refreshVue();
                return true;

            case R.id.action_Rest:
                categAct="Restaurants";
                refreshVue();
                return true;

            case R.id.action_PnB:
                categAct="PnB";
                refreshVue();
                return true;

            case R.id.action_Shops:
                categAct="Shops";
                refreshVue();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void refreshVue(){
        //Go through table places
        mDataBase.child("Places").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //First clear elements of the list
                lst.clear();
                setTitle(categAct);

                if(categAct.equals("University")){
                    for(DataSnapshot name : dataSnapshot.child(categAct).getChildren()){
                        lst.add(name.getKey());
                    }
                }
                else{
                    for(DataSnapshot type : dataSnapshot.child(categAct).getChildren()){
                        for(DataSnapshot id : type.getChildren()){
                            lst.add(id.child("Name").getValue(String.class));
                        }
                    }
                }


                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Everythingscr.this,android.R.layout.simple_list_item_1,lst);

                //Compare name of category with name of listView
                lstAct.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_everything, menu);
        return true;
    }



}
