package com.minburg.movingtool.repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.minburg.movingtool.models.CategoriesDao;
import com.minburg.movingtool.models.Category;
import com.minburg.movingtool.models.PersonalItem;
import com.minburg.movingtool.models.PersonalItemDatabase;
import com.minburg.movingtool.models.PersonalItemsDao;
import com.minburg.movingtool.models.SortType;

import java.util.List;


public class PersonalItemRepository {

    private PersonalItemsDao personalItemsDao;
    private CategoriesDao categoriesDao;

    private LiveData<List<PersonalItem>> allPersonalItems;
    private LiveData<List<Category>> allCategories;

    public PersonalItemRepository(Application application) {
        PersonalItemDatabase database = PersonalItemDatabase.getInstance(application);
        personalItemsDao = database.personalItemsDao();
        allPersonalItems = personalItemsDao.getSortedDefault();
        categoriesDao = database.categoriesDao();
        allCategories = categoriesDao.getSortedDefault();
    }

    public void insert(PersonalItem personalItem) {
        new InsertPersonalItemAsyncTask(personalItemsDao).execute(personalItem);
    }

    public void insert(Category category) {
        new InsertCategoryAsyncTask(categoriesDao).execute(category);
    }


    public void update(PersonalItem personalItem) {
        new UpdatePersonalItemAsyncTask(personalItemsDao).execute(personalItem);
    }


    public void delete(PersonalItem personalItem) {
        new DeletePersonalItemAsyncTask(personalItemsDao).execute(personalItem);
    }

    public void delete(Category category) {
        new DeleteCategoryAsyncTask(categoriesDao).execute(category);
    }

    public void deleteAll() {
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

    public LiveData<List<Category>> getAllICategoriesSorted() {

        allCategories = categoriesDao.getSortedDefault();

        return allCategories;
    }


    private static class InsertPersonalItemAsyncTask extends AsyncTask<PersonalItem, Void, Void> {
        private PersonalItemsDao personalItemsDao;

        private InsertPersonalItemAsyncTask(PersonalItemsDao personalItemsDao) {
            this.personalItemsDao = personalItemsDao;
        }

        @Override
        protected Void doInBackground(PersonalItem... personalItems) {
            personalItemsDao.insert(personalItems[0]);
            return null;
        }
    }

    private static class InsertCategoryAsyncTask extends AsyncTask<Category, Void, Void> {
        private CategoriesDao categoriesDao;

        private InsertCategoryAsyncTask(CategoriesDao categoriesDao) {
            this.categoriesDao = categoriesDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoriesDao.insert(categories[0]);
            return null;
        }
    }

    private static class UpdatePersonalItemAsyncTask extends AsyncTask<PersonalItem, Void, Void> {
        private PersonalItemsDao personalItemsDao;

        private UpdatePersonalItemAsyncTask(PersonalItemsDao personalItemsDao) {
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

        private DeletePersonalItemAsyncTask(PersonalItemsDao personalItemsDao) {
            this.personalItemsDao = personalItemsDao;
        }

        @Override
        protected Void doInBackground(PersonalItem... personalItems) {
            personalItemsDao.delete(personalItems[0]);
            return null;
        }
    }

    private static class DeleteCategoryAsyncTask extends AsyncTask<Category, Void, Void> {
        private CategoriesDao categoriesDao;

        private DeleteCategoryAsyncTask(CategoriesDao categoriesDao) {
            this.categoriesDao = categoriesDao;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            categoriesDao.delete(categories[0]);
            return null;
        }
    }

    private static class DeleteAllPersonalItemAsyncTask extends AsyncTask<PersonalItem, Void, Void> {
        private PersonalItemsDao personalItemsDao;

        private DeleteAllPersonalItemAsyncTask(PersonalItemsDao personalItemsDao) {
            this.personalItemsDao = personalItemsDao;
        }

        @Override
        protected Void doInBackground(PersonalItem... personalItems) {
            personalItemsDao.deleteAllPersonalItems();
            return null;
        }
    }


}
