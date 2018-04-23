package e.utilisateur.easylife01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
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

public class Resto  extends AppCompatActivity {

    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ListView lstResto;
    private DatabaseReference itemVal;
    private List<String> lstR;
    private List<String> lstRef;
    private String Cat,typefood,price;
    private int choix,min,max;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_everything);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        lstR = new ArrayList<>();
        lstRef = new ArrayList<>();
        lstResto = findViewById(R.id.lstCateg);

        lstResto.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Intent intent = new Intent(Resto.this, Detailsscr.class);
                intent.putExtra("ref",lstRef.get(position));
                startActivity(intent);
                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row

            }
        });

        //Get every needed variable
        typefood = getIntent().getStringExtra("typefood");
        Cat = getIntent().getStringExtra("categ");
           price = getIntent().getStringExtra("min");
           if(!price.equals("dont mind") && !price.equals("plus")){
               min = Integer.parseInt(getIntent().getStringExtra("min"));
               max = Integer.parseInt(getIntent().getStringExtra("max"));
           }
        refreshVue();

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void refreshVue() {
        lstRef.clear();
        lstR.clear();

        if(!typefood.equals("dont mind")) {
            if(!price.equals("dont mind") && !Cat.equals("Shops"))
            {
                //Go through the type chosen
                mDataBase.child("Places").child(Cat).child(typefood).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot type) {

                        for (DataSnapshot id : type.getChildren()) {
                            //If between min and max
                            if (!id.getKey().equals("0")) {
                                if (price != "plus") {
                                    int low = Integer.parseInt(id.child("Prices").child("min").getValue(String.class));
                                    int big = Integer.parseInt(id.child("Prices").child("max").getValue(String.class));
                                    if ((low<=min && (min<=big && big<=max))
                                            || ((min<=low && low<=max) && (min<=big && big<=max))
                                            || ((min<=low && low<=max)&&big>=max)){
                                        lstRef.add(id.getRef().toString());
                                        lstR.add(id.child("Name").getValue(String.class));
                                    }
                                }
                                else {
                                    if (Integer.parseInt(id.child("Prices").child("min").getValue(String.class)) >= 50) {
                                        lstRef.add(id.getRef().toString());
                                        lstR.add(id.child("Name").getValue(String.class));
                                    }
                                }
                            }
                        }
                        //Adapter lstView
                        ArrayAdapter adptTypeFood = new ArrayAdapter<>(Resto.this, android.R.layout.simple_list_item_1, lstR);
                        lstResto.setAdapter(adptTypeFood);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            else
            {
                mDataBase.child("Places").child(Cat).child(typefood).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot type) {
                        for (DataSnapshot id : type.getChildren()) {
                            //Add for every thing of the category
                            if(!id.getKey().equals("0")){
                                lstRef.add(id.getRef().toString());
                                lstR.add(id.child("Name").getValue(String.class));
                            }
                        }
                        //Adapter lstView
                        ArrayAdapter adptTypeFood = new ArrayAdapter<>(Resto.this, android.R.layout.simple_list_item_1, lstR);
                        lstResto.setAdapter(adptTypeFood);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        }
        else
        {
            if(price != "dont mind")
            {
                //Go through the types
                mDataBase.child("Places").child(Cat).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot categ) {
                        for(DataSnapshot type : categ.getChildren()){
                            for (DataSnapshot id : type.getChildren()) {
                                //If between min and max
                                if (!id.getKey().equals("0")) {
                                    if (price != "plus") {
                                        int low = Integer.parseInt(id.child("Prices").child("min").getValue(String.class));
                                        int big = Integer.parseInt(id.child("Prices").child("max").getValue(String.class));
                                        if ((min <= low && (max>=low && big<=max)) || ((min>=low && big<=min) && (max>=low && big<=max)) || ((min>=low && big<=min) && max>=big)){
                                            lstRef.add(id.getRef().toString());
                                            lstR.add(id.child("Name").getValue(String.class));
                                        }
                                    }
                                    else {
                                        if (Integer.parseInt(id.child("Prices").child("min").getValue(String.class)) >= 50) {
                                            lstRef.add(id.getRef().toString());
                                            lstR.add(id.child("Name").getValue(String.class));
                                        }
                                    }
                                }
                            }
                        }

                        //Adapter lstView
                        ArrayAdapter adptTypeFood = new ArrayAdapter<>(Resto.this, android.R.layout.simple_list_item_1, lstR);
                        lstResto.setAdapter(adptTypeFood);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            else
            {
                //Go through the types
                mDataBase.child("Places").child(Cat).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot categ) {
                        for(DataSnapshot type : categ.getChildren()){
                            for (DataSnapshot id : type.getChildren()) {
                                if(!id.getKey().equals("0")){
                                    lstRef.add(id.getRef().toString());
                                    lstR.add(id.child("Name").getValue(String.class));
                                }
                            }
                        }

                        //Adapter lstView
                        ArrayAdapter adptTypeFood = new ArrayAdapter<>(Resto.this, android.R.layout.simple_list_item_1, lstR);
                        lstResto.setAdapter(adptTypeFood);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    }
}
