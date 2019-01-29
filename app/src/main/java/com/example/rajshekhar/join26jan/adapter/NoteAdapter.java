package com.example.rajshekhar.join26jan.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.rajshekhar.join26jan.R;
import com.example.rajshekhar.join26jan.model.CategoryNote;
import com.example.rajshekhar.join26jan.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private List<CategoryNote> notes = new ArrayList<>();
    private final ActionCallback callback;

    public NoteAdapter(@NonNull ActionCallback callback) {
        this.callback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onEdit(notes.get(viewHolder.getAdapterPosition()));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CategoryNote note = notes.get(position);
        holder.title.setText(note.getTitle());
        holder.description.setText(note.getDescription());
        holder.category.setText(note.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public Note getNote(int position) {
        return notes.get(position);
    }



    public void setNotes(@NonNull List<CategoryNote> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public interface ActionCallback {
        void onEdit(CategoryNote note);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, category;

        ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            category = (TextView) itemView.findViewById(R.id.category);
        }
    }
}
