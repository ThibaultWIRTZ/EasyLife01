package e.utilisateur.easylife01;

import android.app.Dialog;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
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
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logscr);

        if (isServicesOK()) {
            edtEmail = findViewById(R.id.edtLogin);
            edtPassword = findViewById(R.id.edtPswLog);

            mAuth = FirebaseAuth.getInstance();

            mProgress = new ProgressDialog(this);

            EditText editText = (EditText) findViewById(R.id.edtLogin);
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

        }
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Logscr.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(Logscr.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
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
                                Toast.makeText(Logscr.this,"Your email or your password is wrong",Toast.LENGTH_LONG).show();
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