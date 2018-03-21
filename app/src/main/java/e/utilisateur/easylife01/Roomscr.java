package e.utilisateur.easylife01;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by Utilisateur on 21/03/2018.
 */

public class Roomscr extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomscr);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
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
