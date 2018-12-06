package com.example.gaba.intelligence_homeapp;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;


public class SignupScreenTest {

    @Rule
    public ActivityTestRule<SignupScreen>sScreenTestRule = new ActivityTestRule<SignupScreen>(SignupScreen.class);
    private SignupScreen sScreen = null;
    private TextView text;
    private RadioButton homeOwner;
    private RadioButton serviceProvider;
    private RadioButton admin;
    private RadioGroup rGroup;
    private Button cAccountButton;
    private MyDBHandler database=null;


    @Before
    public void setUp() {
        sScreen = sScreenTestRule.getActivity();
        database = new MyDBHandler(sScreen);
        database.clearAllTables();
    }


    @Test
    @UiThreadTest
    public void checkUserLoginEntered() throws Exception{
        assertNotNull(sScreen.findViewById(R.id.addUser));
        text = sScreen.findViewById(R.id.addUser);
        text.setText("user1");
        String name = text.getText().toString();
        assertEquals("user1", name);
    }

    @Test
    @UiThreadTest
    public void checkPasswordIsEntered() throws Exception{
        assertNotNull(sScreen.findViewById(R.id.addPassword));
        text = sScreen.findViewById(R.id.addPassword);
        text.setText("password1");
        String pass = text.getText().toString();
        assertNotEquals("password", pass);
    }

    @Test
    @UiThreadTest
    public void checkUserTypeSelected() throws Exception{
        assertNotNull(sScreen.findViewById(R.id.radioGroup));
        rGroup = sScreen.findViewById(R.id.radioGroup);
        homeOwner = sScreen.findViewById(R.id.rb_HO);
        homeOwner.setChecked(true);
        assertNotEquals(rGroup.getCheckedRadioButtonId(), -1);
    }

    @Test
    @UiThreadTest
    public void signUpHomeOwnerTest()    {
        assertNotNull(sScreen.findViewById(R.id.addUser));
        text = sScreen.findViewById(R.id.addUser);
        text.setText("user1");
        String name = text.getText().toString();

        assertNotNull(sScreen.findViewById(R.id.addPassword));
        text = sScreen.findViewById(R.id.addPassword);
        text.setText("password1");
        String pass = text.getText().toString();

        rGroup = sScreen.findViewById(R.id.radioGroup);
        homeOwner = sScreen.findViewById(R.id.rb_HO);
        homeOwner.setChecked(true);

        cAccountButton = sScreen.findViewById(R.id.createAcc);
        cAccountButton.performClick();

        User u = database.findUser("user1");

        try {
            assertEquals(PasswordEncryption.encrypt("password1"),u.getPassword());
        }
        catch (Exception e){}
        //assertEquals("password1",u.getPassword());
        assertEquals("user1", u.getUsername());
        assertEquals("Home Owner", u.getRole());

    }

    @Test
    @UiThreadTest
    public void signUpServiceProviderTest()    {
        assertNotNull(sScreen.findViewById(R.id.addUser));
        text = sScreen.findViewById(R.id.addUser);
        text.setText("user2");
        String name = text.getText().toString();

        assertNotNull(sScreen.findViewById(R.id.addPassword));
        text = sScreen.findViewById(R.id.addPassword);
        text.setText("password2");
        String pass = text.getText().toString();

        rGroup = sScreen.findViewById(R.id.radioGroup);
        serviceProvider = sScreen.findViewById(R.id.rb_SP);
        serviceProvider.setChecked(true);

        cAccountButton = sScreen.findViewById(R.id.createAcc);
        cAccountButton.performClick();

        User u = database.findUser("user2");
        try {
            assertEquals(PasswordEncryption.encrypt("password2"),u.getPassword());
        }
        catch (Exception e){}
        //assertEquals("password2",u.getPassword());
        assertEquals("user2", u.getUsername());
        assertEquals("Service Provider", u.getRole());
    }

    @Test
    @UiThreadTest
    public void signUpAdminTest()    {
        assertNotNull(sScreen.findViewById(R.id.addUser));
        text = sScreen.findViewById(R.id.addUser);
        text.setText("admin");
        String name = text.getText().toString();

        assertNotNull(sScreen.findViewById(R.id.addPassword));
        text = sScreen.findViewById(R.id.addPassword);
        text.setText("admin");
        String pass = text.getText().toString();

        rGroup = sScreen.findViewById(R.id.radioGroup);
        admin = sScreen.findViewById(R.id.rb_AD);
        admin.setChecked(true);

        cAccountButton = sScreen.findViewById(R.id.createAcc);
        cAccountButton.performClick();

        User u = database.findUser("admin");
        try {
            assertEquals(PasswordEncryption.encrypt("admin"),u.getPassword());
        }
        catch (Exception e){}
        assertEquals("admin", u.getUsername());
        assertEquals("Administrator", u.getRole());

    }
    /** Tests to make sure only one account can be made per Username,
     * and that one cannot use the same username for even a different role */
    @Test
    @UiThreadTest
    public void checkForUniqueUserNames(){
        signUpHomeOwnerTest();

        text = sScreen.findViewById(R.id.addUser);
        text.setText("user1");
        String name = text.getText().toString();
        text = sScreen.findViewById(R.id.addPassword);
        text.setText("blah");
        String pass = text.getText().toString();

        rGroup = sScreen.findViewById(R.id.radioGroup);
        serviceProvider = sScreen.findViewById(R.id.rb_SP);
        serviceProvider.setChecked(true);

        cAccountButton = sScreen.findViewById(R.id.createAcc);
        cAccountButton.performClick();
        assertEquals(true,sScreen.usernameError);


    }

}
