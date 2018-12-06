package com.example.gaba.intelligence_homeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

public class ServiceProvidersTest {
    @Rule
    public ActivityTestRule<WelcomeScreen> sProviderTestRule = new ActivityTestRule<WelcomeScreen>(WelcomeScreen.class)
    {
        @Override
        protected Intent getActivityIntent()
        {
            Intent intent = new Intent();
            intent.putExtra("roleType","ServiceProvider");
            intent.putExtra("username","Bob");
            intent.putExtra("role","Service Provider");
            return intent;
        }
    };

    public ActivityTestRule<ServiceList> sListTestRule = new ActivityTestRule<ServiceList>(ServiceList.class){
        @Override
        protected Intent getActivityIntent()
        {
            Intent intent = new Intent();
            intent.putExtra("role", "Administrator");
            return intent;
        }
    };
    private WelcomeScreen sProvider = null;
    private MyDBHandler database = null;
    private ServiceList sList = null;

    private RadioButton licensing;
    private Button createProfile, selectAvail;
    private TextView textInput;

    @Before
    public void setUp(){
        sProvider = sProviderTestRule.getActivity();
        database = new MyDBHandler(sProvider);
        sList = sListTestRule.getActivity();

    }
    @After
    public void cleanUp(){database.clearAllTables();}

    /** Following made so it can be used to test the feature for a service provider to link their profile to a given service
     * See checkProvidingService Test below*/
    @Test
    @UiThreadTest
    public void CreationOfService(){ database.addService(new Service("Harvey Cleaners", 5.0)); }

    @Test
    @UiThreadTest
    public void checkProfileCreation(){
        assertNotNull(sProvider.findViewById(R.id.editAddress));
        textInput = sProvider.findViewById(R.id.editAddress);
        textInput.setText("800 King Edward");
//        String address = textInput.getText().toString();

        textInput = sProvider.findViewById(R.id.editDesc);
        textInput.setText("To assure students do not drown in their studies. We are not Insurance.");
//        String description = textInput.getText().toString();

        textInput = sProvider.findViewById(R.id.editCompany);
        textInput.setText("Student Assurance");
//        String company = textInput.getText().toString();

        textInput = sProvider.findViewById(R.id.editPhone);
        textInput.setText("6130000000");
//        long phoneNum = Long.parseLong(textInput.getText().toString());

        licensing = sProvider.findViewById(R.id.yesBtn);
        licensing.setChecked(true);
        Boolean license = licensing.isChecked();

        createProfile = sProvider.findViewById(R.id.btnCreateProf);
        createProfile.performClick();

        User u = database.findUser("Bob");
        assertEquals("Bob", u.getUsername());
        assertEquals(true, database.hasProfile("Bob"));
    }

    /** Checks to make sure when an account/user is deleted, their profile is gone as well*/
   @Test
   @UiThreadTest
   public void checkForNoProfile(){
       database.clearAllTables();
       assertEquals(false, database.hasProfile("Bob"));
   }

    @Test
    @UiThreadTest
    public void checkProvidingService(){
        checkProfileCreation();
        CreationOfService();
        database.addProviderToService("Bob","Harvey Cleaners" );
    }

   @Test
   @UiThreadTest
   public void checkAddAvailabilityButton(){
        selectAvail = sProvider.findViewById(R.id.btnAvail);
        selectAvail.performClick();
       //TODO: Add availability to one slot
       //TODO: Then check to make sure number is a digit (make sure this actually is done in our group)
   }

}
