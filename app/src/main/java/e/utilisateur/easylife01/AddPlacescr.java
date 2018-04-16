package e.utilisateur.easylife01;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddPlacescr extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String lstDay[] = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    private List<String> lstHours = new ArrayList<>();
    private List<String> lstCateg = new ArrayList<>();
    private Spinner spiCateg, spiType;
    private List<Spinner> lstSpiOpen = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addplace);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        spiCateg = findViewById(R.id.spiCateg);
        spiType = findViewById(R.id.spiType);

        spiType.setEnabled(false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

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

        ArrayAdapter<String> adptHours = new ArrayAdapter<String>(AddPlacescr.this,android.R.layout.simple_list_item_1,lstHours);

        //Add lists to open spinner and disable close spinner
        for(int i=0;i<7;i++){
            String so = "spiOpen" + lstDay[i];
            int m = getResources().getIdentifier(so,"id",getPackageName());
            Spinner sp = findViewById(m);
            sp.setAdapter(adptHours);
            lstSpiOpen.add(sp);

            String sc ="spiClose" + lstDay[i];
            m = getResources().getIdentifier(sc,"id",getPackageName());
            sp = findViewById(m);
            sp.setEnabled(false);
        }

        lstCateg.add("");
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

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        for(int i=0;i<7;i++){
            String s = getResources().getResourceName(lstSpiOpen.get(i).getId());
            s=s.replace("Open","Close");
            int j = getResources().getIdentifier(s,"id",getPackageName());
            Spinner sp = findViewById(j);
            if(lstSpiOpen.get(i).getPrompt()=="closed"){
                sp.setEnabled(false);
            }
            else{
                sp.setEnabled(true);
            }
        }
    }
}
