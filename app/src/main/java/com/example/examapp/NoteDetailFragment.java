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

public class NoteDetailFragment extends Fragment {

    private EditText editTextTitle, editTextContent;
    private Button buttonEdit, buttonSave, buttonDelete;
    private Note note;

    public static NoteDetailFragment newInstance(Note note) {
        NoteDetailFragment fragment = new NoteDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("note", note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_detail, container, false);

        editTextTitle = view.findViewById(R.id.editTextNoteTitle);
        editTextContent = view.findViewById(R.id.editTextNoteContent);
        buttonEdit = view.findViewById(R.id.buttonEdit);
        buttonSave = view.findViewById(R.id.buttonSave);
        buttonDelete = view.findViewById(R.id.buttonDelete);

        if (getArguments() != null) {
            note = (Note) getArguments().getSerializable("note");
            editTextTitle.setText(note.getTitle());
            editTextContent.setText(note.getContent());
        }

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextTitle.setEnabled(true);
                editTextContent.setEnabled(true);
                buttonEdit.setVisibility(View.GONE);
                buttonSave.setVisibility(View.VISIBLE);
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              FirebaseFirestore.getInstance().collection("notes").document(note.getId())
                      .update("title", editTextTitle.getText().toString(), "content", editTextContent.getText().toString())
                      .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Note saved", Toast.LENGTH_SHORT).show());

            //        disableEditing();
              editTextTitle.setEnabled(false);
              editTextContent.setEnabled(false);
              buttonEdit.setVisibility(View.VISIBLE);
              buttonSave.setVisibility(View.GONE);
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore.getInstance().collection("notes").document(note.getId())
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getContext(), "Note deleted", Toast.LENGTH_SHORT).show();
                            getParentFragmentManager().popBackStack();
                        });
            }
        });


        return view;
    }
}
