package e.utilisateur.easylife01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddPlacescr extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String lstDay[] = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    private List<String> lstHours = new ArrayList<>();
    private List<String> lstCateg = new ArrayList<>();
    private Spinner spiCateg, spiType;
    private List<Spinner> lstSpiOpen = new ArrayList<>();
    private Spinner sp;
    private EditText edtName,edtDescr,edtMax,edtMin;
    private int biggestID=0;
    private String userAdmin;
    private LinearLayout linPrice;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addplace);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        spiCateg = findViewById(R.id.spiCateg);
        spiType = findViewById(R.id.spiType);
        edtName = findViewById(R.id.edtName);
        edtDescr = findViewById(R.id.edtDescr);
        edtMax = findViewById(R.id.edtMaxPrice);
        edtMin = findViewById(R.id.edtMinPrice);
        linPrice = findViewById(R.id.linLayPrice);

        mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).child("isAdmin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userAdmin = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        lstCateg.add("Select a category");
        //Go through the categories
        mDatabase.child("Places").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot categ : dataSnapshot.getChildren()){
                    if(!categ.getKey().equals("University")){
                        lstCateg.add(categ.getKey());
                        ArrayAdapter<String> adptCateg = new ArrayAdapter<String>(AddPlacescr.this,android.R.layout.simple_list_item_1,lstCateg);
                        spiCateg.setAdapter(adptCateg);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        spiCateg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(parentView.getItemAtPosition(position).toString()=="Select a category"){
                    spiType.setEnabled(false);
                }
                else{
                    if(parentView.getItemAtPosition(position).toString().equals("Shops")){
                        linPrice.setVisibility(View.GONE);
                    }
                    else{
                        linPrice.setVisibility(View.VISIBLE);
                    }
                    spiType.setEnabled(true);
                    mDatabase.child("Places").child(spiCateg.getSelectedItem().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<String> lstType = new ArrayList<>();
                            for(DataSnapshot type : dataSnapshot.getChildren()){
                                lstType.add(type.getKey());
                            }
                            ArrayAdapter<String> adptType = new ArrayAdapter<>(AddPlacescr.this,android.R.layout.simple_list_item_1,lstType);
                            spiType.setAdapter(adptType);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        spiType.setEnabled(false);

        lstHours.add("closed");
        String min = "00";
        String morAft = "am";
        //Add every hours to lst hours
        for(int i=0, h=0;i<48;i++){
            if(i % 2 == 0){
                h++;
                min="00";
            }
            if(i % 24 == 0 && i!=0){
                h=0;
                morAft = "pm";
            }
            String s = h + ":" + min + " " + morAft;
            lstHours.add(s);
            min="30";
        }

        final ArrayAdapter<String> adptHours = new ArrayAdapter<String>(AddPlacescr.this,android.R.layout.simple_list_item_1,lstHours);

        //Add lists to open spinner and disable close spinner
        for(int i=0;i<7;i++){
            String so = "spiOpen" + lstDay[i];
            int m = getResources().getIdentifier(so,"id",getPackageName());
            sp = findViewById(m);
            sp.setAdapter(adptHours);

            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    //Get open spinner name
                    String s = getResources().getResourceName(parentView.getId());
                    s=s.replace("spiOpen","spiClose");

                    //Get close spinner
                    int spiID = getResources().getIdentifier(s,"id",getPackageName());
                    Spinner tempSpi = findViewById(spiID);

                    if(parentView.getItemAtPosition(position).toString().equals("closed")){
                        tempSpi.setEnabled(false);
                    }
                    else{
                        tempSpi.setEnabled(true);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // your code here
                }

            });

            lstSpiOpen.add(sp);

            String sc ="spiClose" + lstDay[i];
            m = getResources().getIdentifier(sc,"id",getPackageName());
            sp = findViewById(m);
            sp.setAdapter(adptHours);
            sp.setEnabled(false);
        }

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }


    public void onClickSend(View view) {
        if(isOk().equals("no")){

            String branch;
            if(isAdmin()){
                branch = "Places";
            }
            else {
                branch = "Requests";
            }

            //Check ID
            mDatabase.child(branch).child(spiCateg.getSelectedItem().toString()).child(spiType.getSelectedItem().toString()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot id : dataSnapshot.getChildren()){
                        if(biggestID<Integer.parseInt(id.getKey())){
                            biggestID=Integer.parseInt(id.getKey());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            DatabaseReference newPlace = mDatabase.child(branch).child(spiCateg.getSelectedItem().toString()).child(spiType.getSelectedItem().toString()).child(String.valueOf(biggestID+1));
            newPlace.child("Name").setValue(edtName.getText().toString());
            newPlace.child("Descr").setValue(edtDescr.getText().toString());

            for(int i=0 ; i<7 ; i++){
                //Opening hours
                String selItem = lstSpiOpen.get(i).getSelectedItem().toString();
                DatabaseReference sched = newPlace.child("Schedule").child(String.valueOf(i));
                sched.child("Open").setValue(selItem);

                if(selItem!="closed"){
                    //Closing hours
                    int idSpi = getResources().getIdentifier("spiClose"+lstDay[i],"id",getPackageName());
                    Spinner sp = findViewById(idSpi);
                    sched.child("Close").setValue(sp.getSelectedItem().toString());
                }
            }

            if(!spiCateg.getSelectedItem().toString().equals("Shops")){
                newPlace.child("Price").child("max").setValue(edtMax.getText().toString());
                newPlace.child("Price").child("min").setValue(edtMin.getText().toString());
            }

            Intent intent = new Intent(this, Selcatscr.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(AddPlacescr.this,isOk(),Toast.LENGTH_LONG).show();
        }
    }

    public boolean isAdmin(){
        if(userAdmin.equals("yes")){
            return true;
        }
        return false;
    }

    public String isOk(){
        int ctrOpen=0, ctrClose=0;

        for(int i=0;i<7;i++){
            Spinner tempSpiOpen = findViewById(getResources().getIdentifier("spiOpen"+lstDay[i],"id",getPackageName()));
            if(tempSpiOpen.getSelectedItem().toString().equals("closed")){
                ctrOpen++;
            }
            Spinner tempSpiClose = findViewById(getResources().getIdentifier("spiClose"+lstDay[i],"id",getPackageName()));
            if(tempSpiClose.getSelectedItem().toString().equals("closed")){
                ctrClose++;
            }
            if(!tempSpiOpen.getSelectedItem().toString().equals("closed") && tempSpiClose.getSelectedItem().toString().equals("closed")){
                return "This place have to close on " + lstDay[i];
            }
        }

        if(spiCateg.getSelectedItem().toString().equals("Select a category")){
            return "You must select a category";
        }
        else if(TextUtils.isEmpty(edtName.getText())){
            return "You must enter a name";
        }
        else if(TextUtils.isEmpty(edtDescr.getText())){
            return "You must enter a description";
        }
        else if(ctrOpen==7){
            return "This place must open at certain hours, you must enter it";
        }
        else if(ctrClose==7){
            return "This place need close hours";
        }
        else if(!spiCateg.getSelectedItem().toString().equals("Shops")) {
            if (TextUtils.isEmpty(edtMax.getText().toString())) {
                return "You must enter a max price";
            } else if (TextUtils.isEmpty(edtMin.getText().toString())) {
                return "You must enter a max price";
            }
            else if(Integer.parseInt(edtMin.getText().toString())>Integer.parseInt(edtMax.getText().toString())){
                return  "Max must be bigger than min";
            }
        }

        return "no";
    }
}
