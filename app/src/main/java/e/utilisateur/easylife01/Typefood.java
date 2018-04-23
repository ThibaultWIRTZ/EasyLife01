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
import android.widget.TextView;

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
 * Created by esthe on 02/04/2018.
 */

public class Typefood extends AppCompatActivity {

    private DatabaseReference mDataBase;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference itemVal;
    private List<String> lstTF;
    private List<String> lstRef;
    private String Cat;
    private TextView lblTitle;

    private Spinner spinTypeFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typefood);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        lstTF = new ArrayList<>();
        lstRef = new ArrayList<>();

        Cat = getIntent().getStringExtra("choice");
        lblTitle = findViewById(R.id.lblFilter);
        String s = lblTitle.getText()+" " + Cat;
        lblTitle.setText(s);

        s = getTitle() + " " + Cat;
        setTitle(s);

        spinTypeFood = findViewById(R.id.spinTypeFood);

        refreshVue();


        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void refreshVue() {
        lstRef.clear();
        lstTF.clear();
        //Go through fav and check for same "normal" in database
        mDataBase.child("Places").child(Cat).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot food) {
                //Find category
                for (DataSnapshot type : food.getChildren()) {
                    //Find type

                    //DatabaseReference dbr = mDataBase.child("Places").child("Restaurant").child(type.getKey());
                    lstRef.add(type.getRef().toString());
                    lstTF.add(type.getKey());

                    //Adapteur lstView
                    ArrayAdapter adptTypeFood = new ArrayAdapter<>(Typefood.this, android.R.layout.simple_list_item_1, lstTF);
                    spinTypeFood.setAdapter(adptTypeFood);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onClickContinue(View view) {
        Class classe;
        if(!Cat.equals("Shops")){
            classe = Price.class;
        }
        else
        {
            classe = Resto.class;
        }

        Intent intent=new Intent(this, classe);
        intent.putExtra("categ",Cat);
        if(Cat.equals("Shops")){
            intent.putExtra("min","plus");
        }
        if (findViewById(R.id.spinTypeFood).isEnabled()) {
            intent.putExtra("typefood", spinTypeFood.getSelectedItem().toString());
        }
        else {
            intent.putExtra("typefood", "dont mind");
        }
        startActivity(intent);
    }

    public void onClickTypeFood(View view) {
        spinTypeFood.setEnabled(true);
    }

    public void onClickDontMind(View view) {
        spinTypeFood.setEnabled(false);
    }

}