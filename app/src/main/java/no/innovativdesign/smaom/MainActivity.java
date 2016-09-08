package no.innovativdesign.smaom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import no.innovativdesign.smaom.R;


public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}