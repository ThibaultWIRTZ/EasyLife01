package e.utilisateur.easylife01;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Utilisateur on 13/03/2018.
 */

public class Logscr extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logscr);

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
        Intent intent = new Intent(this, Selcatscr.class);
        startActivity(intent);
    }
}