package e.utilisateur.easylife01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Thibault WIRTZ on 13/03/2018.
 */


public class Selcatscr extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selcatscr);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_disc:
                Intent intent = new Intent(this, Logscr.class);
                startActivity(intent);
                return true;

            case R.id.action_fav:
                Intent intent01 = new Intent(this, Favscr.class);
                startActivity(intent01);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*public void onClickBaseCat(View view) {
        Intent intent = new Intent(this, Periscr.class);
        startActivity(intent);
    }*/

    public void onClickUniv(View view) {
        int choice = 1;
        Intent intent = new Intent(this, Roomscr.class);
        intent.putExtra("choice", choice);
        startActivity(intent);
    }

    public void onClickResto(View view) {
        int choice = 2;
        Intent intent = new Intent(this, Periscr.class);
        intent.putExtra("choice", choice);
        startActivity(intent);
    }

    public void onClickPubBar(View view) {
        int choice = 3;
        Intent intent = new Intent(this, Periscr.class);
        intent.putExtra("choice", choice);
        startActivity(intent);
    }

    public void onClickShop(View view) {
        int choice = 4;
        Intent intent = new Intent(this, Periscr.class);
        intent.putExtra("choice", choice);
        startActivity(intent);
    }

}
