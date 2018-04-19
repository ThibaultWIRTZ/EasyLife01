package e.utilisateur.easylife01;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Utilisateur on 21/03/2018.
 */

public class Periscr extends AppCompatActivity {

    private int choice;
    private EditText edtPeri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peri);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        choice = (int)getIntent().getSerializableExtra("choice");
        edtPeri = findViewById(R.id.edtPeri);
        findViewById(R.id.txterror).setVisibility(View.INVISIBLE);

    }
    public void onClickPeriDc(View view) {
        findViewById(R.id.edtPeri).setEnabled(false);
    }

    public void onClickPeri(View view) {
        findViewById(R.id.edtPeri).setEnabled(true);
    }

    public void onClickContinue(View view){

        /*if(edtPeri.getText().toString().equals("") && (findViewById(R.id.edtPeri).isEnabled()))
        {
            findViewById(R.id.txterror).setVisibility(View.VISIBLE);
        }
        else {

            if (choice == 2) {
                Intent intent = new Intent(this, Typefood.class);
                startActivity(intent);
            } else if (choice == 3) {
                Intent intent = new Intent(this, CrtAccscr.class);
                startActivity(intent);
            } else if (choice == 4) {
                Intent intent = new Intent(this, CrtAccscr.class);
                startActivity(intent);
            }
        }*/
    }


}
