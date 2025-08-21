package Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BookDatabaseHelper extends SQLiteOpenHelper {
    public static final String BOOKS = "books";
    public static final String TITLE = "title";
    public static final String QUANTITY = "quantity";
    public static final String ID = "id";


   public BookDatabaseHelper(@Nullable Context context) {
        super(context, "librarybooks.db", null, 2); // change this to version 2
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + BOOKS + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE + " TEXT UNIQUE, " +
                QUANTITY + " INTEGER DEFAULT 1)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("DROP TABLE IF EXISTS " + BOOKS);
            onCreate(db);
        }
    }    // add a new book to the library
    public boolean addBook(String title, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TITLE, title);
        cv.put(QUANTITY, quantity);

        long result = db.insert(BOOKS, null, cv);
        return result != -1;// return true if successful
    }


    // get all books from library
    // simple query to get all books used comp2000 as a reference for the API and sqllite
    public List<BookModel> getAllBooks() {
        List<BookModel> outputList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + BOOKS;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                int quantity = cursor.getInt(2);

                BookModel bookModel = new BookModel(id, title, quantity);
                outputList.add(bookModel);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return outputList;
    }
    // update book details
    public boolean updateBook(String oldTitle, String newTitle, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE, newTitle);
        values.put(QUANTITY, newQuantity);

        int result = db.update(BOOKS, values, TITLE + "=?", new String[]{oldTitle});
        return result > 0;
    }


    // delete book from library
    public boolean deleteBook(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(BOOKS, TITLE + "=?", new String[]{title});
        return result > 0;
    }
    // check if book exists in library


    public boolean bookExists(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + BOOKS + " WHERE " + TITLE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{title});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }



}