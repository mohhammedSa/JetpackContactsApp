package com.example.contactsapp.BackEnd

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.example.contactsapp.ContactData

const val DB_NAME = "ContactDB"
const val TABLE_NAME = "Contact_table"
const val ID_COL = "id"
const val NAME_COL = "Name"
const val PH_NUMBER = "Ph_number"

class SQLiteDB(private val context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1) {
    override fun onCreate(p0: SQLiteDatabase?) {
        val createTable =
            "CREATE TABLE $TABLE_NAME ( $ID_COL INTEGER PRIMARY KEY , $NAME_COL TEXT, $PH_NUMBER TEXT )"
        p0?.execSQL(createTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        val delete = "DROP TABLE IF EXISTS $TABLE_NAME"
        p0?.execSQL(delete)
    }

    fun addContact(data: ContactData) {
        val db = this.writableDatabase
        val content = ContentValues()
        content.put(NAME_COL, data.name)
        content.put(PH_NUMBER, data.phNumber)
        db.insert(TABLE_NAME, null, content)
        db.close()
    }

    @SuppressLint("Range")
    fun readData(): ArrayList<ContactData> {
        val list = ArrayList<ContactData>()
        val db = this.readableDatabase
        val query = "select * from $TABLE_NAME"
        val result = db.rawQuery(query, null)
        if (result.moveToNext()) {
            do {
                val id = result.getInt(result.getColumnIndex(ID_COL))
                val name = result.getString(result.getColumnIndex(NAME_COL))
                val number = result.getString(result.getColumnIndex(PH_NUMBER))
                val contactData = ContactData(id, name, number)
                list.add(contactData)
            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return list
    }

    fun deleteContact(id: Int): Boolean {
        val db = writableDatabase
        val selection = "$ID_COL = ?"
        val selectionArgs = arrayOf(id.toString())
        val deletedRow = db.delete(TABLE_NAME, selection, selectionArgs)
        db.close()
        return deletedRow > 0
    }

    fun updateContact(data: ContactData): Boolean {
        val db = writableDatabase
        val content = ContentValues()
        content.put(ID_COL, data.id)
        content.put(NAME_COL, data.name)
        content.put(PH_NUMBER, data.phNumber)
        val selection = "$ID_COL = ?"
        val selectionArgs = arrayOf(data.id.toString())
        val updatedRow = db.update(TABLE_NAME, content, selection, selectionArgs)
        db.close()
        return updatedRow > 0
    }
}