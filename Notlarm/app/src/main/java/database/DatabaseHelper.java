package database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import database.model.Note;
import info.notlarm.sqlite.database.model.Note;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Veritabanı versiyonu
    private static final int DATABASE_VERSION = 1;

    // Veritabanı adı
    private static final String DATABASE_NAME = "notes_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Tabloların oluşturulması
    @Override
    public void onCreate(SQLiteDatabase db) {

        // notes tablosunu oluştur
        db.execSQL(Note.CREATE_TABLE);
    }

    // Veritabanının güncellenmesi
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Mevcut notes tablosunu kaldır.
        db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);

        // Veritabanını tekrardan oluştur.
        onCreate(db);
        public long insertNote(String note) {
            // veritabanına erişimde kullanılacak nesne
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            // `id` and `timestamp` otomatik oluşturulduğu için eklemeye gerek yoktur.
            values.put(Note.COLUMN_NOTE, note);

            // yeni kayıt oluştur
            long id = db.insert(Note.TABLE_NAME, null, values);

            // bağlantıyı kapat
            db.close();

            // yeni eklenen kaydın id bilgisini dönebilirsin.
            return id;
        }
        public Note getNote(long id) {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(Note.TABLE_NAME,
                    new String[]{Note.COLUMN_ID, Note.COLUMN_NOTE, Note.COLUMN_TIMESTAMP},
                    Note.COLUMN_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            Note note = new Note(
                    cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)),
                    cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP)));

            // veritabanı bağlantısını kapatalım
            cursor.close();

            return note;
        }

        public List getAllNotes() {
            List notes = new ArrayList<>();

            // Select Sorgusu
            String selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " ORDER BY " +
                    Note.COLUMN_TIMESTAMP + " DESC";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    Note note = new Note();
                    note.setId(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)));
                    note.setNote(cursor.getString(cursor.getColumnIndex(Note.COLUMN_NOTE)));
                    note.setTimestamp(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TIMESTAMP)));

                    notes.add(note);
                } while (cursor.moveToNext());
            }

            // veritabanı bağlantısını kapatalım
            db.close();

            // notların tümünü döner
            return notes;
        }

        public int getNotesCount() {
            String countQuery = "SELECT  * FROM " + Note.TABLE_NAME;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);

            int count = cursor.getCount();
            cursor.close();


            // notların sayısını döner
            return count;
        }

        public int updateNote(Note note) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Note.COLUMN_NOTE, note.getNote());

            // notunu güncelleyelim
            return db.update(Note.TABLE_NAME, values, Note.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(note.getId())});
        }

        public void deleteNote(Note note) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(Note.TABLE_NAME, Note.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(note.getId())});
            db.close();
        }























    }