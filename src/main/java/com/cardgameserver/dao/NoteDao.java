package com.cardgameserver.dao;

import com.cardgameserver.entity.Note;

import java.util.List;

public interface NoteDao {
    List<Note> findById(Long userId);
    int insert(Note note);

}
