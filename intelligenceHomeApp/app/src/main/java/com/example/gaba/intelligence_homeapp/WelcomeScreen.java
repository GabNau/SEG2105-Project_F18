package com.example.gaba.intelligence_homeapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import static android.view.View.VISIBLE;

// class used to create the welcome screen once you log in
public class WelcomeScreen extends AppCompatActivity {

    TextView userNameDisplay;
    TextView userRoleDisplay;
    TextView adminRoleDisplay;
    TextView emptyCredentialsAlert;
    String str;
    User user;
    String role;
    Button serv_list;
    Button bookings;
    Button avail;
    Button createProf;
    RadioButton licenseTrue;
    RadioButton licenseFalse;
    EditText address;
    EditText description;
    EditText company;
    EditText phone;



    @Override
    // onCreate method
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        userRoleDisplay = findViewById(R.id.roleTxt);
        userNameDisplay = findViewById(R.id.userDisplay);
        adminRoleDisplay = findViewById(R.id.adminDisplay);
        avail = findViewById(R.id.btnAvail);
        createProf = findViewById(R.id.btnCreateProf);
        licenseFalse = findViewById(R.id.noBtn);
        licenseTrue = findViewById(R.id.yesBtn);
        serv_list = findViewById(R.id.servList);
        bookings = findViewById(R.id.bookings);
        emptyCredentialsAlert = findViewById(R.id.EmptyProfileAlert);

        MyDBHandler dbHandler = new MyDBHandler(this);
        user = dbHandler.findUser(getIntent().getStringExtra("Name"));

        //States Users Name and info on the Welcome Screen, if blank/null, throws an exception and simply states Welcome
        try {
            str = "Welcome " + user.getUsername() + "!";
        }catch(Exception e){
            str = "Welcome!";
        }

        //When running App from Login, the following should never run
        // only set for when app doesn't start with login
        try {
            String roleType = getIntent().getStringExtra("roleType");
            String hO = "HomeOwner";
            String sP = "ServiceProvider";
            if(roleType.equals(hO)){ roleType = "Home Owner"; }
            else if(roleType.equals(sP)){roleType = "Service Provider";}
            else{roleType = null;}
            if(user == null && !roleType.equals(null)){
                user = new User("Bob","password",roleType);
                dbHandler.addUser(user);
            }
        }catch(Exception e){
            String roleType = null;
        }


        userNameDisplay.setText(str);
        userRoleDisplay.setText(user.getRole() + " Account");
        role = user.getRole();
        String ad = "";
        if (role.equals("Administrator")){
            serv_list.setVisibility(VISIBLE);
            ad = dbHandler.getAllUsernames();
            adminRoleDisplay.setText(ad);
            findViewById(R.id.yesBtn).setVisibility(View.GONE);
            findViewById(R.id.noBtn).setVisibility(View.GONE);
            findViewById(R.id.noBtn).setVisibility(View.GONE);
            findViewById(R.id.editAddress).setVisibility(View.GONE);
            findViewById(R.id.editDesc).setVisibility(View.GONE);
            findViewById(R.id.editCompany).setVisibility(View.GONE);
            findViewById(R.id.txtProfile).setVisibility(View.GONE);
            findViewById(R.id.btnAvail).setVisibility(View.GONE);
            findViewById(R.id.rg).setVisibility(View.GONE);
            findViewById(R.id.btnCreateProf).setVisibility(View.GONE);
            findViewById(R.id.editPhone).setVisibility(View.GONE);

        }
        else
            adminRoleDisplay.setText("");

        boolean hasProfile = dbHandler.hasProfile(user.getUsername());
        if(user.getRole().equals("Service Provider") && !hasProfile){
            serv_list.setVisibility(View.GONE);
            bookings.setVisibility(View.GONE);
            findViewById(R.id.yesBtn).setVisibility(VISIBLE);
            findViewById(R.id.noBtn).setVisibility(VISIBLE);
            findViewById(R.id.noBtn).setVisibility(VISIBLE);
            findViewById(R.id.editAddress).setVisibility(VISIBLE);
            findViewById(R.id.editDesc).setVisibility(VISIBLE);
            findViewById(R.id.editCompany).setVisibility(VISIBLE);
            findViewById(R.id.txtProfile).setVisibility(VISIBLE);
            findViewById(R.id.btnAvail).setVisibility(View.GONE);
            findViewById(R.id.rg).setVisibility(VISIBLE);
        }

        else if(user.getRole().equals("Service Provider") && hasProfile){
            serv_list.setVisibility(VISIBLE);
            bookings.setVisibility(VISIBLE);
            findViewById(R.id.yesBtn).setVisibility(View.GONE);
            findViewById(R.id.noBtn).setVisibility(View.GONE);
            findViewById(R.id.noBtn).setVisibility(View.GONE);
            findViewById(R.id.editAddress).setVisibility(View.GONE);
            findViewById(R.id.editDesc).setVisibility(View.GONE);
            findViewById(R.id.editCompany).setVisibility(View.GONE);
            findViewById(R.id.txtProfile).setVisibility(View.GONE);
            findViewById(R.id.editPhone).setVisibility(View.GONE);
            //findViewById(R.id.btnAvail).setVisibility(View.GONE);
            findViewById(R.id.rg).setVisibility(VISIBLE);
            findViewById(R.id.noBtn).setVisibility(View.GONE);
            findViewById(R.id.yesBtn).setVisibility(View.GONE);
            findViewById(R.id.btnCreateProf).setVisibility(View.GONE);
            findViewById(R.id.btnAvail).setVisibility(View.VISIBLE);
            String temp = dbHandler.getServiceProviderInfo(user.getUsername());
            adminRoleDisplay.setText(temp);

        }
        else if (user.getRole().equals("Home Owner")){
            serv_list.setVisibility(VISIBLE);
            bookings.setVisibility(VISIBLE);
            findViewById(R.id.yesBtn).setVisibility(View.GONE);
            findViewById(R.id.noBtn).setVisibility(View.GONE);
            findViewById(R.id.noBtn).setVisibility(View.GONE);
            findViewById(R.id.editAddress).setVisibility(View.GONE);
            findViewById(R.id.editDesc).setVisibility(View.GONE);
            findViewById(R.id.editCompany).setVisibility(View.GONE);
            findViewById(R.id.txtProfile).setVisibility(View.GONE);
            findViewById(R.id.editPhone).setVisibility(View.GONE);
            //findViewById(R.id.btnAvail).setVisibility(View.GONE);
            findViewById(R.id.rg).setVisibility(VISIBLE);
            findViewById(R.id.noBtn).setVisibility(View.GONE);
            findViewById(R.id.yesBtn).setVisibility(View.GONE);
            findViewById(R.id.btnCreateProf).setVisibility(View.GONE);
            findViewById(R.id.btnAvail).setVisibility(View.GONE);


        }

    }

    public void availability(View view) {
        Intent intent = new Intent(getApplicationContext(), availiability.class);
        intent.putExtra("username", user.getUsername());
        startActivityForResult(intent, 0);
    }


    public void createProfile(View view) throws Exception{
        MyDBHandler dbHandler = new MyDBHandler(this);
        //TO DO: Check to make sure that the following are not null
        address = findViewById(R.id.editAddress);
        description = findViewById(R.id.editDesc);
        company = findViewById(R.id.editCompany);
        phone = findViewById(R.id.editPhone);
        boolean license = false;
        if (licenseTrue.isChecked()){
            license = true;
        }else{
            license = false;
        }

        /** CHECK if Boxes are empty- if not then addProfile*/
       try{
           dbHandler.addProfile(user.getUsername(),company.getText().toString(),address.getText().toString(),Long.parseLong(phone.getText().toString()),description.getText().toString(), license, "");
       }catch (Exception e){
           emptyCredentialsAlert.setText("ALERT. Please fill out ALL boxes appropriately!");
       }

        findViewById(R.id.yesBtn).setEnabled(false);
        findViewById(R.id.noBtn).setEnabled(false);
        findViewById(R.id.noBtn).setEnabled(false);
        findViewById(R.id.editAddress).setEnabled(false);
        findViewById(R.id.editDesc).setEnabled(false);
        findViewById(R.id.editCompany).setEnabled(false);
        findViewById(R.id.txtProfile).setEnabled(false);
        findViewById(R.id.editPhone).setEnabled(false);
        findViewById(R.id.rg).setEnabled(false);
        findViewById(R.id.btnCreateProf).setVisibility(View.GONE);
        findViewById(R.id.btnAvail).setVisibility(VISIBLE);

        finish();
        startActivityForResult(getIntent(),0);

    }

    // Method to start the activity
    public void sList(View view){

        Intent intent = new Intent(getApplicationContext(),ServiceList.class);
        intent.putExtra("role", role);
        intent.putExtra("username", user.getUsername());
        startActivityForResult(intent,0);
    }

    public void bList(View view){

        Intent intent = new Intent(getApplicationContext(),BookingsList.class);
        intent.putExtra("role", role);
        intent.putExtra("username", user.getUsername());
        startActivityForResult(intent,0);
    }
    /*
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        dayF = day;

        Calendar c = Calendar.getInstance();
        hour1 = c.get(Calendar.HOUR_OF_DAY);
        minute1 = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(WelcomeScreen.this,WelcomeScreen.this,
                hour1,minute1, DateFormat.is24HourFormat(this));
        timePickerDialog.show();

       //TimePickerDialog timePickerDialog1 = new TimePickerDialog(WelcomeScreen.this,WelcomeScreen.this,
       //         hour2,minute2, DateFormat.is24HourFormat(this));
       //timePickerDialog1.show();

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        hour1F = hourOfDay;
        minute1F = minute;
    }
    */

}
