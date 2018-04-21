package e.utilisateur.easylife01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

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
    private String Cat, ID;

    private Spinner spinPrice;
    private String typefood;
    private String price;
    private int min;
    private int choix;

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


        typefood = (String)getIntent().getSerializableExtra("typefood");
        price = (String)getIntent().getSerializableExtra("price");

        switch (price)
        {
            case "0-10€":
                choix = 0;
                break;
            case "10-20€" :
                choix = 1;
                break;
            case "20-30€" :
                choix = 2;
                break;
            case "30-40€" :
                choix = 3;
                break;
            case "40-50€" :
                choix = 4;
                break;
            case "plus" :
                choix = 5;
                break;
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
        //Go through fav and check for same "normal" in database


        if(typefood != "dont mind") {
            if(price != "dont mind")
            {
                mDataBase.child("Places").child("Restaurants").child(typefood).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot resto) {

                        for (DataSnapshot r : resto.getChildren()) {

                            for (DataSnapshot range : r.getChildren()) {

                                //Find type

                                min = (int)range.child("Price").child("min").getValue();

                                switch (choix)
                                {
                                    case 0:
                                        if(min>=0 && min<=10)
                                        {
                                            lstR.add(range.child("Name").getValue().toString());
                                        }
                                        break;
                                    case 1:
                                        if(min>=10 && min<=20)
                                        {
                                            lstR.add(range.child("Name").getValue().toString());
                                        }
                                        break;
                                    case 2:
                                        if(min>=20 && min<=30)
                                        {
                                            lstR.add(range.child("Name").getValue().toString());
                                        }
                                        break;
                                    case 3:
                                        if(min>=30 && min<=40)
                                        {
                                            lstR.add(range.child("Name").getValue().toString());
                                        }
                                        break;
                                    case 4:
                                        if(min>=40 && min<=50)
                                        {
                                            lstR.add(range.child("Name").getValue().toString());
                                        }
                                        break;
                                    case 5:
                                        if(min >= 50)
                                        {
                                            lstR.add(range.child("Name").getValue().toString());
                                        }
                                        break;
                                }
                                //DatabaseReference dbr = mDataBase.child("Places").child("Restaurant").child(type.getKey());
                                lstRef.add(range.getRef().toString());
                                lstR.add(range.getKey());

                                //Adapteur lstView
                                ArrayAdapter adptTypeFood = new ArrayAdapter<>(Resto.this, android.R.layout.simple_list_item_1, lstR);
                                spinPrice.setAdapter(adptTypeFood);
                            }

                        }
                        //Find category

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
            else
            {
                mDataBase.child("Places").child("Restaurants").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot resto) {

                        for(DataSnapshot categ : resto.getChildren()){
                            for(DataSnapshot nb : categ.getChildren()){
                                //if(nb.child()) != "nullItem")
                                //{
                                for(DataSnapshot name : nb.getChildren()) {
                                    //if (nb.getKey() != "0") {
                                    lstR.add(name.child("Name").getValue(String.class));
                                    lstRef.add(name.getRef().toString());
                                    //Adapteur lstView
                                    ArrayAdapter adptResto = new ArrayAdapter<>(Resto.this, android.R.layout.simple_list_item_1, lstR);
                                    lstResto.setAdapter(adptResto);
                                    //}
                                }
                                //}
                            }
                        }

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
                mDataBase.child("Places").child("Restaurants").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot tf) {
                        for (DataSnapshot resto : tf.getChildren()) {

                            for (DataSnapshot r : resto.getChildren()) {

                                for (DataSnapshot range : r.getChildren()) {

                                    //Find type
                                    //DatabaseReference dbr = mDataBase.child("Places").child("Restaurant").child(type.getKey());
                                    lstRef.add(range.getRef().toString());
                                    lstR.add(range.getKey());

                                    //Adapteur lstView
                                    ArrayAdapter adptTypeFood = new ArrayAdapter<>(Resto.this, android.R.layout.simple_list_item_1, lstR);
                                    spinPrice.setAdapter(adptTypeFood);
                                }

                            }
                            //Find category
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            else
            {
                mDataBase.child("Places").child("Restaurants").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot resto) {

                        for(DataSnapshot categ : resto.getChildren()){
                            for(DataSnapshot nb : categ.getChildren()){
                                //if(nb.child()) != "nullItem")
                                //{
                                for(DataSnapshot name : nb.getChildren()) {
                                    //if (nb.getKey() != "0") {
                                    lstR.add(name.child("Name").getValue(String.class));
                                    lstRef.add(name.getRef().toString());
                                    //Adapteur lstView
                                    ArrayAdapter adptResto = new ArrayAdapter<>(Resto.this, android.R.layout.simple_list_item_1, lstR);
                                    lstResto.setAdapter(adptResto);
                                    //}
                                }
                                //}
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

        }
    }
}
