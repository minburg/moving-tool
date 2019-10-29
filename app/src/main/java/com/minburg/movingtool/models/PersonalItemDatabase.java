package com.minburg.movingtool.models;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {PersonalItem.class, Category.class}, version = 2, exportSchema = false)
public abstract class PersonalItemDatabase extends RoomDatabase {

    private static PersonalItemDatabase instance;

    public abstract PersonalItemsDao personalItemsDao();
    public abstract CategoriesDao categoriesDao();

    public static synchronized PersonalItemDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), PersonalItemDatabase.class, "personal_items_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private PersonalItemsDao personalItemsDao;
        private CategoriesDao categoriesDao;

        private PopulateDbAsyncTask(PersonalItemDatabase db){
            personalItemsDao = db.personalItemsDao();
            categoriesDao = db.categoriesDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            categoriesDao.insert(new Category("Default"));
//            personalItemsDao.insert(new PersonalItem("0", 35, new Category("Essen"), Ownership.Keep, "deag", "sdgd"));
//            personalItemsDao.insert(new PersonalItem("1", 3315, new Category("Laufen"), Ownership.Keep, "deag", "sdgd"));


            return null;
        }
    }
}
