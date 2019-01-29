package com.example.rajshekhar.join26jan;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.rajshekhar.join26jan.adapter.NoteAdapter;
import com.example.rajshekhar.join26jan.db.AppDatabase;
import com.example.rajshekhar.join26jan.model.CategoryNote;
import com.example.rajshekhar.join26jan.model.Note;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.ActionCallback, View.OnClickListener {
    private NoteAdapter adapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME).build();
        findViewById(R.id.add).setOnClickListener(this);
        adapter = new NoteAdapter(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                deleteNote(adapter.getNote(viewHolder.getAdapterPosition()));
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }
    @Override
    public void onEdit(CategoryNote note) {
        Intent intent = new Intent(this, AddEmpActivity.class);
        intent.putExtras(AddEmpActivity.newInstanceBundle(note.getId()));
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                startActivity(new Intent(this, AddEmpActivity.class));
                break;
        }
    }
    private void loadNotes() {
        new AsyncTask<Void, Void, List<CategoryNote>>() {
            @Override
            protected List<CategoryNote> doInBackground(Void... params) {
                return db.getNoteDao().getCategoryNotes();
            }

            @Override
            protected void onPostExecute(List<CategoryNote> notes) {
                adapter.setNotes(notes);
            }
        }.execute();
    }

    private void deleteNote(Note note) {
        new AsyncTask<Note, Void, Void>() {
            @Override
            protected Void doInBackground(Note... params) {
                db.getNoteDao().deleteAll(params);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                loadNotes();
            }
        }.execute(note);
    }
}
