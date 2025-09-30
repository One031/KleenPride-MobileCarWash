package com.s4.kleenpride_mobilecarwash;

import static org.junit.Assert.assertNotNull;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented tests for Firebase Authentication using the local emulator
 * This runs on an Android device
 */

@RunWith(AndroidJUnit4.class)
public class AuthTest {

    private FirebaseAuth auth;

    @Before
    public void setUp() {
        // Initialize FirebaseAPP for testing
        FirebaseApp.initializeApp(ApplicationProvider.getApplicationContext());

        // Get the FirebaseAuth instance
        auth = FirebaseAuth.getInstance();

        // Connect to emulator
        auth.useEmulator("10.0.2.2", 9099);
    }

    @Test
    public void testRegister() throws Exception {
        // Register a test user and block until complete
        assertNotNull(
                Tasks.await(auth.createUserWithEmailAndPassword("test@example.com", "password123"))
                        .getUser()
        );
    }

    @Test
    public void testLogin() throws Exception {
        // Login the test user and block until complete
        assertNotNull(
                Tasks.await(auth.signInWithEmailAndPassword("test@example.com", "password123"))
                        .getUser()
        );
    }
}
