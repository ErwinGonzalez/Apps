package com.galileo.cc6.helpmate.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by EEVGG on 19/10/2016.
 */

public class DBHandler extends SQLiteOpenHelper {

    /*
    Class used to manage SQLite access,
    database version and name are declared as variables to facilitate upgrade
    four tables created
        >static final String COURSE_TABLE = "courses";
        >static final String SCHEDULE_TABLE = "schedule";
        >static final String ASSIGNMENT_TABLE = "assingments";
        >static final String TABLE_NOTES = "notes";

     for each table there's a Data Access Object that manages the database queries
     */


    private static final int DATABASE_VERSION =8;
    private static final String DATABASE_NAME = "CoursesDB";

    static final String COURSE_TABLE = "courses";
    static final String COURSE_ID = "id";
    static final String COURSE_NAME = "name";
    static final String COURSE_COLOR = "color";

    static final String SCHEDULE_TABLE = "schedule";
    static final String SCHEDULE_ID = "id";
    static final String SCHEDULE_DAY = "day";
    static final String SCHEDULE_START = "start";
    static final String SCHEDULE_END = "end";
    static final String SCHEDULE_COURSE_ID = "curso";

    static final String ASSIGNMENT_TABLE = "assingments";
    static final String ASSIGNMENT_ID = "id";
    static final String ASSIGNMENT_TITLE = "title";
    static final String ASSIGNMENT_DESCRIPTION = "description";
    static final String ASSIGNMENT_COURSE_ID = "curso";
    static final String ASSIGNMENT_DATE = "date";
    static final String ASSIGNMENT_PRIORITY = "priority";
    static final String ASSIGNMENT_REPEAT = "repeat";

    static final String TABLE_NOTES = "notes";
    static final String KEY_ID = "id";
    static final String KEY_NOTE_TITLE = "noteTitle";
    static final String KEY_SPANNABLE_NOTE = "serializedSpannableNote";
    static final String KEY_IMAGE = "image";
    static final String KEY_DATE_UPDATED = "dateUpdated";
    static final String HOJAS_COURSE_ID = "idCurso";


    DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS "+COURSE_TABLE+"(" +
                        COURSE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        COURSE_NAME+" TEXT," +
                        COURSE_COLOR+" TEXT )"
        );
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS "+SCHEDULE_TABLE+"(" +
                        SCHEDULE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        SCHEDULE_DAY+" INTEGER," +
                        SCHEDULE_START+" TEXT," +
                        SCHEDULE_END+" TEXT," +
                        SCHEDULE_COURSE_ID+" INTEGER )"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ASSIGNMENT_TABLE+"(" +
                        ASSIGNMENT_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                        ASSIGNMENT_TITLE+" TEXT,"+
                        ASSIGNMENT_DESCRIPTION+" TEXT," +
                        ASSIGNMENT_COURSE_ID+" INTEGER," +
                        ASSIGNMENT_DATE+" TEXT," +
                        ASSIGNMENT_PRIORITY+" INTEGER," +
                        ASSIGNMENT_REPEAT+" TEXT )"
        );
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_SPANNABLE_NOTE + " TEXT, "
                + KEY_IMAGE + " BLOB, "
                + KEY_DATE_UPDATED + " TEXT, "
                + KEY_NOTE_TITLE + " VARCHAR(100),"
                + HOJAS_COURSE_ID + " INTEGER )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+COURSE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+SCHEDULE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS "+ASSIGNMENT_TABLE);
        onCreate(db);
    }
    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

}
