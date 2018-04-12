package e.utilisateur.easylife01;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Utilisateur on 13/03/2018.
 */

public class CrtAccscr extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText edtEmail,edtPsw,edtName;
    private ProgressDialog mProgress;

    //DataBase
    private FirebaseDatabase db;
    private DatabaseReference mDataBase;

    public static final String TAG = CrtAccscr.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crtacc);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        edtEmail = findViewById(R.id.edtMail);
        edtPsw = findViewById(R.id.edtPsw);
        edtName = findViewById(R.id.edtName);

        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);

        //Get Users reference in database
        db=FirebaseDatabase.getInstance();
        mDataBase = db.getReference("Users");

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }


    public void onClickCreation(View view) {
        final String name = edtName.getText().toString().trim();
        String psw = edtPsw.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();

        if(!email.matches("") && !psw.matches("") && !name.matches("")) {

            mProgress.setMessage("Signing up...");
            mProgress.show();

            mAuth.createUserWithEmailAndPassword(email, psw)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                //Take the current user
                                FirebaseUser current_user = mAuth.getCurrentUser();

                                //Take his Id
                                DatabaseReference user_id = mDataBase.child(current_user.getUid());

                                //Store name to user ID
                                user_id.child("Name").setValue(name);

                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");

                                mProgress.dismiss();

                                createSelCat();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                mProgress.dismiss();
                                Toast.makeText(CrtAccscr.this, "Creation failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void createSelCat(){
        Intent intent = new Intent(this, Selcatscr.class);
        startActivity(intent);
    }
}
