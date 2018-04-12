package e.utilisateur.easylife01;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Utilisateur on 21/03/2018.
 */

public class Roomscr extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Spinner spiBuild;

    private DatabaseReference mDataBase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomscr);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        findViewById(R.id.spiBuild).setEnabled(false);

        mAuth = FirebaseAuth.getInstance();
        spiBuild = findViewById(R.id.spiBuild);
        mDataBase = FirebaseDatabase.getInstance().getReference();

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // Read buildings from University
        mDataBase.child("University").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                final List<String> buildings = new ArrayList<String>();

                //Go through the table University
                for (DataSnapshot buildingsSnapshot: dataSnapshot.getChildren()) {
                    String building = buildingsSnapshot.getKey();
                    buildings.add(building);
                }

                //Adapt to spinner
                ArrayAdapter<String> buildingsAdapter = new ArrayAdapter<String>(Roomscr.this, android.R.layout.simple_spinner_item, buildings);
                buildingsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spiBuild.setAdapter(buildingsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                TextView t = findViewById(R.id.lblSelRoom);
                t.setText("error");
            }
        });
    }
    public void onClickBuild(View view) {
        findViewById(R.id.spiBuild).setEnabled(true);
        findViewById(R.id.edtRoom).setEnabled(false);
    }

    public void onClickRoom(View view) {
        findViewById(R.id.spiBuild).setEnabled(false);
        findViewById(R.id.edtRoom).setEnabled(true);
    }

    public void onClickFindRoom(View view) {

    }
}
