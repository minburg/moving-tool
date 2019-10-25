package com.minburg.movingtool.repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.minburg.movingtool.models.PersonalItem;
import com.minburg.movingtool.models.PersonalItemDatabase;
import com.minburg.movingtool.models.PersonalItemsDao;
import com.minburg.movingtool.models.SortType;

import java.util.List;


public class PersonalItemRepository {

    private PersonalItemsDao personalItemsDao;

    private LiveData<List<PersonalItem>> allPersonalItems;

    public PersonalItemRepository(Application application){
        PersonalItemDatabase database = PersonalItemDatabase.getInstance(application);
        personalItemsDao = database.personalItemsDao();
        allPersonalItems = personalItemsDao.getSortedDefault();

    }

    public void insert(PersonalItem personalItem){
        new InsertPersonalItemAsyncTask(personalItemsDao).execute(personalItem);
    }

    public void update(PersonalItem personalItem){
        new UpdatePersonalItemAsyncTask(personalItemsDao).execute(personalItem);
    }

    public void delete(PersonalItem  personalItem){
        new DeletePersonalItemAsyncTask(personalItemsDao).execute(personalItem);
    }

    public void deleteAll(){
        new DeleteAllPersonalItemAsyncTask(personalItemsDao).execute();
    }

    public LiveData<List<PersonalItem>> getAllItemsSorted(SortType sortType) {

        switch (sortType) {
            case Default:
                allPersonalItems = personalItemsDao.getSortedDefault();
                break;
            case Name:
                allPersonalItems = personalItemsDao.getSortedByName();
                break;
            case Category:
                //
                break;
            case Value:
                //
                break;
            case Ownership:
                //
                break;
        }


        return allPersonalItems;
    }

    public LiveData<List<PersonalItem>> getAllItemsSortedDefault() {
        allPersonalItems = personalItemsDao.getSortedDefault();
        return allPersonalItems;
    }

    public LiveData<List<PersonalItem>> getItemsSortedByName() {
        allPersonalItems = personalItemsDao.getSortedByName();
        return allPersonalItems;
    }

    private static class InsertPersonalItemAsyncTask extends AsyncTask<PersonalItem, Void, Void> {
        private PersonalItemsDao personalItemsDao;

        private InsertPersonalItemAsyncTask(PersonalItemsDao personalItemsDao){
            this.personalItemsDao = personalItemsDao;
        }

        @Override
        protected Void doInBackground(PersonalItem... personalItems) {
            personalItemsDao.insert(personalItems[0]);
            return null;
        }
    }

    private static class UpdatePersonalItemAsyncTask extends AsyncTask<PersonalItem, Void, Void> {
        private PersonalItemsDao personalItemsDao;

        private UpdatePersonalItemAsyncTask(PersonalItemsDao personalItemsDao){
            this.personalItemsDao = personalItemsDao;
        }

        @Override
        protected Void doInBackground(PersonalItem... personalItems) {
            personalItemsDao.update(personalItems[0]);
            return null;
        }
    }

    private static class DeletePersonalItemAsyncTask extends AsyncTask<PersonalItem, Void, Void> {
        private PersonalItemsDao personalItemsDao;

        private DeletePersonalItemAsyncTask(PersonalItemsDao personalItemsDao){
            this.personalItemsDao = personalItemsDao;
        }

        @Override
        protected Void doInBackground(PersonalItem... personalItems) {
            personalItemsDao.delete(personalItems[0]);
            return null;
        }
    }

    private static class DeleteAllPersonalItemAsyncTask extends AsyncTask<PersonalItem, Void, Void> {
        private PersonalItemsDao personalItemsDao;

        private DeleteAllPersonalItemAsyncTask(PersonalItemsDao personalItemsDao){
            this.personalItemsDao = personalItemsDao;
        }

        @Override
        protected Void doInBackground(PersonalItem... personalItems) {
            personalItemsDao.deleteAllPersonalItems();
            return null;
        }
    }


}
