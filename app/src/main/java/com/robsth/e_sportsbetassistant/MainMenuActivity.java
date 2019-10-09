package com.robsth.e_sportsbetassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void moveToMatchesList(View view) {
        Intent intent = new Intent(this, MatchesListActivity.class);
        startActivity(intent);
    }
}
