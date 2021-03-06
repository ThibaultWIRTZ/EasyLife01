package e.utilisateur.easylife01;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Detailsscr extends AppCompatActivity {
    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    private String curID, curItem, curCat, curType, address;
    private boolean b = false;
    private TextView lblDescr, lblTitlePlace, lblPrice, lblSched, lblAddress;
    private DatabaseReference itemRef;
    private String lstDay[] = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    private Object itemValue;
    private Menu menu;
    private ImageView imgPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        lblDescr = findViewById(R.id.lblDescription);
        lblTitlePlace = findViewById(R.id.lblTitlePlace);
        lblPrice=findViewById(R.id.lblPrice);
        lblSched=findViewById(R.id.lblSched);
        lblAddress=findViewById(R.id.lblAddress);

        imgPlace=findViewById(R.id.imgPlace);

        curItem = getIntent().getStringExtra("ref");

        itemRef = FirebaseDatabase.getInstance().getReferenceFromUrl(curItem);


        if(itemRef.getParent().getKey().equals("University")){
            String s = itemRef.getKey();
            s = s.replace("%20"," ");

            mDataBase.child("Places").child("University").child(s).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot name) {
                    lblTitlePlace.setText(name.getKey());
                    address = name.child("Address").getValue(String.class);
                    lblAddress.setText(address);
                    lblDescr.setText(name.child("Descr").getValue(String.class));
                    lblPrice.setVisibility(View.GONE);
                    lblSched.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        else {
            itemRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!itemRef.getParent().getParent().getKey().equals("Shops")) {
                        String price = "Approx. price : " + dataSnapshot.child("Prices").child("min").getValue(String.class) + " - " + dataSnapshot.child("Prices").child("max").getValue(String.class) + "€";
                        lblPrice.setText(price);
                    } else {
                        lblPrice.setVisibility(View.GONE);
                    }
                    //Go through the element schedule
                    String sched = "";
                    for (int i = 0; i < 7; i++) {
                        if (dataSnapshot.child("Sched").child(String.valueOf(i)).child("Open").getValue(String.class).equals("closed")) {
                            sched += lstDay[i] + " : " + dataSnapshot.child("Sched").child(String.valueOf(i)).child("Open").getValue(String.class);
                        } else {
                            sched += lstDay[i] + " : " + dataSnapshot.child("Sched").child(String.valueOf(i)).child("Open").getValue(String.class) + " - " + dataSnapshot.child("Sched").child(String.valueOf(i)).child("Close").getValue(String.class);
                        }
                        if (i != 6) {
                            sched += "\n";
                        }
                    }
                    imgPlace.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    String s = dataSnapshot.child("PicUrl").getValue(String.class);
                    Picasso.with(Detailsscr.this).load(s).placeholder(R.mipmap.ic_launcher)
                            .error(R.mipmap.ic_launcher)
                            .into(imgPlace, new com.squareup.picasso.Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {

                                }
                            });


                    lblSched.setText(sched);
                    lblAddress.setText(dataSnapshot.child("Address").getValue(String.class));
                    lblTitlePlace.setText(dataSnapshot.child("Name").getValue(String.class));
                    lblDescr.setText(dataSnapshot.child("Descr").getValue(String.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        }

        isFav();
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void isFav(){
        //Check if fav
        mDataBase.child("Users").child(mAuth.getCurrentUser().getUid()).child("Fav").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String s = itemRef.getKey().replace("%20"," ");
                    if(itemRef.getParent().getKey().equals("University")){
                        if(dataSnapshot.child(itemRef.getParent().getKey()).child(s).getValue(String.class) != null){
                            b=true;
                        }
                        else{
                            b=false;
                        }
                    }
                    else{
                        if(dataSnapshot.child(itemRef.getParent().getParent().getKey()).child(itemRef.getParent().getKey()).child(itemRef.getKey()).getValue(String.class) != null){
                            b=true;
                        }
                        else{
                            b=false;
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        this.menu=menu;
        if(b==false){
            menu.findItem(R.id.action_addFav).setTitle(getResources().getString(R.string.notFav));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_addFav:
                String s;
                DatabaseReference db = mDataBase.child("Users").child(mAuth.getCurrentUser().getUid()).child("Fav");
                s = itemRef.getKey().replace("%20"," ");
                if(itemRef.getParent().getKey().equals("University")){
                    db = db.child(itemRef.getParent().getKey()).child(s);
                }
                else{
                    db =  db.child(itemRef.getParent().getParent().getKey()).child(itemRef.getParent().getKey()).child(s);
                }

                MenuItem it =menu.findItem(R.id.action_addFav);

                if(b==true){
                    db.setValue(null);
                    b=false;
                    it.setTitle(getResources().getString(R.string.notFav));
                }
                else{
                    b=true;
                    db.setValue("normal");
                    it.setTitle(getResources().getString(R.string.inFav));
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickDirection(View view) {
        if(itemRef.getParent().getKey().equals("University")){
            Intent map = new Intent(Detailsscr.this,MapsActivity.class);
            map.putExtra("ad",address);
            startActivity(map);
        }
        else{
            itemRef.child("Address").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot ad) {
                    Intent map = new Intent(Detailsscr.this,MapsActivity.class);
                    map.putExtra("ad",ad.getValue(String.class));
                    startActivity(map);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
