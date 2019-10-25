package com.minburg.movingtool.ui.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.minburg.movingtool.MainActivity;
import com.minburg.movingtool.R;
import com.minburg.movingtool.adapters.RecyclerAdapter;
import com.minburg.movingtool.models.Ownership;
import com.minburg.movingtool.models.PersonalItem;
import com.minburg.movingtool.models.SortType;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager rvLayoutManager;
    RecyclerAdapter recyclerAdapter;
    FloatingActionButton fab;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_PERMISSION_CAMERA = 246;
    private StorageReference storageRef;
    private String currentPhotoPath;

    static final String TAG = "Tag";

    private boolean changeIsDeleting = false;
    private TextView accumulatedText;


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        setHasOptionsMenu(true);

        accumulatedText = rootView.findViewById(R.id.accumulatedValue);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        rvLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(rvLayoutManager);

        recyclerAdapter = new RecyclerAdapter(getContext(), MainFragment.this);
        recyclerView.setAdapter(recyclerAdapter);


        mViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
//        mViewModel.sortItems(SortType.Default);
        mViewModel.getAllItemsWithSorting().observe(getActivity(), new Observer<List<PersonalItem>>() {
            @Override
            public void onChanged(List<PersonalItem> personalItems) {
                setNewData(personalItems);
                int sum = 0;
                for (PersonalItem p : personalItems) {
                    if (p.getOwnership().equals(Ownership.Sell)) {
                        sum += p.getEstimatedValue();
                    }
                }
                accumulatedText.setText("Total value of items to be sold: " + sum + " €");
            }
        });

        fab = rootView.findViewById(R.id.fab_main);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeNewPicture();
            }
        });
        return rootView;
    }

    private void setNewData(List<PersonalItem> personalItems) {

        recyclerAdapter.setItems(personalItems);

        if (!changeIsDeleting && getActivity() != null) {
            RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(getActivity()) {
                @Override
                protected int getVerticalSnapPreference() {
                    return LinearSmoothScroller.SNAP_TO_END;
                }
            };
            smoothScroller.setTargetPosition(personalItems.size());
            rvLayoutManager.startSmoothScroll(smoothScroller);
        }
    }

    private void takeNewPicture() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA},
                    REQUEST_PERMISSION_CAMERA);
            dispatchTakePictureIntent();
        }
    }

    private void uploadToFirebase() {
        Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        StorageReference riversRef = storageRef.child("images/rivers.jpg");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
//                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    public void editItemFragment(PersonalItem item) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        EditPersonalItemFragment frag = EditPersonalItemFragment.newInstance();
        frag.setObject(item);
        transaction.replace(R.id.container, frag)
                .commitNow();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(getActivity(), "Exception occured with file creation", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(), "com.minburg.movingtool.fileprovider", photoFile);
                ((MainActivity) getActivity()).setImageFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapter.getFilter().filter(newText);
                recyclerAdapter.setCurrentFilter(newText);
                return false;
            }
        });
        //super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    public void deleteItem(PersonalItem item) {
        changeIsDeleting = true;
        mViewModel.delete(item);
    }

    public void sortByName() {
        Log.e("1 fragment ->", "sortByName");
        mViewModel.sortItems(SortType.Default);
    }

    public void sortByValue() {
    }

    public void sortByCategory() {
    }

    public void sortByOwnership() {
    }
}
