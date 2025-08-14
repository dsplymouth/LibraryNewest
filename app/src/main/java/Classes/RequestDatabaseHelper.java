package Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RequestDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "requests.db";
    private static final int DATABASE_VERSION = 1;

    // table name
    private static final String TABLE_REQUESTS = "requests";

    // column names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_BOOK_ID = "book_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_REQUEST_DATE = "request_date";
    private static final String COLUMN_BOOK_TITLE = "book_title";
    private static final String COLUMN_MEMBER_NAME = "member_name";

    // create table
    private static final String CREATE_TABLE_REQUESTS =
            "CREATE TABLE " + TABLE_REQUESTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_BOOK_ID + " INTEGER, " +
                    COLUMN_USERNAME + " TEXT, " +
                    COLUMN_REQUEST_DATE + " TEXT, " +
                    COLUMN_BOOK_TITLE + " TEXT, " +
                    COLUMN_MEMBER_NAME + " TEXT)";

    public RequestDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_REQUESTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REQUESTS);
        onCreate(db);
    }

    // add new request
    public long addRequest(Request request) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_BOOK_ID, request.getBookId());
        values.put(COLUMN_USERNAME, request.getUsername());
        values.put(COLUMN_REQUEST_DATE, getCurrentDate());
        values.put(COLUMN_BOOK_TITLE, request.getBookTitle());
        values.put(COLUMN_MEMBER_NAME, request.getMemberName());

        long id = db.insert(TABLE_REQUESTS, null, values);
        db.close();
        return id;
    }

    // get all requests
    public List<Request> getAllRequests() {
        List<Request> requests = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_REQUESTS + " ORDER BY " + COLUMN_REQUEST_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Request request = new Request();

                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int bookIdIndex = cursor.getColumnIndex(COLUMN_BOOK_ID);
                int usernameIndex = cursor.getColumnIndex(COLUMN_USERNAME);
                int requestDateIndex = cursor.getColumnIndex(COLUMN_REQUEST_DATE);
                int bookTitleIndex = cursor.getColumnIndex(COLUMN_BOOK_TITLE);
                int memberNameIndex = cursor.getColumnIndex(COLUMN_MEMBER_NAME);

                if (idIndex >= 0) request.setId(cursor.getInt(idIndex));
                if (bookIdIndex >= 0) request.setBookId(cursor.getInt(bookIdIndex));
                if (usernameIndex >= 0) request.setUsername(cursor.getString(usernameIndex));
                if (requestDateIndex >= 0) request.setRequestDate(cursor.getString(requestDateIndex));
                if (bookTitleIndex >= 0) request.setBookTitle(cursor.getString(bookTitleIndex));
                if (memberNameIndex >= 0) request.setMemberName(cursor.getString(memberNameIndex));

                requests.add(request);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return requests;
    }

    // get requests by username
    public List<Request> getRequestsByUsername(String username) {
        List<Request> requests = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_REQUESTS +
                " WHERE " + COLUMN_USERNAME + " = ? ORDER BY " + COLUMN_REQUEST_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{username});

        if (cursor.moveToFirst()) {
            do {
                Request request = new Request();

                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int bookIdIndex = cursor.getColumnIndex(COLUMN_BOOK_ID);
                int usernameIndex = cursor.getColumnIndex(COLUMN_USERNAME);
                int requestDateIndex = cursor.getColumnIndex(COLUMN_REQUEST_DATE);
                int bookTitleIndex = cursor.getColumnIndex(COLUMN_BOOK_TITLE);
                int memberNameIndex = cursor.getColumnIndex(COLUMN_MEMBER_NAME);

                if (idIndex >= 0) request.setId(cursor.getInt(idIndex));
                if (bookIdIndex >= 0) request.setBookId(cursor.getInt(bookIdIndex));
                if (usernameIndex >= 0) request.setUsername(cursor.getString(usernameIndex));
                if (requestDateIndex >= 0) request.setRequestDate(cursor.getString(requestDateIndex));
                if (bookTitleIndex >= 0) request.setBookTitle(cursor.getString(bookTitleIndex));
                if (memberNameIndex >= 0) request.setMemberName(cursor.getString(memberNameIndex));

                requests.add(request);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return requests;
    }

    // delete request
    public int deleteRequest(int requestId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_REQUESTS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(requestId)});
        db.close();
        return result;
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}