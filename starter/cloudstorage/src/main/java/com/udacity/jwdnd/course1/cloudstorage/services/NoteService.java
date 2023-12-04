package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.exception.NoteException;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class NoteService {
    @Autowired
    private NoteMapper noteMapper;

    @Autowired
    private UserService userService;

    private String msgNotification;

    /**
     * Get all note from database
     * @return
     */
    public List<Note> getAll() {
        return noteMapper.getAll();
    }

    /**
     * Get by note id
     * @param noteId
     * @return
     */
    public Note getByNoteId(int noteId) {
        return noteMapper.getById(noteId);
    }

    /**
     * Adds a new note record
     * @param note
     */
    public void add(Note note) {
        for(Note noteDB : this.getAll()) {
            if(noteDB.getNoteTitle().equals(note.getNoteTitle())) {
                throw new NoteException("Title already exists, please try again");
            }
        }
        note.setUserId(userService.getCurrentUserId());
        noteMapper.add(note);
    }

    /**
     * Updates a note record
     * @param note
     */
    public void update(Note note) {
        note.setUserId(userService.getCurrentUserId());
        noteMapper.update(note);
    }

    /**
     * Deletes a record by note id
     * @param noteId
     */
    public void deleteByID(int noteId) {
        noteMapper.deleteByID(noteId);
    }

    /**
     * Get all note by user id
     * @param noteId
     * @return
     */
    public List<Note> getAllByUserId(int noteId) {
        return noteMapper.getAllByUserId(noteId);
    }

    public String getMsgNotification() {
        return msgNotification;
    }

    public void setMsgNotification(String msgNotification) {
        this.msgNotification = msgNotification;
    }
}
