package com.minburg.movingtool.ui.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.minburg.movingtool.R;
import com.minburg.movingtool.adapters.CategoryRecyclerAdapter;
import com.minburg.movingtool.adapters.MainRecyclerAdapter;
import com.minburg.movingtool.models.Category;
import com.minburg.movingtool.models.Ownership;
import com.minburg.movingtool.models.PersonalItem;

import java.util.List;

public class CategoriesFragment extends Fragment {

    private MainViewModel mViewModel;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager rvLayoutManager;
    CategoryRecyclerAdapter categoryRecyclerAdapter;
    FloatingActionButton fab;

    private boolean changeIsDeleting = false;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.categories_fragment, container, false);
        setHasOptionsMenu(true);


        recyclerView = rootView.findViewById(R.id.categoryRecyclerView);
        rvLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(rvLayoutManager);

        categoryRecyclerAdapter = new CategoryRecyclerAdapter(getContext(), CategoriesFragment.this);
        recyclerView.setAdapter(categoryRecyclerAdapter);


        mViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
//        mViewModel.sortItems(SortType.Default);
        mViewModel.getAllCategoriesWithSorting().observe(getActivity(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                Log.e("category ", categories.toString());
                setNewData(categories);

            }
        });

        fab = rootView.findViewById(R.id.fab_category);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo: popup for new Category
                showAddItemDialog(getContext());
            }
        });
        return rootView;
    }

    private void showAddItemDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Add a new category")
                .setMessage("What categories do you want to use for your items?")
                .setView(taskEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String category = String.valueOf(taskEditText.getText());
                        mViewModel.insert(new Category(category));

                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    private void setNewData(List<Category> categories) {

        categoryRecyclerAdapter.setItems(categories);

        if (!changeIsDeleting && getActivity() != null) {
            RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(getActivity()) {
                @Override
                protected int getVerticalSnapPreference() {
                    return LinearSmoothScroller.SNAP_TO_END;
                }
            };
            smoothScroller.setTargetPosition(categories.size());
            rvLayoutManager.startSmoothScroll(smoothScroller);
        }
    }

    public void deleteItem(Category category) {
        changeIsDeleting = true;
        mViewModel.delete(category);
    }
}
