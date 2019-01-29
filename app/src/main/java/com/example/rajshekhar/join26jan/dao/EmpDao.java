package com.example.rajshekhar.join26jan.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;
import android.arch.persistence.room.Update;


import com.example.rajshekhar.join26jan.model.CategoryNote;
import com.example.rajshekhar.join26jan.model.Note;

import java.util.List;


@Dao
public interface EmpDao {
    @Insert
    void insertAll(Note... emps);

    @Update
    void updateAll(Note... emps);

    @Query("SELECT * FROM Note")
    List<Note> getAll();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT Note.id, Note.title, Note.description, category.name as categoryName " +
            "FROM Note " +
            "LEFT JOIN category ON Note.category_id = category.id")
    List<CategoryNote> getCategoryNotes();

    @Query("SELECT Note.id, Note.title, Note.description, Note.category_id " +
            "FROM Note " +
            "LEFT JOIN category ON Note.category_id = category.id " +
            "WHERE Note.id = :noteId")
    CategoryNote getCategoryNote(long noteId);

    @Delete
    void deleteAll(Note... emps);


}
