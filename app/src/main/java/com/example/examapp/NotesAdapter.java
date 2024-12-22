package com.example.examapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private ArrayList<Note> notes_list;
    private OnNoteClickListener listener;

    public NotesAdapter(ArrayList<Note> noteList, OnNoteClickListener listener) {
        this.notes_list = noteList;
        this.listener = listener;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = notes_list.get(position);
        holder.titleTextView.setText(note.getTitle());
        holder.contentTextView.setText(note.getContent());

        holder.itemView.setOnClickListener(v -> listener.onNoteClick(note));
    }

    @Override
    public int getItemCount() {
        return notes_list.size();
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, contentTextView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textViewTitle);
            contentTextView = itemView.findViewById(R.id.textViewContent);
        }
    }

    public interface OnNoteClickListener {
        void onNoteClick(Note note);
    }
}

