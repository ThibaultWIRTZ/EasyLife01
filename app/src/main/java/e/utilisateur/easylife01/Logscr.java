package e.utilisateur.easylife01;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;
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

public class Logscr extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email,password;
    public static final String TAG = CrtAccscr.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logscr);

        email = findViewById(R.id.edtLogin);
        password = findViewById(R.id.edtPswLog);
        mAuth = FirebaseAuth.getInstance();

        EditText editText = (EditText) findViewById(R.id.edtLogin);
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public void onClickCrtAcc(View view) {
        Intent intent = new Intent(this, CrtAccscr.class);
        startActivity(intent);
    }


    public void onClickCatSel(View view) {
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            createLog();
                        } else
                            {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Logscr.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void createLog(){
        Intent intent = new Intent(this, Selcatscr.class);
        startActivity(intent);
    }
}