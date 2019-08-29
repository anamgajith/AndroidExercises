package com.anamgajith.shoppinglist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 958;
    private ArrayList<String> arrayList;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                String item = data.getStringExtra("item");
                if(arrayList.isEmpty()){
                    arrayList.add(item);
                    listView.setAdapter(adapter);
                }else {
                    arrayList.add(item);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listone);
        if(savedInstanceState != null){
            arrayList = savedInstanceState.getStringArrayList("listItems");
        }else {
            arrayList = new ArrayList<>();
            listView.setAdapter(adapter);
        }
        adapter = new ArrayAdapter<>(MainActivity.this,android.R.layout.simple_list_item_1,arrayList);

        if(!arrayList.isEmpty()){
            listView.setAdapter(adapter);
        }

        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this,Main2Activity.class),REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("listItems",arrayList);
    }
}
