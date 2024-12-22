package com.example.examapp;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.examapp.databinding.ActivityMainBinding;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    // Declare the Floating Action Buttons
    private FloatingActionButton fabMain, fabNotes, fabImages, fabEnvironmental, fabGps;
    private FloatingActionButton currentFab = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Initialize the Floating Action Buttons
        fabNotes = findViewById(R.id.fab_notes);
        fabImages = findViewById(R.id.fab_images);
        fabEnvironmental = findViewById(R.id.fab_environmental);
        fabGps = findViewById(R.id.fab_gps);

        fabNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new NotesFragment());
                transaction.commit();
//                toggleFragment(fabNotes, new NotesFragment());
            }
        });

        fabImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new ImagesNotesFragment());
                transaction.commit();
//                toggleFragment(fabImages, new ImagesNotesFragment());
            }
        });

        fabEnvironmental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, EnvMontActivity.class);
                startActivity(intent);

//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, new EnvironmentalMonitoringFragment());
//                transaction.commit();
//                toggleFragment(fabEnvironmental, new EnvironmentalMonitoringFragment());
            }
        });

        fabGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new GpsTrackingFragment());
                transaction.commit();
//                toggleFragment(fabGps, new GpsTrackingFragment());
            }
        });
    }

    /**
     * Toggles the fragment visibility and the FABs.
     */
//    private void toggleFragment(FloatingActionButton fab, androidx.fragment.app.Fragment fragment) {
//        if (currentFab == fab) {
//            // If the same FAB is clicked again, reset to the default state
////            resetFabVisibility();
//            removeCurrentFragment();
//            currentFab = null;
//        } else {
//            // Hide other FABs
////            hideOtherFabs(fab);
//            // Show the fragment
//            replaceFragment(fragment);
//            currentFab = fab;
//        }
//    }
//
//    /**
//     * Hides all other FABs except the currently clicked one.
//     * Resets the visibility of all FABs to visible.
//     */
////    private void hideOtherFabs(FloatingActionButton clickedFab) {
////        fabNotes.setVisibility(clickedFab == fabNotes ? View.VISIBLE : View.GONE);
////        fabImages.setVisibility(clickedFab == fabImages ? View.VISIBLE : View.GONE);
////        fabEnvironmental.setVisibility(clickedFab == fabEnvironmental ? View.VISIBLE : View.GONE);
////        fabGps.setVisibility(clickedFab == fabGps ? View.VISIBLE : View.GONE);
////    }
////    private void resetFabVisibility() {
////        fabNotes.setVisibility(View.VISIBLE);
////        fabImages.setVisibility(View.VISIBLE);
////        fabEnvironmental.setVisibility(View.VISIBLE);
////        fabGps.setVisibility(View.VISIBLE);
////    }
//
//    /**
//     * Replaces the current fragment with the provided one.
//     * Removes the currently visible fragment.
//     */
//    private void replaceFragment(androidx.fragment.app.Fragment fragment) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragment_container, fragment);
//        transaction.commit();
//    }
//    private void removeCurrentFragment() {
//        if (!getSupportFragmentManager().getFragments().isEmpty()) {
//            getSupportFragmentManager().popBackStack();
//        }
//    }

}
