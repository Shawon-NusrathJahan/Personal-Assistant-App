package com.example.examapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignupFragment extends Fragment {
    FirebaseAuth mAuth;
    private EditText fullNameTxt, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button signupButton;
    private ProgressBar progressBar;
    private TextView navigateToLogin;

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        fullNameTxt = view.findViewById(R.id.signup_name);
        confirmPasswordEditText = view.findViewById(R.id.signup_confirm_password);
        emailEditText = view.findViewById(R.id.signup_email);
        passwordEditText = view.findViewById(R.id.signup_password);
        signupButton = view.findViewById(R.id.signup_button);
        progressBar = view.findViewById(R.id.progressBar);
//        navigateToLogin = view.findViewById(R.id.navigate_to_login);


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String fullName, email, password, confirmPassword;
                fullName = fullNameTxt.getText().toString();
                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();
                confirmPassword = confirmPasswordEditText.getText().toString();

                // Check for empty fields
                if (fullName.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    fullNameTxt.setError("Full Name is required");
                    fullNameTxt.requestFocus();
                    return;
                }

                if (email.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    emailEditText.setError("Email is required");
                    emailEditText.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    passwordEditText.setError("Password is required");
                    passwordEditText.requestFocus();
                    return;
                }

                if (confirmPassword.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    confirmPasswordEditText.setError("Please confirm your password");
                    confirmPasswordEditText.requestFocus();
                    return;
                }

                // Check if passwords match
                if (!password.equals(confirmPassword)) {
                    progressBar.setVisibility(View.GONE);
                    confirmPasswordEditText.setError("Passwords do not match");
                    confirmPasswordEditText.requestFocus();
                    return;
                }

                // Proceed with registration logic (e.g., Firebase Authentication)
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
//                                    Log.d(TAG, "createUserWithEmail:success");
//                                    FirebaseUser user = mAuth.getCurrentUser();
//                                    updateUI(user);
                                    Toast.makeText(getActivity(), "Registration Successful.",
                                            Toast.LENGTH_SHORT).show();
                                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.regBot, new LoginFragment());
                                    transaction.commit();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    String errorMsg = task.getException() != null ? task.getException().getMessage() : "Registration failed.";
                                    Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                                    //updateUI(null);
                                }
                            }
                        });
            }
        });

//        navigateToLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.regBot, new LoginFragment());
//                //transaction.addToBackStack(null); // adds the LoginFragment to the back stack, allowing the user to return to it by pressing the back button
//                transaction.commit();
//            }
//        });
        return view;
    }
}