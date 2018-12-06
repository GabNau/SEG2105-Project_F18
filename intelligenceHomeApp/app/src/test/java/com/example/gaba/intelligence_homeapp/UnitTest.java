package com.example.gaba.intelligence_homeapp;

import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void PasswordEncryptionTest() {
        try {
            System.out.println("Password: 'admin' is stored as "+PasswordEncryption.encrypt("admin")+" in the database");
            System.out.println("Password: 'serviceprovider123' is stored as "+PasswordEncryption.encrypt("serviceprovider123")+" in the database");
            System.out.println("Password: 'homeowner_1' is stored as "+PasswordEncryption.encrypt("homeowner_1")+" in the database");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            assertEquals(PasswordEncryption.encrypt("admin"), PasswordEncryption.encrypt("admin"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}