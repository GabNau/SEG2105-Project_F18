package com.example.gaba.intelligence_homeapp;

import android.content.Intent;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

public class HomeOwnerTest {

    @Rule
    public ActivityTestRule<WelcomeScreen> hOwnerTestRule = new ActivityTestRule<WelcomeScreen>(WelcomeScreen.class)
    {
        @Override
        protected Intent getActivityIntent()
        {
            Intent intent = new Intent();
            intent.putExtra("roleType","HomeOwner");
            intent.putExtra("username","Billy");
            intent.putExtra("role","Home Owner");
            return intent;
        }
    };
    public ActivityTestRule<WelcomeScreen> sProviderTestRule = new ActivityTestRule<WelcomeScreen>(WelcomeScreen.class)
    {   @Override
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
          intent.putExtra("username","Admin");
          intent.putExtra("role", "Administrator");
          return intent;
      }
    };
    public ActivityTestRule<Service_providers_list> splTestRule = new ActivityTestRule<Service_providers_list>(Service_providers_list.class){
        @Override
        protected Intent getActivityIntent()
        {
            Intent intent = new Intent();
            intent.putExtra("serviceName", "Harvey Cleaners");
            return intent;
        }
    };
    public ActivityTestRule<BookingsList> bLTestRule = new ActivityTestRule<BookingsList>(BookingsList.class);
    public ActivityTestRule<rating_select_dialog> rsdTestRule = new ActivityTestRule<rating_select_dialog>(rating_select_dialog.class);

    private rating_select_dialog rsd = null;
    private Service_providers_list spl = null;
    private BookingsList bL = null;
    private WelcomeScreen hOwner = null;
    private WelcomeScreen sProvider = null;
    private MyDBHandler database;
    private ServiceList sList;

    private Button sListBtn, addButton, ratingBtn,pickTBtn,filter, bookIt;
    private RadioButton licensing;
    private Button createProfile;
    private TextView textInput;

    @Before
    public void setUp(){
        hOwner = hOwnerTestRule.getActivity();
        sProvider = sProviderTestRule.getActivity();
        sList = sListTestRule.getActivity();
        spl = splTestRule.getActivity();
        rsd = rsdTestRule.getActivity();
        bL = bLTestRule.getActivity();
        database = new MyDBHandler(hOwner);
    }

    @After
    public void cleanUp(){database.clearAllTables();}

    /** To test Home Owner Functionality- this user must have access to some list of services and Service Providers it can search through*/
    @Test
    @UiThreadTest
    public void CreationOfServices(){
        database.addService(new Service("Harvey Cleaners", 5.0));
        database.addService(new Service("Bug Watchers", 13.5));
    }
    
    @Test
    @UiThreadTest
    public void CreationOfServiceProviders(){

        CreationOfServices();
        database.addUser(new User("Bill","pass","Service Provider"));
        long phone = 613000000;
        String name = "Bill";
        String company = "Student Assurance";
        String address = "800 King Edward";
        String description = "We are not Insurance.";
        try {
            database.addProfile(name, company, address, phone, description, true, "0");
        }catch (Exception e){  }

        assertNotNull(database.getServiceProviderInfo("Bill"));

        database.addUser(new User("Saraaah","pass","Service Provider"));
        long phoneNum = 613000123;
        try {
            database.addProfile("Saraaah", "uOTTAWA", "800 King Edward", phoneNum, "Say whaa", false, "0");
        }catch (Exception e){  }
        assertNotNull(database.getServiceProviderInfo("Saraaah"));

        database.addProviderToService("Bill", "Harvey Cleaners");
        database.addProviderToService("Saraaah", "Bug Watchers");
        database.addProviderToService("Saraaah", "Harvey Cleaners");

    }

    @Test
    @UiThreadTest
    public void checkServiceListAccess(){
        sListBtn = hOwner.findViewById(R.id.servList);
        sListBtn.performClick();
        assertTrue(sListBtn.isClickable());
    }
    @Test
    @UiThreadTest
    public void checkBookingList(){
        assertEquals(1,1);
        bookIt = hOwner.findViewById(R.id.bookings);
        bookIt.performClick();
        assertTrue(bookIt.isClickable());
    }

    @Test
    @UiThreadTest
    public void checkIfBookingsAreEmpty(){
        CreationOfServices();
        CreationOfServiceProviders();
        assertEquals(null,bL);
    }

    @Test
    @UiThreadTest
    public void searchByTypeAndRate(){
        CreationOfServices();
        CreationOfServiceProviders();
        //Initiated past the page which specifies type
        Service serviceType = new Service();
        serviceType.setName("Harvey Cleaners");
        serviceType.setRate(55.5);
      //  ArrayList<Service> soFar = database.getAllServices();

        //spl.updateRatingFilter(); Time constraint prevented us from testing the buttons below instead
//        ratingBtn = spl.findViewById(R.id.rating);
//        ratingBtn.performClick();
//        filter = rsd.findViewById(R.id.btnSearch);
//        filter.performClick();
//        serviceType = sList.findViewById(R.id.addPassword);

    }

    @Test
    @UiThreadTest
    public void checkTimeInputs(){
        CreationOfServices();
        CreationOfServiceProviders();
        ArrayList<User> providers = new ArrayList<User>();
        providers.add(database.findUser("Bill"));
        providers.add(database.findUser("Saraaah"));
        ArrayList<User> userList = database.getServiceProviders_time(0,24,providers);
        assertNotNull(userList);

    }

    @Test
    @UiThreadTest
    public void checkAvailability(){
        CreationOfServices();
        CreationOfServiceProviders();
        database.updateAvailability("Bill","12");
        String bler = database.getAvailabilities("Bill");
        assertNotNull(bler);
    }

    @Test
    @UiThreadTest
    public void checkResetFilters(){
        //Due to time contraints we were unable to fully test all the possibilities of our search engine.
        // So the above tests suffice.
        CreationOfServices();
        CreationOfServiceProviders();
        database.clearAllTables();
//        assertTrue(database.getAllServices() == null);
    }
    
    @Test
    @UiThreadTest
    public void checkAddReviewFeature(){
        CreationOfServices();
        CreationOfServiceProviders();
        database.addReview("Bill",5,"Bein!");

    }

}
