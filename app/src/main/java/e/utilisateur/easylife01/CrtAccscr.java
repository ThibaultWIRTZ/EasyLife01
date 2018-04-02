package e.utilisateur.easylife01;

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

/**
 * Created by Utilisateur on 13/03/2018.
 */

public class CrtAccscr extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email,password;
    public static final String TAG = CrtAccscr.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crtacc);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        email = findViewById(R.id.edtMail);
        password = findViewById(R.id.edtPsw);
        mAuth = FirebaseAuth.getInstance();

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }


    public void onClickCreation(View view) {
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            createSelCat();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CrtAccscr.this, "Creation failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void createSelCat(){
        Intent intent = new Intent(this, Selcatscr.class);
        intent.putExtra("usr",email.getText().toString());
        startActivity(intent);
    }
}
