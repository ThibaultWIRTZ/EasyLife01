package e.utilisateur.easylife01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Feedbackscr extends AppCompatActivity {
    private DatabaseReference mDataBase,fRef;
    private EditText edtFB;
    private String txtFB, s;
    private FirebaseAuth mAuth;
    private int maxNb;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        mDataBase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        edtFB = findViewById(R.id.edtFeedBack);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void onClickBtnSend(View view){
        mDataBase.child("FeedBacks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                maxNb=0;
                for(DataSnapshot feedback : dataSnapshot.getChildren()){
                    //Look for the max value in table
                    int i = Integer.parseInt(feedback.getKey());
                    if(i>maxNb){
                        maxNb=i;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        s = String.valueOf(maxNb+1);
        //Add feedback to table
        fRef = mDataBase.child("FeedBacks").child(s);
        txtFB = edtFB.getText().toString();
        fRef.child("txt").setValue(txtFB);
        fRef.child("user").setValue(mAuth.getCurrentUser().getUid());
        Intent intent = new Intent(this,Selcatscr.class);
        startActivity(intent);
    }
}
