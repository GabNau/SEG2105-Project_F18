package com.example.gaba.intelligence_homeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Service_providers_list extends AppCompatActivity{
    ArrayList<User> serviceProvidersList = new ArrayList<>();
    ListView listView;
    ArrayAdapter<User> adapter;
    MyDBHandler dbHandler;

    Button btnPickTimes;
    String serviceName;
    String username;

    SeekBar minRating;
    SeekBar maxRating;

    int minFilter = 0;
    int maxFilter = 5;

    int minTime = 0;
    int maxTime = 24;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_providers_list);
        serviceName = getIntent().getStringExtra("serviceName");
        btnPickTimes = (Button) findViewById(R.id.pickTimes);
        minRating = (SeekBar) findViewById(R.id.barMin);
        maxRating = (SeekBar) findViewById(R.id.barMax);
        // Get ListView object from xml layout
        listView = findViewById(R.id.servProv);
        //For each item in database, add to serviceList
        dbHandler = new MyDBHandler(this);
        serviceProvidersList =  (dbHandler.getServiceProviders(serviceName));
        //Create an ArrayAdapter and Set it on the ListView
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, serviceProvidersList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), ((TextView) view).getText(),Toast.LENGTH_SHORT).show();
                username = ((TextView) view).getText().toString();

                Intent intent = new Intent(getApplicationContext(), ViewBookRate_ServiceProvider.class);
                intent.putExtra("serviceName", serviceName);
                intent.putExtra("username", username);
                intent.putExtra("accountOwner", getIntent().getStringExtra("username"));
                startActivityForResult(intent, 0);

            }
        });

    }

    public void viewProvider(View view){
        Intent intent = new Intent(getApplicationContext(), ViewBookRate_ServiceProvider.class);
        intent.putExtra("serviceName", serviceName);
        startActivityForResult(intent, 0);
    }

    public void viewRating(View view){

        //showRatingSelectDialog();
        findViewById(R.id.filter_options).setVisibility(View.GONE);
        findViewById(R.id.service_providers_list).setVisibility(View.GONE);
        findViewById(R.id.rating_options).setVisibility(View.VISIBLE);


    }
    public void updateRatingFilter(){
        minFilter = minRating.getProgress();
        maxFilter = maxRating.getProgress();

        serviceProvidersList = dbHandler.getServiceProviders_rating(minFilter,maxFilter, dbHandler.getServiceProviders_time(minTime,maxTime,dbHandler.getServiceProviders(serviceName)));
        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, serviceProvidersList);
        listView.setAdapter(new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, serviceProvidersList));

        findViewById(R.id.rating_options).setVisibility(View.GONE);
        findViewById(R.id.filter_options).setVisibility(View.VISIBLE);
        findViewById(R.id.service_providers_list).setVisibility(View.VISIBLE);

    }

    public void updateRatingFilter(View view){
        minFilter = minRating.getProgress();
        maxFilter = maxRating.getProgress();

        serviceProvidersList = dbHandler.getServiceProviders_rating(minFilter,maxFilter, dbHandler.getServiceProviders_time(minTime,maxTime,dbHandler.getServiceProviders(serviceName)));
        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, serviceProvidersList);
        listView.setAdapter(new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, serviceProvidersList));

        findViewById(R.id.rating_options).setVisibility(View.GONE);
        findViewById(R.id.filter_options).setVisibility(View.VISIBLE);
        findViewById(R.id.service_providers_list).setVisibility(View.VISIBLE);

    }


    public void viewTimeOptions(View view){

        //showRatingSelectDialog();
        findViewById(R.id.filter_options).setVisibility(View.GONE);
        findViewById(R.id.service_providers_list).setVisibility(View.GONE);
        findViewById(R.id.time_options).setVisibility(View.VISIBLE);


    }
    public void updateTimeFilter(View view){


        try {
            minTime = Integer.parseInt(((EditText) findViewById(R.id.minTime)).getText().toString());
            maxTime = Integer.parseInt(((EditText) findViewById(R.id.maxTime)).getText().toString());
            serviceProvidersList = dbHandler.getServiceProviders_time(minTime,maxTime, dbHandler.getServiceProviders_rating(minFilter,maxFilter,dbHandler.getServiceProviders(serviceName)));
            adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, serviceProvidersList);
            listView.setAdapter(new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, serviceProvidersList));
        }
        catch (Exception e){
            resetFilter();
        }

        if(maxTime>=minTime) {
            findViewById(R.id.time_options).setVisibility(View.GONE);
            findViewById(R.id.filter_options).setVisibility(View.VISIBLE);
            findViewById(R.id.service_providers_list).setVisibility(View.VISIBLE);
        }
        else
            Toast.makeText(this,"Invalid Time Interval",Toast.LENGTH_LONG).show();
    }

    public void resetFilter(){
        serviceProvidersList =  (dbHandler.getServiceProviders(serviceName));
        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, serviceProvidersList);
        listView.setAdapter(new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, serviceProvidersList));

        ((SeekBar)findViewById(R.id.barMin)).setProgress(0);
        ((SeekBar)findViewById(R.id.barMax)).setProgress(5);

        minFilter = 0;
        maxFilter = 5;

        ((EditText) findViewById(R.id.maxTime)).setText("");
        ((EditText) findViewById(R.id.minTime)).setText("");

        minTime = 0;
        maxTime = 24;
    }

    public void resetFilter(View view){
        serviceProvidersList =  (dbHandler.getServiceProviders(serviceName));
        adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, serviceProvidersList);
        listView.setAdapter(new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, serviceProvidersList));

        ((SeekBar)findViewById(R.id.barMin)).setProgress(0);
        ((SeekBar)findViewById(R.id.barMax)).setProgress(5);

        minFilter = 0;
        maxFilter = 5;

        ((EditText) findViewById(R.id.maxTime)).setText("");
        ((EditText) findViewById(R.id.minTime)).setText("");

        minTime = 0;
        maxTime = 24;

    }

/*
    private void showRatingSelectDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_rating_select_dialog, null);
        dialogBuilder.setView(dialogView);

        final Button btnSearch = (Button) dialogView.findViewById(R.id.btnSearch);

        dialogBuilder.setTitle("Rating Search");
        final AlertDialog b = dialogBuilder.create();
        b.show();



        //(findViewById(R.id.LL)).setVisibility(View.VISIBLE);
        //((SeekBar)findViewById(R.id.seekBar2)).setVisibility(View.VISIBLE);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Need to make a method that makes a list based on the given rating and displays it
                min = minRating.getProgress();
                max = maxRating.getProgress();

                ArrayList<User> temp= dbHandler.getServiceProviders(serviceName,min,max);
                adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, temp);
                listView.setAdapter(new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, temp));
                b.dismiss();
            }
        });
    }*/


    }
