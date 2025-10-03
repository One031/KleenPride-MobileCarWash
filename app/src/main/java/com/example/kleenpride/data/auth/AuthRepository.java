package com.example.kleenpride.data.auth;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


/**
 * AuthRepository handles all Firebase Authentication operations.
 * This class is part of the data layer (backend) and isolates
 * Firebase logic from the ViewModel and UI
 */

public class AuthRepository {
    // FirebaseAuth instance to interact with Firebase Authentication
    private final FirebaseAuth auth;

    // Constructor initializes the FirebaseAuth instance
    public AuthRepository (){
    auth = FirebaseAuth.getInstance();
    }

    /**
     * Registers a new user with email and password
     * Wait for Firebase to finish (asynchronous)
     * If successful, return the new user object
     * If failed, throw an error so it can be handles by the ViewModel/UI
     */

    public Task<FirebaseUser> register(String email, String password) {
        // createUserWithEmailAndPassword asynchronously creates the user in Firebase
        return auth.createUserWithEmailAndPassword(email, password)
                // continueWith runs after the asynchronous Firebase task completes
                .continueWith(task -> {
                    if(task.isSuccessful()) {
                        // On success, return the currently authenticated user
                        return auth.getCurrentUser();
                    }else {
                        // On failure, throw the exception to be handled by the caller
                        throw task.getException();
                    }
                });
    }

    /**
     * Logs in an existing user with email and password
     * Returns a Task that resolves with the FirebaseUser if successful, or throws an exception if it fails.
     */

    public Task<FirebaseUser> login (String email, String password) {
        // Ask Firebase to sign in the user with the provided email and password
        return auth.signInWithEmailAndPassword(email, password)
                // continueWith runs after the asynchronous Firebase task completes
                .continueWith(task -> {
                    if (task.isSuccessful()){
                        // On success, return the currently authenticated user
                        return auth.getCurrentUser();
                    }else {
                        // On failure, throw the exception to be handled by the caller
                        throw task.getException();
                    }
                });
    }
    /**
    Logging in with Google credentials
     */

    public Task<FirebaseUser> loginWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        return auth.signInWithCredential(credential)
                .continueWith(task -> {
                    if(task.isSuccessful()){
                        return auth.getCurrentUser();
                    }else {
                        throw task.getException();
                    }
                });
    }

    /**
     * Forgot Password
     */

    public Task<Void> sendPasswordResetEmail(String email) {
        return auth.sendPasswordResetEmail(email);
    }

    /**
     * Logs out the current user
     * After this, getCurrentUser() will return null
     */
    public void logout() {
        auth.signOut();
    }

    /**
     * Returns the currently logged-in user, or null if no user is signed in
     */
    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

}