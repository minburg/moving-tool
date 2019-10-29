package com.minburg.movingtool.models;

import androidx.room.TypeConverter;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Converters {

    private static DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;


    @TypeConverter
    public static OffsetDateTime toOffsetDateTime(String value) {
        return OffsetDateTime.parse(value, formatter);
    }

    @TypeConverter
    public static String fromOffsetDateTime(OffsetDateTime date) {
        return date.format(formatter);
    }

    @TypeConverter
    public static Category toCategory(String value) {
        return new Category(value);
    }

    @TypeConverter
    public static String toString(Category value) {
        return value.getName();
    }

    @TypeConverter
    public static Ownership toOwnership(String value) {
        return Ownership.valueOf(value);
    }

    @TypeConverter
    public static String toString(Ownership value) {
        return value.toString();
    }
}
