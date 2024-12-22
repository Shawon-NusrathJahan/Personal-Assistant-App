package com.example.examapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

public class NoteAddFragment extends Fragment {

    private EditText editTextTitle, editTextContent;
    private Button buttonSave, buttonCancel;

    public NoteAddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note_add, container, false);

        editTextTitle = view.findViewById(R.id.editTextNoteTitle );
        editTextContent = view.findViewById(R.id.editTextNoteContent);
        buttonSave = view.findViewById(R.id.buttonSaveNote);
        buttonCancel = view.findViewById(R.id.buttonCancel);

        // Save button functionality
        buttonSave.setOnClickListener(v -> saveNote());

        // Cancel button functionality
        buttonCancel.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        return view;
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString().trim();
        String content = editTextContent.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new note object
        Note note = new Note(title, content);

        // Save note to Firestore
        FirebaseFirestore.getInstance().collection("notes")
                .add(note)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Note added successfully", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error adding note", Toast.LENGTH_SHORT).show();
                });
    }
}
