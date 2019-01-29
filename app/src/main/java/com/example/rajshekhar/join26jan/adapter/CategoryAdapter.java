package com.example.rajshekhar.join26jan.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;


import com.example.rajshekhar.join26jan.R;
import com.example.rajshekhar.join26jan.model.Category;

import java.util.List;

public class CategoryAdapter extends ArrayAdapter<Category> {
    private final List<Category>categories;

    public CategoryAdapter(@NonNull Context context,@LayoutRes int resource,List<Category>categories){
        super(context,resource,categories);
        this.categories=categories;
    }

    @Override
    public int getCount() {
    return super.getCount()+1;
    }
    @NonNull
    @Override
    public Category getItem(int position){
        if(position ==0){
            Category category = new Category();
            category.setId(0);
            category.setName(getContext().getString(R.string.create_new_depart));
            return category;
        }
        return super.getItem(position-1);
    }
    public  int getCategoryPosition(@NonNull Long categoryId){
        if (categoryId!=null){
            for (int i=0;i<categories.size();i++){
                if(categoryId==categories.get(i).getId()){
                    return i+1;
                }
            }
        }
        return -1;
    }

    }
