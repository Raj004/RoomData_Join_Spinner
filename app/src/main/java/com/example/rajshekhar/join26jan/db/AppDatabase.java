package com.example.rajshekhar.join26jan.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.rajshekhar.join26jan.dao.DepartmentDao;
import com.example.rajshekhar.join26jan.dao.EmpDao;
import com.example.rajshekhar.join26jan.model.Category;
import com.example.rajshekhar.join26jan.model.Note;

@Database(entities = {Note.class, Category.class}, version = 7)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "app_db";

    public abstract EmpDao getNoteDao();

    public abstract DepartmentDao getCategoryDao();

}
