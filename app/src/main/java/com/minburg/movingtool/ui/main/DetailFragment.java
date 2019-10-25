package com.minburg.movingtool.ui.main;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.minburg.movingtool.models.PersonalItem;

public class DetailFragment extends Fragment {
    protected MainViewModel mViewModel;
    protected FloatingActionButton fab;
    protected EditText editTextName;
    protected EditText editTextValue;
    protected RadioButton radioKeep;
    protected RadioButton radioSell;
    protected RadioButton radioStore;
    protected ImageView imageView;
    protected Spinner spinner;
    protected RadioGroup radioGroup;
    protected PersonalItem newItem;


    protected void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        View focusedView = getActivity().getCurrentFocus();

        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
