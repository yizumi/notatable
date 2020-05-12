package com.apcandsons.notation;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Staff {
    ArrayList<Note> notes = new ArrayList<>();
    int id = 0;

    public void addNote(Note note) {
        note.setId(id++);
        notes.add(note);
    }

    public String toJSON() {
        Gson gson = new Gson();
        String json = gson.toJson(notes);
        notes.clear();
        return json;
    }
}
