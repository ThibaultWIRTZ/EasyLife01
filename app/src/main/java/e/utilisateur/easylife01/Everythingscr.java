package e.utilisateur.easylife01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class Everythingscr extends AppCompatActivity {

    private DatabaseReference mDataBase;
    private List<String> lst = new ArrayList<String>();
    private ListView lstAct;
    private String categAct = "University";
    private List<DatabaseReference> lstRef= new ArrayList<DatabaseReference>();
    private List<DataSnapshot> lstCat= new ArrayList<DataSnapshot>();
    private List<DataSnapshot> lstType= new ArrayList<DataSnapshot>();

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
                lstRef.clear();
                setTitle(categAct);

                if(categAct.equals("University")){
                    for(DataSnapshot name : dataSnapshot.child(categAct).getChildren()){
                        lst.add(name.getKey());
                        lstRef.add(name.getRef());
                    }
                }
                else{
                    for(DataSnapshot type : dataSnapshot.child(categAct).getChildren()){
                        for(DataSnapshot id : type.getChildren()){
                            if(Integer.parseInt(id.getKey())>0){
                                lst.add(id.child("Name").getValue(String.class));
                                lstRef.add(id.getRef());
                            }
                        }
                    }
                }


                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Everythingscr.this,android.R.layout.simple_list_item_1,lst);

                //Compare name of category with name of listView
                lstAct.setAdapter(arrayAdapter);

                lstAct.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position,
                                            long arg3)
                    {
                        Intent intent = new Intent(Everythingscr.this, Detailsscr.class);
                        intent.putExtra("ref",lstRef.get(position).toString());
                        startActivity(intent);
                        // assuming string and if you want to get the value on click of list item
                        // do what you intend to do on click of listview row

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
        // Inflate the menu_main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_everything, menu);
        return true;
    }



}
