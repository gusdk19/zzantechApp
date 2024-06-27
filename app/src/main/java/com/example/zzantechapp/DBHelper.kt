package com.example.zzantechapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "goal_status.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "goal_status"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_STATUS = "status"
        private const val COLUMN_GOAL_AMOUNT = "goal_amount"
        private const val COLUMN_USED_AMOUNT = "used_amount"
        private const val TAG = "DBHelper"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_DATE TEXT PRIMARY KEY, "
                + "$COLUMN_STATUS TEXT, "
                + "$COLUMN_GOAL_AMOUNT INTEGER, "
                + "$COLUMN_USED_AMOUNT INTEGER)")
        db?.execSQL(createTable)
        Log.d(TAG, "Database created with table: $TABLE_NAME")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
        Log.d(TAG, "Database upgraded from version $oldVersion to $newVersion")
    }

    fun insertOrUpdateStatus(date: String, status: String, goalAmount: Int, usedAmount: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DATE, date)
            put(COLUMN_STATUS, status)
            put(COLUMN_GOAL_AMOUNT, goalAmount)
            put(COLUMN_USED_AMOUNT, usedAmount)
        }

        val rows = db.update(TABLE_NAME, values, "$COLUMN_DATE = ?", arrayOf(date))
        if (rows == 0) {
            db.insert(TABLE_NAME, null, values)
            Log.d(TAG, "Inserted new status: Date=$date, Status=$status, Goal=$goalAmount, Used=$usedAmount")
        } else {
            Log.d(TAG, "Updated status: Date=$date, Status=$status, Goal=$goalAmount, Used=$usedAmount")
        }
    }

    fun getStatus(date: String): Triple<String, Int, Int>? {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_NAME,
            arrayOf(COLUMN_STATUS, COLUMN_GOAL_AMOUNT, COLUMN_USED_AMOUNT),
            "$COLUMN_DATE = ?",
            arrayOf(date),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS))
            val goalAmount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GOAL_AMOUNT))
            val usedAmount = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USED_AMOUNT))
            cursor.close()
            Log.d(TAG, "Retrieved status: Date=$date, Status=$status, Goal=$goalAmount, Used=$usedAmount")
            Triple(status, goalAmount, usedAmount)
        } else {
            cursor.close()
            Log.d(TAG, "No status found for Date=$date")
            null
        }
    }
}
