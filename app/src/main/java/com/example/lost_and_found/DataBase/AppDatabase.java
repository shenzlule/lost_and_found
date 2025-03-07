package com.example.lost_and_found.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.lost_and_found.models.Item;
import com.example.lost_and_found.models.ItemDao;

@Database(entities = {Item.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract ItemDao itemDao();

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
