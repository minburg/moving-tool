package com.minburg.movingtool.models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface PersonalItemsDao {

    @Insert
    void insert(PersonalItem item);

    @Update
    void update(PersonalItem item);

    @Delete
    void delete(PersonalItem item);

    @Query("DELETE FROM personal_items_table")
    void deleteAllPersonalItems();

//    @Query("SELECT * FROM personal_items_table ORDER BY datetime(date)")
//    LiveData<List<PersonalItem>> getSortedDefault();

    @Query("SELECT * FROM personal_items_table ORDER BY date ASC")
    LiveData<List<PersonalItem>> getSortedDefault();

    @Query("SELECT * FROM personal_items_table ORDER BY name DESC")
    LiveData<List<PersonalItem>> getSortedByName();
}
