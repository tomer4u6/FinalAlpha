package com.example.finalalpha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class UpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String str = item.getTitle().toString();
        Intent t;
        if(str.equals("Register")){
            t = new Intent(this,RegisterActivity.class);
            startActivity(t);
        }
        if(str.equals("Scan")){
            t = new Intent(this,ScanActivity.class);
            startActivity(t);
        }
        return super.onOptionsItemSelected(item);
    }
}
