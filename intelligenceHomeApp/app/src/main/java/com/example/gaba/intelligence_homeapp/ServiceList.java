package com.example.gaba.intelligence_homeapp;

import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.*;

// class creates a list view of all services, and allows admin to add, edit or delete services
public class ServiceList extends AppCompatActivity {

    //MyDBHandler dbHandler = new MyDBHandler(this);
    ArrayList<Service> serviceList = new ArrayList<>();
    ListView listView;
    ArrayAdapter<Service> adapter;
    MyDBHandler dbHandler;

    EditText editService;
    EditText editRate;
    Button buttonAddService;
    String username;
    String role;

    @Override
    // onCreate method
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_list);
        editService = (EditText) findViewById(R.id.editService);
        editRate = (EditText) findViewById(R.id.editRate);
        buttonAddService = (Button) findViewById(R.id.btnAddService);
        // Get ListView object from xml layout
        listView = findViewById(R.id.serv);
        //For each item in database, add to serviceList
        dbHandler = new MyDBHandler(this);
        serviceList =  (dbHandler.getAllServices());
        //Create an ArrayAdapter and Set it on the ListView
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, serviceList);
        listView.setAdapter(adapter);
        role = getIntent().getStringExtra("role");
        username = getIntent().getStringExtra("username");
        if (!role.equals("Administrator")){
            findViewById(R.id.editService).setVisibility(View.GONE);
            findViewById(R.id.editRate).setVisibility(View.GONE);
            findViewById(R.id.btnAddService).setVisibility(View.GONE);
            findViewById(R.id.textServ).setVisibility(View.GONE);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), ((TextView) view).getText(),Toast.LENGTH_SHORT).show();
                String[] item = ((String) ((TextView) view).getText()).split(", ");
                String n = item[0];
                String r = item[1].split(" ")[0];
                //Toast.makeText(getApplicationContext(), n, Toast.LENGTH_SHORT).show();
                if (role.equals("Administrator")){
                    showUpdateDeleteDialog(n, Double.parseDouble(r));
                }
                else if (role.equals("Service Provider")){
                    showAddDeleteProviderDialog(username, n);
                }
                else if (role.equals("Home Owner")){
                    //showServiceProviders(n);// n = service name
                    Intent intent2 = new Intent(getApplicationContext(),Service_providers_list.class);
                    intent2.putExtra("serviceName", n);
                    intent2.putExtra("username", username);
                    startActivityForResult(intent2,0);

                }

            }
        });
    }

    //  Method used to create a dialog box that it used to update and delete services
    private void showUpdateDeleteDialog(final String serviceName, final double serviceRate){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.activity_update_delete_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextService = (EditText) dialogView.findViewById(R.id.editService);
        final EditText editTextRate  = (EditText) dialogView.findViewById(R.id.editRate);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateService);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteService);
        final EditText updateRate = (EditText) dialogView.findViewById(R.id.editHourlyRate);

        dialogBuilder.setTitle(serviceName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double rate = Double.parseDouble(String.valueOf(updateRate.getText().toString()));
                updateService(serviceName, rate);
                b.dismiss();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                deleteService(serviceName);
                b.dismiss();
            }

        });
    }

    // Method used to delete a service from the database and list
    public boolean deleteService(String name){
        dbHandler.deleteService(name);
        serviceList = dbHandler.getAllServices();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, serviceList);
        listView.setAdapter(adapter);
        return true;
    }

    // Method used to update the service in the database and list
    public void updateService(String service, double rate){
        try {
            dbHandler.updateService(service, rate);
            serviceList = dbHandler.getAllServices();
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, serviceList);
            listView.setAdapter(adapter);
        }catch(Exception e){

        }
    }

    // Method used to add a service in the database and list
    public void addService(View view){

        String name = editService.getText().toString().trim();
        double rate = 0.0;
        boolean validRate = true;
        try {
            rate = Double.parseDouble(editRate.getText().toString());
        }
        catch (Exception e){
            validRate = false;
            Toast.makeText(this,"Please enter a valid name and rate",Toast.LENGTH_LONG).show();
        }
        if(name.isEmpty() || !validRate){

            finish();
            Toast.makeText(this,"Please enter a valid Name and Rate",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(),ServiceList.class);
            intent.putExtra("username", username);
            intent.putExtra("role", role);
            startActivityForResult(intent,0);
        }
        else{
            Toast.makeText(this,"Service added",Toast.LENGTH_LONG).show();

            Service service1 = new Service(name,rate);
            dbHandler.addService(service1);
            //dbHandler.clearAllTables();
            serviceList = dbHandler.getAllServices();
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, serviceList);
            listView.setAdapter(adapter);


            editRate.setText("");
            editService.setText("");
        }
    }

    public boolean showAddDeleteProviderDialog(final String username, final String serviceName){
        try {
            if (username == "segTestUser")
                throw new Exception();
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            final View dialogView = inflater.inflate(R.layout.activity_add_delete_providers, null);
            dialogBuilder.setView(dialogView);

            final Button buttonAdd = (Button) dialogView.findViewById(R.id.buttonAddProvider);
            final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteProvider);

            dialogBuilder.setTitle("Service: " + serviceName);
            final AlertDialog b = dialogBuilder.create();
            b.show();

            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //addProvider
                    if (!dbHandler.addProviderToService(username, serviceName))
                        Toast.makeText(getApplicationContext(),"Already registered to this service",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(),"Registered to service",Toast.LENGTH_LONG).show();
                    b.dismiss();
                }
            });

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    //deleteProvider()
                    dbHandler.deleteProviderToService(username, serviceName);
                    Toast.makeText(getApplicationContext(),"Unregistered from service",Toast.LENGTH_LONG).show();
                    b.dismiss();
                }

            });
        }
        catch(Exception e){
            return false;
        }
        return true;
    }






}
