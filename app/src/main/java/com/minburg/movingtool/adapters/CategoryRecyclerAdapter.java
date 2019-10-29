package com.minburg.movingtool.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.minburg.movingtool.R;
import com.minburg.movingtool.models.Category;
import com.minburg.movingtool.models.PersonalItem;
import com.minburg.movingtool.ui.main.CategoriesFragment;
import com.minburg.movingtool.ui.main.MainFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.MyViewHolderCategory> {

    private List<Category> allCategories = new ArrayList<>();
    private Context context;
    private CategoriesFragment fragment;

    public CategoryRecyclerAdapter(Context context, CategoriesFragment fragment) {
        this.context = context;
        this.fragment = fragment;
    }


    @NonNull
    @Override
    public MyViewHolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_category, null);
        return new MyViewHolderCategory(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderCategory holder, final int position) {

        holder.textCategory.setText(getItemAt(position).getName());
        holder.deleteItem.setOnLongClickListener(v -> {
            fragment.deleteItem(getItemAt(position));
//                super.notifyDataSetChanged();
            return false;
        });

    }


    private Category getItemAt(int position) {
        return allCategories.get(position);
    }


    @Override
    public int getItemCount() {
        return allCategories.size();
    }

    public void setItems(List<Category> categories) {
        this.allCategories = categories;
        notifyDataSetChanged();
    }

    public class MyViewHolderCategory extends RecyclerView.ViewHolder {

        TextView textCategory;
        ImageView deleteItem;

        public MyViewHolderCategory(View itemView) {
            super(itemView);
            textCategory = itemView.findViewById(R.id.categoryName);
            deleteItem = itemView.findViewById(R.id.deleteItem);
            deleteItem.setVisibility(View.VISIBLE);
        }
    }
}
