package cat.judith.stopsmoking;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CigarettesSmokedDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String INT_TYPE = " INTEGER";
    public static final String COMMA_SEP = ",";

    public static final String TABLE_NAME = "SMOKED_CIGS_PER_DAY";
    public static final String COLUMN_NAME_ENTRY_ID = "DATE";
    public static final String COLUMN_NAME_SMOKED = "CIGS_SMOKED";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    "_ID INTEGER PRIMARY KEY," +
                    COLUMN_NAME_ENTRY_ID + INT_TYPE + " UNIQUE " + COMMA_SEP +
                    COLUMN_NAME_SMOKED + INT_TYPE +
                    " )";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String SQL_GET_SUM = " SELECT SUM( " + COLUMN_NAME_SMOKED + " ) "
            + " FROM " + TABLE_NAME;
    public static final String SQL_GET_DAYS_SMOKED = "SELECT COUNT(*) "
            + " FROM " + TABLE_NAME
            + " WHERE " + COLUMN_NAME_SMOKED + " != 0";

    public CigarettesSmokedDB(Context context) {
        super(context, "cigsSmoked.db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

}
