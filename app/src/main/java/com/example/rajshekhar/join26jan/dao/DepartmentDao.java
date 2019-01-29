package com.example.rajshekhar.join26jan.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import com.example.rajshekhar.join26jan.model.Category;

import java.util.List;

@Dao
public interface DepartmentDao {

    @Insert
    long insert(Category category);

    @Query("SELECT * FROM category")
    List<Category>getAll();

    @Delete
    void deleteAll(Category... categories);



}
