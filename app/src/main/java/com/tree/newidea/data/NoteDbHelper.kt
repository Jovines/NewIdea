package com.tree.newidea.data

import android.content.Context
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.tree.newidea.bean.NotepadBean
import org.jetbrains.anko.db.NULL
import androidx.room.RoomMasterTable.TABLE_NAME
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.room.RoomMasterTable.TABLE_NAME





class NoteDbHelper(
    context: Context?,
    private val DATEBASE_NAME: String = "note.db",
    private val DATABASE_VERSION: Int = 1
    ) : SQLiteOpenHelper(context, DATEBASE_NAME, null, DATABASE_VERSION) {

    val noteBeanName = NotepadBean::class.java

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val SQL_CREATE_NOTES_TABLE = ("CREATE TABLE " + NotepadEntry.TABLE_NAME + " ("
                + NotepadEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NotepadEntry.TILTE + " TEXT NOT NULL, "
                + NotepadEntry.TEXT + " TEXT, "
                + NotepadEntry.DATE + " TEXT NOT NULL, "
                + NotepadEntry.IS_MARKDOWN + " TINYINT DEFAULT 0, "
                + NotepadEntry.IS_UNDO + " TINYINT DEFAULT 0);")
        sqLiteDatabase.execSQL(SQL_CREATE_NOTES_TABLE)

    }



    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {

    }
}
