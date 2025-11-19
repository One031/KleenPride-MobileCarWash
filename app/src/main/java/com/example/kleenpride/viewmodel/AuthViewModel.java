package com.example.kleenpride.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.example.kleenpride.data.auth.AuthRepository;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * AuthViewModel acts a a bridge between the UI and AuthRepository
 * The UI observes LiveData exposed by this ViewModel
 * The ViewModel calls the repository to perform Firebase operations
 */
public class AuthViewModel extends ViewModel {

    // Repository instance for authentication operations
    private final AuthRepository authRepo;

    // LiveData to observe the currently logged-in user
    private final MutableLiveData<FirebaseUser> userLiveData;

    // LiveData to observe error messages from Firebase operations
    private final MutableLiveData<String> errorLiveData;

    // LiveData to observe Forgot Password status
    private final MutableLiveData<Boolean> resetPasswordStatusLiveData;
    // Constructor initializes repository and LiveData
    public AuthViewModel() {
        authRepo = new AuthRepository();
        userLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
        resetPasswordStatusLiveData = new MutableLiveData<>();
    }

    // Expose immutable LiveData for the UI to observe
    public LiveData<FirebaseUser> getUserLiveData() { return userLiveData; }
    public LiveData<String> getErrorLiveData() { return errorLiveData; }
    public LiveData<Boolean> getResetPasswordStatusLiveData() { return resetPasswordStatusLiveData; }

    /**
     * Registers a new user using the AuthRepository
     * Updates LiveData with the result for the UI to observe
     */

    public void register(String email, String password, Runnable onSucess) {
        authRepo.register(email, password)
                .addOnSuccessListener(user -> {
                    // On success, post the FirebaseUser to LiveData
                    userLiveData.postValue(user);
                    if (onSucess != null) onSucess.run();
                })
                .addOnFailureListener(e -> {
                    // On failure, post the error message to LiveData
                    errorLiveData.postValue(e.getMessage());
                });
    }

    /**
     * Logs in an existing user using the AuthRepository
     * Updates LiveData with the result for the UI to observe
     */

    public void login (String email, String password) {
        authRepo.login(email, password)
                .addOnSuccessListener(user -> userLiveData.postValue(user))
                .addOnFailureListener(e -> errorLiveData.postValue(e.getMessage()));
    }

    // Login with Google
    public void googleLogin(String idToken) {
        authRepo.loginWithGoogle(idToken)
                .addOnSuccessListener(user -> userLiveData.postValue(user))
                .addOnFailureListener(e -> errorLiveData.postValue(e.getMessage()));
    }

    // Forgot password
    public void resetPassword(String email) {
        authRepo.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused -> resetPasswordStatusLiveData.postValue(true))
                .addOnFailureListener(e -> {
                    resetPasswordStatusLiveData.postValue(false);
                    errorLiveData.postValue(e.getMessage());
                });
    }

    /**
     * Logs out the current user
     * Clears userLiveData to indicate no user is logged in
     */

    public void logout() {
        authRepo.logout();
        userLiveData.postValue(null); // No user is logged in after logout
    }
}
