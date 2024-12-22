package com.example.examapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class NotesFragment extends Fragment {

    private RecyclerView recyclerView;
    private Button buttonAddNote;
    private NotesAdapter notesAdapter;
    private ArrayList<Note> notesList;
    private FirebaseFirestore db;

    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewNotes);
        buttonAddNote = view.findViewById(R.id.buttonAddNote);
        
        // Set up RecyclerView
        notesList = new ArrayList<>();
        notesAdapter = new NotesAdapter(notesList, this::openNoteDetail);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(notesAdapter);

        // Add note button click listener
        buttonAddNote.setOnClickListener(v -> openAddNoteFragment());
        
        // (Initialize Firestore &) Fetch notes from Firestore
//        db = FirebaseFirestore.getInstance();
        fetchNotesFromFirestore();

        return view;
    }

    private void fetchNotesFromFirestore() {
//        db.collection("notes")
        FirebaseFirestore.getInstance().collection("notes")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    notesList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Note note = doc.toObject(Note.class);
                        note.setId(doc.getId());
                        notesList.add(note);
                    }
                    notesAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Snackbar.make(getView(), "Error fetching notes", Snackbar.LENGTH_SHORT).show();
                });
    }

    private void openAddNoteFragment() {
        // Navigate to AddNoteFragment
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new NoteAddFragment())
                .addToBackStack(null)
                .commit();
    }

    private void openNoteDetail(Note note) {
        // Pass note data to NoteDetailFragment
        NoteDetailFragment fragment = NoteDetailFragment.newInstance(note);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    // Method to add a new note to Firestore
//    public void addNote(String title, String content) {
//        Note note = new Note(title, content);
//        db.collection("notes").add(note)
//                .addOnSuccessListener(documentReference -> {
//                    notesList.add(note);
//                    notesAdapter.notifyDataSetChanged();
//                })
//                .addOnFailureListener(e -> {
//                    Snackbar.make(getView(), "Error adding note", Snackbar.LENGTH_SHORT).show();
//                });
//    }
}
