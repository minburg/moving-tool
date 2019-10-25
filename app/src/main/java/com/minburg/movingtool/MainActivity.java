package com.minburg.movingtool;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.minburg.movingtool.ui.main.MainFragment;
import com.minburg.movingtool.ui.main.NewPersonalItemFragment;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final String TAG = "Tag";

    private File photoFile;

    /*
    * Todo:
    *  Custom Appbar Drawable
    * Multiline Textview for name etc
    * Appp Drawer for Categories
    * custom categories
    *
    * */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
    }

    @Override
    public void onBackPressed() {
//        getSupportFragmentManager().popBackStack();

        super.onBackPressed();
    }

    public void setImageFile(File uri){
        photoFile = uri;
    }

    public File getImageFile(){
        return photoFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            if(photoFile != null) {

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, NewPersonalItemFragment.newInstance())
                        .commitNow();
            }
        }
    }
}
