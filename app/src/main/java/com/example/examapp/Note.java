package com.example.examapp;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.Objects;

public class Note implements Serializable {
    private String id, title, content;

    /* Constructors */
    // Default constructor for Firestore
    public Note() {
        // Required for Firestore serialization
    }
    // Constructor that accepts id, title, and content
    public Note(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
    // Constructor that accepts title and content, generates id dynamically
    public Note(String title, String content) {
        this.id = java.util.UUID.randomUUID().toString(); // Generates a unique ID
        this.title = title;
        this.content = content;
    }

    /* Getters and Setters */

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("note_title")  // Optional, if you want to map Firestore field to a custom name
    public String getTitle() {
        return title;
    }
    @PropertyName("note_title")
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }



    // Override toString for easier debugging and logging
    @Override
    public String toString() {
        return "Note{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    // Utility: Check if the note is empty
    public boolean isEmpty() {
        return (title == null || title.isEmpty()) && (content == null || content.isEmpty());
    }

    // Equals and hashCode for comparison and collections usage
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equals(id, note.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}