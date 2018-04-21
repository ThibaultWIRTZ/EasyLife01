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
    private String Cat, ID;

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

        // lstPrice = findViewById(R.id.lstCateg);

        spinPrice = findViewById(R.id.spinPrice);
        typefood = (String)getIntent().getSerializableExtra("typefood");


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

        if (findViewById(R.id.spinPrice).isEnabled()) {
            Intent intent = new Intent(this, Resto.class);
            intent.putExtra("typefood", spinPrice.getSelectedItem().toString());
            intent.putExtra("price", spinPrice.getSelectedItem().toString());
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, Resto.class);
            intent.putExtra("typefood", "dont mind");
            intent.putExtra("price", "dont mind");
            startActivity(intent);
        }


    }

    public void onClickPrice(View view) {
        findViewById(R.id.spinPrice).setEnabled(true);


    }

    public void onClickDontMind(View view) {
        findViewById(R.id.spinPrice).setEnabled(false);
    }
}
