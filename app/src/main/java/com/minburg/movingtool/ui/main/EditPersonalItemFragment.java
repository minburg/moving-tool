package com.minburg.movingtool.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.minburg.movingtool.MainActivity;
import com.minburg.movingtool.R;
import com.minburg.movingtool.models.Category;
import com.minburg.movingtool.models.Ownership;
import com.minburg.movingtool.models.PersonalItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EditPersonalItemFragment extends DetailFragment {
    private PersonalItem personalItem;
    private List<Category> categories = new ArrayList<>();


    public static EditPersonalItemFragment newInstance() {

        return new EditPersonalItemFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.new_personal_item_fragment, container, false);

        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mViewModel.getAllCategoriesWithSorting().observe(getActivity(), new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                setNewData(categories);

            }
        });

        personalItem = EditPersonalItemFragmentArgs.fromBundle(getArguments()).getItem();

        editTextName = rootView.findViewById(R.id.edit_text_name);
        editTextName.setText(personalItem.getName());
        editTextValue = rootView.findViewById(R.id.edit_text_value);
        editTextValue.setVisibility(View.INVISIBLE);
        editTextValue.setText(String.valueOf(personalItem.getEstimatedValue()));
        radioKeep = rootView.findViewById(R.id.radio_keep);
        radioKeep.setChecked(true);
        radioSell = rootView.findViewById(R.id.radio_sell);
        radioSell.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editTextValue.setVisibility(View.VISIBLE);
                }else{
                    editTextValue.setText("");
                    editTextValue.setVisibility(View.INVISIBLE);
                }
            }
        });
        radioStore = rootView.findViewById(R.id.radio_store);

        switch (personalItem.getOwnership()) {
            case Keep:
                radioKeep.setChecked(true);
                break;
            case Sell:
                radioSell.setChecked(true);
                break;
            case Store_Somewhere:
                radioStore.setChecked(true);
                break;
        }

        imageView = rootView.findViewById(R.id.new_image_view);
        spinner = rootView.findViewById(R.id.spinnerCategory);
        radioGroup = (RadioGroup) rootView.findViewById(R.id.radio_group);

        ArrayAdapter<Category> adapter = new ArrayAdapter<Category>(getActivity().getBaseContext(), R.layout.simple_spinner_item, R.id.tvSpinner, categories);
        spinner.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(personalItem.getCategory());
        spinner.setSelection(spinnerPosition);

        final File file = new File(personalItem.getLocalStorage());

        if (file.exists()) {
            Glide
                    .with(this)
                    .load(file)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView);
        }


        fab = rootView.findViewById(R.id.fab_confirm);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = radioGroup.findViewById(radioButtonID);
                String selectedText = (String) radioButton.getText();
                Ownership ownership;
                if (selectedText.equals(getResources().getString(R.string.keep))) {
                    ownership = Ownership.Keep;
                } else if (selectedText.equals(getResources().getString(R.string.sell))) {
                    ownership = Ownership.Sell;
                } else {
                    ownership = Ownership.Store_Somewhere;
                }

                boolean nameNotEmpty = true;
                boolean valueNotEmpty = true;
                if (editTextName.getText().toString().equals("")) {
                    nameNotEmpty = false;
                    editTextName.setError("Name cannot be empty!");
                }
                if (editTextValue.getText().toString().equals("") && ownership.equals(Ownership.Sell)) {
                    valueNotEmpty = false;
                    editTextValue.setError("Value for items to sell cannot be empty!");
                }
                String value = editTextValue.getText().toString();
                if (editTextValue.getText().toString().equals("") && !ownership.equals(Ownership.Sell)) {
                    value = "0";
                }

                if (nameNotEmpty && valueNotEmpty) {


                    Category category = new Category(spinner.getSelectedItem().toString());
                    newItem = new PersonalItem(editTextName.getText().toString(), Integer.parseInt(value), category, ownership, file.getPath(), "");
                    newItem.setId(personalItem.getId());
                    newItem.setDate(personalItem.getDate());
                    mViewModel.update(newItem);

                    //Close keyBoard in transition
                    hideKeyboard();

                    Navigation.findNavController(getView()).navigate(R.id.nav_home);

//                    getActivity().getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.fragment_container, MainFragment.newInstance())
//                            .commitNow();
                }
            }
        });

        return rootView;
    }

    private void setNewData(List<Category> categories) {
        this.categories = categories;
        spinner.setAdapter(new ArrayAdapter<Category>(getActivity().getBaseContext(), R.layout.simple_spinner_item, R.id.tvSpinner, this.categories));
    }
}
