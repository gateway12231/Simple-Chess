package com.nwurth.simplechess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

public class PlayerScreen extends AppCompatActivity implements OnClickListener{
    private View start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_screen);
        start = (Button) findViewById(R.id.button);
        start.setOnClickListener(this);
    }

    public void onClick(View v){
        EditText p1 = (EditText) findViewById(R.id.p1);
        EditText p2 = (EditText) findViewById(R.id.p2);
        Intent game = new Intent(PlayerScreen.this,GameScreen.class);
        game.putExtra("white",p1.getText().toString());
        game.putExtra("black",p2.getText().toString());
        startActivity(game);
    }
}