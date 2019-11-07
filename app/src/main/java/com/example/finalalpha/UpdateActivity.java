package com.example.finalalpha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.utilities.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class UpdateActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    EditText et_plainText;
    ListView lv_database;
    ArrayList<String> stringList = new ArrayList<String>();
    ArrayAdapter<String> adapter;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Alpha");
    ValueEventListener strListener;

    AlertDialog.Builder adb;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);


        et_plainText = (EditText)findViewById(R.id.et_plainText);
        lv_database = (ListView)findViewById(R.id.lv_database);

        lv_database.setOnItemClickListener(this);
        lv_database.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,stringList);
        lv_database.setAdapter(adapter);

        strListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stringList.clear();
                for(DataSnapshot childSnapShot : dataSnapshot.getChildren()) {

                    String value = childSnapShot.getValue(String.class);

                    stringList.add(value);
                }
                adapter = new ArrayAdapter<String>(UpdateActivity.this, R.layout.support_simple_spinner_dropdown_item,stringList);
                lv_database.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        myRef.addValueEventListener(strListener);

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

        if(strListener!=null){
            myRef.removeEventListener(strListener);
        }

        if(str.equals("Register User")){
            t = new Intent(this,RegisterActivity.class);
            startActivity(t);
        }
        if(str.equals("Scan NFC")){
            t = new Intent(this,ScanActivity.class);
            startActivity(t);
        }
        return super.onOptionsItemSelected(item);
    }

    public void AddTextToFB(View view) {
        if(!validateForm()){
            return;
        }


        if(et_plainText.getText().toString().equals("/")){
            et_plainText.setError("Invalid Text.");
        }
        else {
            try {
                String str = et_plainText.getText().toString();
                myRef = database.getReference("Alpha").child(str);
                myRef.setValue(str);
                Toast.makeText(this, "Writing succeeded.", Toast.LENGTH_SHORT).show();
            } catch (Exception exception) {
                et_plainText.setError("Invalid Text.");
            }
        }
        et_plainText.setText("");
    }

    private boolean validateForm() {
        //Validate if text is not missing

        boolean valid = true;

        String plainText = et_plainText.getText().toString();
        if (TextUtils.isEmpty(plainText)) {
            et_plainText.setError("Required.");
            valid = false;
        } else {
            et_plainText.setError(null);
        }

        return valid;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Confirm deleting value from Firebase");
        adb.setMessage("Please confirm deleting process from the database:");
        adb.setCancelable(false);
        adb.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str = stringList.get(position);
                myRef = database.getReference("Alpha").child(str);
                myRef.removeValue();

                Toast.makeText(UpdateActivity.this, "Deleting succeeded.", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        adb.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog ad = adb.create();
        ad.show();

    }
}
