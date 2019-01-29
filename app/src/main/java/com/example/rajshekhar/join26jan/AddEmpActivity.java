package com.example.rajshekhar.join26jan;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.rajshekhar.join26jan.adapter.CategoryAdapter;
import com.example.rajshekhar.join26jan.db.AppDatabase;
import com.example.rajshekhar.join26jan.model.Category;
import com.example.rajshekhar.join26jan.model.CategoryNote;
import com.example.rajshekhar.join26jan.model.Note;

import java.util.List;

public class AddEmpActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String EXTRA_NOTE_ID = "EXTRA_NOTE_ID";

    private AppDatabase db;
    private TextView title;
    private TextView description;
    private TextView category;
    private Spinner categorySpinner;
    private CategoryAdapter adapter;
    private Long noteId;
    private Note emp;

    public static Bundle newInstanceBundle(long noteId) {
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_NOTE_ID, noteId);
        return bundle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__emp);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_NOTE_ID)) {
            noteId = intent.getLongExtra(EXTRA_NOTE_ID, -1);
        }

        db = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME).build();
        title = (TextView) findViewById(R.id.title);
        description = (TextView) findViewById(R.id.description);
        category = (TextView) findViewById(R.id.category);
        findViewById(R.id.save).setOnClickListener(this);

        categorySpinner = (Spinner) findViewById(R.id.category_spinner);
        categorySpinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadCategories();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                saveNote();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            category.setVisibility(View.VISIBLE);
        } else {
            category.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void loadNote() {
        new AsyncTask<Long, Void, CategoryNote>() {
            @Override
            protected CategoryNote doInBackground(Long... params) {
                return db.getNoteDao().getCategoryNote(params[0]);
            }

            @Override
            protected void onPostExecute(CategoryNote note) {
                setEmp(note);
            }
        }.execute(noteId);
    }

    private void setEmp(Note emp) {
        this.emp = emp;
        title.setText(emp.getTitle());
        description.setText(emp.getDescription());
        int categoryPosition = adapter.getCategoryPosition(emp.getCategoryId());
        if (categoryPosition > 0) {
            categorySpinner.setSelection(categoryPosition);
        }
    }

    private void loadCategories() {
        new AsyncTask<Void, Void, List<Category>>() {
            @Override
            protected List<Category> doInBackground(Void... params) {
                return db.getCategoryDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Category> adapterCategories) {
                setCategories(adapterCategories);
                if (noteId != null) {
                    loadNote();
                }
            }
        }.execute();
    }

    private void setCategories(List<Category> categories) {
        adapter = new CategoryAdapter(this, android.R.layout.simple_list_item_1, categories);
        categorySpinner.setAdapter(adapter);
    }

    private void saveNote() {
        if (emp == null) {
            emp = new Note();
        }
        emp.setTitle(title.getText().toString().trim());
        emp.setDescription(description.getText().toString().trim());
        final String categoryName = category.getText().toString().trim();
        Category selectedCategory = adapter.getItem(categorySpinner.getSelectedItemPosition());
        final Long selectedCategoryId = (selectedCategory == null || selectedCategory.getId() <= 0) ? null : selectedCategory.getId();

        new AsyncTask<Note, Void, Void>() {
            @Override
            protected Void doInBackground(Note... params) {
                Note saveEmp = params[0];
                if (selectedCategoryId != null) {
                    saveEmp.setCategoryId(selectedCategoryId);
                } else if (categoryName.length() > 0) {
                    Category category = new Category();
                    category.setName(categoryName);
                    long categoryId = db.getCategoryDao().insert(category);
                    saveEmp.setCategoryId(categoryId);
                } else {
                    emp.setCategoryId(null);
                }
                if (saveEmp.getId() > 0) {
                    db.getNoteDao().updateAll(saveEmp);
                } else {
                    db.getNoteDao().insertAll(saveEmp);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                setResult(RESULT_OK);
                finish();
            }
        }.execute(emp);
    }
}
