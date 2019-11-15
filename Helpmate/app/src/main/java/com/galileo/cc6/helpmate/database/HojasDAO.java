package com.galileo.cc6.helpmate.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.Spannable;
import android.util.Log;

import com.galileo.cc6.helpmate.activities.BitmapConverter;
import com.galileo.cc6.helpmate.dataModels.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.galileo.cc6.helpmate.database.DBHandler.HOJAS_COURSE_ID;
import static com.galileo.cc6.helpmate.database.DBHandler.KEY_DATE_UPDATED;
import static com.galileo.cc6.helpmate.database.DBHandler.KEY_ID;
import static com.galileo.cc6.helpmate.database.DBHandler.KEY_IMAGE;
import static com.galileo.cc6.helpmate.database.DBHandler.KEY_NOTE_TITLE;
import static com.galileo.cc6.helpmate.database.DBHandler.KEY_SPANNABLE_NOTE;
import static com.galileo.cc6.helpmate.database.DBHandler.TABLE_NOTES;

/**
 * Created by Santiago on 28/10/2016.
 */

public class HojasDAO {
    public static final String TAG = "HojasDAO";

    private static final DateFormat dt = new SimpleDateFormat("dd.MM.yyyy, hh:mm:ss", Locale.getDefault());

    private SQLiteDatabase db;
    private DBHandler dbh;
    private Context ctx;
    private String[] cAllColumns = {
            KEY_ID,
            KEY_NOTE_TITLE,
            KEY_SPANNABLE_NOTE,
            KEY_IMAGE,
            KEY_DATE_UPDATED,
            HOJAS_COURSE_ID
    };
    public HojasDAO(Context ctx){
        this.ctx = ctx;
        dbh = new DBHandler(ctx);
        try{
            open();
        }catch (SQLException e) {
            Log.e(TAG, "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void open() throws SQLException{
        db= dbh.getWritableDatabase();
    }
    public void close() {
        dbh.close();
    }

    /**
     * Method used to clear notes table
     */
    public void clearAllNotesByCourse(long courseID) {
        db = dbh.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NOTES+ " WHERE "+HOJAS_COURSE_ID+" = "+courseID);
    }
    public void clearAllNotes() {
        db = dbh.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NOTES);
    }
    /**
     * Method used to put Note object into Database
     * @param note Note object to put into DB
     */
    public void createNote(Note note) {
        db = dbh.getWritableDatabase();
        String spannableAsHtml = Html.toHtml(note.getSpannable());
        String date = dt.format(new Date());

        ContentValues values = new ContentValues();

        values.put(KEY_SPANNABLE_NOTE, spannableAsHtml);
        values.put(KEY_NOTE_TITLE, note.getTitle());
        values.put(KEY_IMAGE, BitmapConverter.getBytes(note.getImage()));
        values.put(KEY_DATE_UPDATED, date);
        values.put(HOJAS_COURSE_ID, note.getCourseID());
        if(!db.isOpen()){
            db=dbh.getWritableDatabase();
        }
        db.insert(TABLE_NOTES, null, values);
        db.close();
    }

    /**
     * Method used to get specified Note from Database
     * @param id KEY_ID of Note to get from Database
     * @return Note object with specified KEY_ID
     */
    public Note getNote(int id) throws SQLiteException {
        db = dbh.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTES, new String[]{KEY_ID, KEY_SPANNABLE_NOTE, KEY_IMAGE, KEY_DATE_UPDATED, KEY_NOTE_TITLE, HOJAS_COURSE_ID}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (!cursor.moveToFirst()) {
            deleteNote(id); //Failed to load note. Maybe the user restored data from an incompatible backup?
            //Let's delete the problematic data
            throw new SQLiteException("Note doesn't exist");
        }

        String spannableAsHtml = cursor.getString(cursor.getColumnIndex(KEY_SPANNABLE_NOTE));
        // :)))))
        Spannable spannable = (Spannable) Html.fromHtml(Html.toHtml(Html.fromHtml(spannableAsHtml)));

        Bitmap image = BitmapConverter.getImage(cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE)));

        //Default val
        Date date;
        long course = cursor.getLong(cursor.getColumnIndex(HOJAS_COURSE_ID));
        try {
            date = dt.parse(cursor.getString(cursor.getColumnIndex(KEY_DATE_UPDATED)));
        } catch (Exception e) {
            date = new Date();
            e.printStackTrace();
        }

        String title;
        try {
            title = cursor.getString(cursor.getColumnIndex(KEY_NOTE_TITLE));
        }catch (Exception e){
            title = "";
            e.printStackTrace();
        }

        if (spannable.length() >= 2) {
            spannable = (Spannable) spannable.subSequence(0, spannable.length() - 2);
        }


        db.close();
        cursor.close();
        return new Note(id, title, spannable, image, date,course);
    }

    /**
     * Method used to delete specified Note from Database
     * @param note Note to delete
     */
    public void deleteNote(Note note) {
        deleteNote(note.getId());
    }

    public void deleteNote(int noteId) {
        db = dbh.getWritableDatabase();
        db.delete(TABLE_NOTES, KEY_ID + "=?", new String[]{String.valueOf(noteId)});
        db.close();
    }

    /**
     * Method used to get count of notes in Database
     * @return count of notes in Database
     */
    public int getNoteCount() {
        db = dbh.getReadableDatabase();
        int numberOfNotes = (int) DatabaseUtils.queryNumEntries(db, TABLE_NOTES);
        db.close();
        return numberOfNotes;
    }

    /**
     * Method used to update Note's text/format
     * @param note Note to update
     * @return updated Note
     */
    public int updateNote(Note note) {
        db = dbh.getWritableDatabase();

        String spannableAsHtml = Html.toHtml(note.getSpannable());

        String date = dt.format(new Date());

        ContentValues values = new ContentValues();
        values.put(KEY_IMAGE, BitmapConverter.getBytes(note.getImage()));
        values.put(KEY_DATE_UPDATED, date);
        values.put(KEY_SPANNABLE_NOTE, spannableAsHtml);
        values.put(KEY_NOTE_TITLE, note.getTitle());
        values.put(HOJAS_COURSE_ID,(int)note.getCourseID());
        Log.d(TAG, "note course : " +note.getTitle() +":"+note.getCourseID());
        return db.update(TABLE_NOTES, values, KEY_ID + "=?", new String[]{String.valueOf(note.getId())});

    }

    /**
     * Method used to get all notes in Database
     * @return ArrayList of Notes, containing all notes in Database
     */
    public ArrayList<Note> getAllNotesAsArrayList() {
        ArrayList<Note> notes = new ArrayList<>();

        db = dbh.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES, null);

        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                Spannable spannable = (Spannable) Html.fromHtml(cursor.getString(cursor.getColumnIndex(KEY_SPANNABLE_NOTE)));
                Bitmap image = BitmapConverter.getImage(cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE)));
                //Default val
                Date date;
                long course = cursor.getLong(cursor.getColumnIndex(HOJAS_COURSE_ID));

                try {
                    date = dt.parse(cursor.getString(cursor.getColumnIndex(KEY_DATE_UPDATED)));
                } catch (Exception e) {
                    date = new Date();
                    e.printStackTrace();
                }

                String title;
                try {
                    title = cursor.getString(cursor.getColumnIndex(KEY_NOTE_TITLE));
                } catch (Exception e) {
                    title = "";
                    e.printStackTrace();
                }

                Note note = new Note(id, title, spannable, image, date,course);
                notes.add(note);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return notes;
    }
    public ArrayList<Note> getAllNotesByCourse(long courseID) {
        ArrayList<Note> notes = new ArrayList<>();

        db = dbh.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES+ " WHERE "+HOJAS_COURSE_ID+" = "+courseID, null);

        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                Spannable spannable = (Spannable) Html.fromHtml(cursor.getString(cursor.getColumnIndex(KEY_SPANNABLE_NOTE)));
                Bitmap image = BitmapConverter.getImage(cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE)));
                //Default val
                Date date;
                long course = cursor.getLong(cursor.getColumnIndex(HOJAS_COURSE_ID));

                try {
                    date = dt.parse(cursor.getString(cursor.getColumnIndex(KEY_DATE_UPDATED)));
                } catch (Exception e) {
                    date = new Date();
                    e.printStackTrace();
                }

                String title;
                try {
                    title = cursor.getString(cursor.getColumnIndex(KEY_NOTE_TITLE));
                } catch (Exception e) {
                    title = "";
                    e.printStackTrace();
                }

                Note note = new Note(id, title, spannable, image, date,course);
                notes.add(note);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return notes;
    }

    /**
     * Method used to get all notes in Database
     * @return Array of Notes, containing all notes in Database
     */
    public Note[] getAllNotesAsArray() {
        ArrayList<Note> notes = new ArrayList<>();

        db = dbh.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NOTES, null);

        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                Spannable spannable = (Spannable) Html.fromHtml(cursor.getString(cursor.getColumnIndex(KEY_SPANNABLE_NOTE)));
                Bitmap image = BitmapConverter.getImage(cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE)));
                //Default val
                Date date;
                long course = cursor.getLong(cursor.getColumnIndex(HOJAS_COURSE_ID));

                try {
                    date = dt.parse(cursor.getString(cursor.getColumnIndex(KEY_DATE_UPDATED)));
                } catch (Exception e) {
                    date = new Date();
                    e.printStackTrace();
                }

                String title;
                try {
                    title = cursor.getString(cursor.getColumnIndex(KEY_NOTE_TITLE));
                } catch (Exception e) {
                    title = "";
                    e.printStackTrace();
                }

                Note note = new Note(id, title, spannable, image, date,course);
                notes.add(note);
            }
            while (cursor.moveToNext());
        }

        Note[] result = new Note[notes.size()];

        for (int i = 0; i < notes.size(); i++) {
            result[i] = notes.get(i);
        }
        cursor.close();
        return result;
    }
}
