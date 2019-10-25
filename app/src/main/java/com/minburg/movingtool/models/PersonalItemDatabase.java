package com.minburg.movingtool.models;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = PersonalItem.class, version = 1)
public abstract class PersonalItemDatabase extends RoomDatabase {

    private static PersonalItemDatabase instance;

    public abstract PersonalItemsDao personalItemsDao();

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

        private PopulateDbAsyncTask(PersonalItemDatabase db){
            personalItemsDao = db.personalItemsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            personalItemsDao.insert(new PersonalItem("0", 35, Category.Crafting_Material, Ownership.Keep, "deag", "sdgd"));
//            personalItemsDao.insert(new PersonalItem("1", 3315, Category.Tools, Ownership.Keep, "deag", "sdgd"));
//            personalItemsDao.insert(new PersonalItem("2", 15, Category.Clothing, Ownership.Keep, "deag", "sdgd"));
//            personalItemsDao.insert(new PersonalItem("3", 35, Category.Crafting_Material, Ownership.Keep, "deag", "sdgd"));
//            personalItemsDao.insert(new PersonalItem("4", 3315, Category.Tools, Ownership.Keep, "deag", "sdgd"));
//            personalItemsDao.insert(new PersonalItem("5", 3315, Category.Tools, Ownership.Keep, "deag", "sdgd"));


            return null;
        }
    }
}
