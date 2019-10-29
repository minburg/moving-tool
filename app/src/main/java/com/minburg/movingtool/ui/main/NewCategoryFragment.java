package com.minburg.movingtool.ui.main;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.minburg.movingtool.adapters.RecyclerAdapter;

public class NewCategoryFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager rvLayoutManager;
    RecyclerAdapter recyclerAdapter;
    FloatingActionButton fab;
}
