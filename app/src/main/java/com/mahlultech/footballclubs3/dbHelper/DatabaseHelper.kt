package com.mahlultech.footballclubs3.dbHelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class DatabaseHelper(ctx: Context): ManagedSQLiteOpenHelper(ctx, "Favorite.db", null, 1){

    companion object {
        private var instance: DatabaseHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): DatabaseHelper {
            if (instance == null) {
                instance = DatabaseHelper(ctx.applicationContext)
            }
            return instance as DatabaseHelper
        }
    }
    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(FavoriteEvent.TABLE_FAVORITE, true,
                FavoriteEvent.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                FavoriteEvent.EVENT_ID to TEXT + UNIQUE,
                FavoriteEvent.ID_HOME to TEXT,
                FavoriteEvent.ID_AWAY to TEXT,
                FavoriteEvent.DATE to TEXT,
                FavoriteEvent.HOME_NAME to TEXT,
                FavoriteEvent.HOME_SKOR to TEXT,
                FavoriteEvent.AWAY_NAME to TEXT,
                FavoriteEvent.AWAY_SKOR to TEXT,
                FavoriteEvent.TIME to TEXT)

        db.createTable(FavoriteTeam.TABLE_TEAM, true,
                FavoriteTeam.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                FavoriteTeam.TEAM_ID to TEXT + UNIQUE,
                FavoriteTeam.TEAM_NAME to TEXT,
                FavoriteTeam.TEAM_BADGE to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(FavoriteEvent.TABLE_FAVORITE, true)
        db.dropTable(FavoriteTeam.TABLE_TEAM, true)
    }
}
val Context.database: DatabaseHelper get() = DatabaseHelper.getInstance(applicationContext)