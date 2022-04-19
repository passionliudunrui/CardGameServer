package com.cardgameserver.service;

import com.cardgameserver.entity.Note;

import java.util.List;

public interface NoteService {
    List<Note> findById(Long userId);
    int insert(Note note);
}
