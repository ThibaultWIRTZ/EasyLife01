package e.utilisateur.easylife01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

public class Price  extends AppCompatActivity {

    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ListView lstPrice;
    private DatabaseReference itemVal;
    private List<String> lstRef;
    private String Cat,min,max;;

    private Spinner spinPrice;
    private String typefood;


    private String lstP[] = {"0-10€","10-20€","20-30€","30-40€","40-50€","plus"};

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        spinPrice = findViewById(R.id.spinPrice);
        typefood = (String)getIntent().getSerializableExtra("typefood");
        Cat = getIntent().getStringExtra("categ");
        Toast.makeText(this,typefood,Toast.LENGTH_LONG).show();


        refreshVue();

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void refreshVue() {
        ArrayAdapter adptPrice = new ArrayAdapter<>(Price.this, android.R.layout.simple_list_item_1, lstP);
        spinPrice.setAdapter(adptPrice);
        //Go through fav and check for same "normal" in database

    }

    public void onClickContinue(View view) {
        Intent intent = new Intent(this, Resto.class);
        intent.putExtra("typefood", typefood);
        intent.putExtra("categ", Cat);

        if (findViewById(R.id.spinPrice).isEnabled()) {
            if(spinPrice.getSelectedItem().toString().equals("0-10€")){
                min="0";
                max="10";
            }
            else if(spinPrice.getSelectedItem().toString().equals("10-20€")){
                min="10";
                max="20";
            }
            else if(spinPrice.getSelectedItem().toString().equals("20-30€")){
                min="20";
                max="30";
            }
            else if(spinPrice.getSelectedItem().toString().equals("30-40€")){
                min="30";
                max="40";
            }
            else if(spinPrice.getSelectedItem().toString().equals("40-50€")){
                min="40";
                max="50";
            }
            else if(spinPrice.getSelectedItem().toString().equals("plus")){
                min="plus";
                max="plus";
            }
            intent.putExtra("min", min);
            intent.putExtra("max", max);
        }
        else{
            intent.putExtra("min", "dont mind");
        }
        startActivity(intent);
    }

    public void onClickPrice(View view) {
        spinPrice.setEnabled(true);
    }

    public void onClickDontMind(View view) {
        spinPrice.setEnabled(false);
    }
}
