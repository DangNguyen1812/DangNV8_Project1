package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.exception.NoteException;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/note")
public class NoteController {

    @Autowired
    private NoteService noteService;

    /**
     * Create a Note
     * 
     * @param note
     * @return
     */
    @PostMapping("/add")
    public String addNote(@ModelAttribute Note note) {
        try {
            Note noteModel = noteService.getByNoteId(note.getNoteId());
            if (noteModel != null) {
                noteService.update(note);
                noteService.setMsgNotification("Success: The note has been successfully updated");
            } else {
                noteService.add(note);
                noteService.setMsgNotification("Success: The note has been successfully created");
            }
        } catch (NoteException e) {
            noteService.setMsgNotification(e.getMessage());
        } catch (Exception e) {
            noteService.setMsgNotification("Error: Addding note occurred an expected error");
        }

        return "redirect:/home";
    }

    /**
     * Delete a note by noteId
     * 
     * @param noteId
     * @return
     */
    @GetMapping("/delete/{noteId}")
    public String deleteNote(@PathVariable String noteId) {
        noteService.deleteByID(Integer.parseInt(noteId));
        noteService.setMsgNotification("Success: The note has been successfully delete");
        return "redirect:/home";
    }

    /**
     * Get by note Id
     * 
     * @param noteId
     * @return
     */
    @GetMapping("/get/{noteId}")
    public ResponseEntity<Note> getNote(@PathVariable String noteId) {
        Note note = noteService.getByNoteId(Integer.parseInt(noteId));
        return new ResponseEntity<>(note, HttpStatus.OK);
    }

}
