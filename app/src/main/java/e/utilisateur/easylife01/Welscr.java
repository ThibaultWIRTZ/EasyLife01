package e.utilisateur.easylife01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class Welscr extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welscr);
    }

    public void goToLogScr (View view){
        Intent intent = new Intent (this, Logscr.class);
        startActivity(intent);
    }
}
