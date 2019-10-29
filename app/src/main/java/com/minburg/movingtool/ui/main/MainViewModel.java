package com.minburg.movingtool.ui.main;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.minburg.movingtool.models.Category;
import com.minburg.movingtool.models.PersonalItem;
import com.minburg.movingtool.models.SortType;
import com.minburg.movingtool.repositories.PersonalItemRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private LiveData<List<PersonalItem>> allPersonalItems;
    private LiveData<List<Category>> allCategories;
    private PersonalItemRepository repository;
    private MutableLiveData<SortType> sortType = new MutableLiveData<>();


    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new PersonalItemRepository(application);
        sortType.setValue(SortType.Default);
//        sortType.postValue(SortType.Default);

//        allPersonalItems = Transformations.switchMap(sortType, input -> {
//            final MediatorLiveData<List<PersonalItem>> result = new MediatorLiveData<>();
//            result.addSource(repository.getAllItemsSorted(input), tasks -> result.setValue(repository.getAllItemsSorted(input).getValue()));
//            return result;
//        });

        allPersonalItems = Transformations.switchMap(sortType, new Function<SortType, LiveData<List<PersonalItem>>>() {
            @Override
            public LiveData<List<PersonalItem>> apply(SortType input) {
                return repository.getAllItemsSorted(input);
            }
        });

        allCategories = Transformations.switchMap(sortType, new Function<SortType, LiveData<List<Category>>>() {
            @Override
            public LiveData<List<Category>> apply(SortType input) {
                return repository.getAllICategoriesSorted();
            }
        });

    }

    public void insert(PersonalItem personalItem) {
        repository.insert(personalItem);
    }
    public void insert(Category category) {
        repository.insert(category);
    }

    public void update(PersonalItem personalItem) {
        repository.update(personalItem);
    }

    public void delete(PersonalItem personalItem) {
        repository.delete(personalItem);
    }
    public void delete(Category category) {
        repository.delete(category);
    }

    public void deleteAllItems() {
        repository.deleteAll();
    }

    public LiveData<List<PersonalItem>> getAllItemsWithSorting() {
        return allPersonalItems;
    }
    public LiveData<List<Category>> getAllCategoriesWithSorting() {
        return allCategories;
    }

    public void sortItems(SortType sort) {

        sortType.setValue(sort);
        sortType.postValue(sort);
    }


/*    public void addNewValue(final PersonalItem personalItem) {
        mIsUpdating.setValue(true);

        List<PersonalItem> currentPersons = mPersonalItems.getValue();
        currentPersons.add(personalItem);
        mPersonalItems.postValue(currentPersons);
        Log.e("added values..", "dfg");
        for(PersonalItem p : mPersonalItems.getValue()){
            Log.e(p.getId() + ": ", p.getName());
        }
        mIsUpdating.postValue(false);
    }*/

/*    public void sortDataByName() {

        mIsUpdating.setValue(true);
        List<PersonalItem> currentPersons = mPersonalItems.getValue();
        for(PersonalItem p : currentPersons){
            Log.e(p.getId() + ": ", p.getName());
        }
        List<PersonalItem> result = currentPersons
                .stream()
                .sorted((object1, object2) -> object1.getName().compareTo(object2.getName()))
                .collect(Collectors.toList());

        for(PersonalItem p : result){
            Log.e(p.getId() + ": ", p.getName());
        }
        result.add(new PersonalItem("zocken", 3, Category.Crafting_Material, Ownership.Keep, "d", "d"));
        mPersonalItems.setValue(result);
        for(PersonalItem p : mPersonalItems.getValue()){
            Log.e(p.getId() + ": ", p.getName());
        }
        mIsUpdating.postValue(false);
    }*/


/*    public void deleteValue(int uid) {
        mIsUpdating.setValue(true);
        List<PersonalItem> currentPersons = mPersonalItems.getValue();
        currentPersons.removeIf(obj -> obj.getId() == uid);
        mPersonalItems.postValue(currentPersons);
        Log.e("deleted values..", "dfg");
        for(PersonalItem p : mPersonalItems.getValue()){
            Log.e(p.getId() + ": ", p.getName());
        }
        mIsUpdating.postValue(false);

    }*/
}
