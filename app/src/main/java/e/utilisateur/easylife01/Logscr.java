package e.utilisateur.easylife01;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

/**
 * Created by Utilisateur on 13/03/2018.
 */

public class Logscr extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText edtEmail,edtPassword;
    private String email,password;
    public static final String TAG = CrtAccscr.class.getSimpleName();

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logscr);

        edtEmail = findViewById(R.id.edtLogin);
        edtPassword = findViewById(R.id.edtPswLog);

        mAuth = FirebaseAuth.getInstance();

        mProgress = new ProgressDialog(this);

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
        email = edtEmail.getText().toString().trim();
        password = edtPassword.getText().toString().trim();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            mProgress.setMessage("Signing in...");
            mProgress.show();

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                mProgress.dismiss();
                                createLog();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                mProgress.dismiss();
                            }
                        }
                    });
        }
        else{
            testEdt();
        }
    }

    public void testEdt(){
     if(TextUtils.isEmpty(email)){
         Toast.makeText(Logscr.this, "Enter an email",
                 Toast.LENGTH_SHORT).show();
     }
     else if(!email.contains("@")){
         Toast.makeText(Logscr.this, "Enter a valid E-mail",
                 Toast.LENGTH_SHORT).show();
     }
     else if(TextUtils.isEmpty(password)){
         Toast.makeText(Logscr.this, "Enter a password",
                 Toast.LENGTH_SHORT).show();
     }
    }

    public void createLog(){
        Intent intent = new Intent(this, Selcatscr.class);
        startActivity(intent);
    }
}