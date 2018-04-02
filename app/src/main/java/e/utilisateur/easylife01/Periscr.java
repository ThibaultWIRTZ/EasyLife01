package e.utilisateur.easylife01;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by Utilisateur on 21/03/2018.
 */

public class Periscr extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peri);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void onClickPeriDc(View view) {
        findViewById(R.id.edtPeri).setEnabled(false);
    }

    public void onClickPeri(View view) {
        findViewById(R.id.edtPeri).setEnabled(true);
    }
}
