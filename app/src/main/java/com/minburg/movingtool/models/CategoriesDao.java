package com.minburg.movingtool.models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CategoriesDao {

    @Insert
    void insert(Category category);

    @Update
    void update(Category category);

    @Delete
    void delete(Category category);

    @Query("DELETE FROM categories_table")
    void deleteAllPersonalItems();

//    @Query("SELECT * FROM personal_items_table ORDER BY datetime(date)")
//    LiveData<List<PersonalItem>> getSortedDefault();

    @Query("SELECT * FROM categories_table ORDER BY name ASC")
    LiveData<List<Category>> getSortedDefault();

}
