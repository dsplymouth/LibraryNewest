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
        super(context, "librarybooks.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable =
                "CREATE TABLE " + BOOKS + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TITLE + " TEXT, " +
                QUANTITY + " INTEGER)";

        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTable = "DROP TABLE IF EXISTS " + BOOKS;
        db.execSQL(dropTable);
        onCreate(db);
    }

    public boolean addBook(BookModel bookModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TITLE, bookModel.getTitle());
        cv.put(QUANTITY, bookModel.getQuantity());

        long result = db.insert(BOOKS, null, cv);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

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

                BookModel bookModel = new BookModel(title, quantity);
                bookModel.setId(id);
                outputList.add(bookModel);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return outputList;
    }

    public boolean deleteBook(BookModel bookModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + BOOKS + " WHERE " + TITLE + " = '" + bookModel.getTitle() + "'";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean updateBook(BookModel bookModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TITLE, bookModel.getTitle());
        cv.put(QUANTITY, bookModel.getQuantity());

        long result = db.update(BOOKS, cv, ID + " = ?", new String[]{String.valueOf(bookModel.getId())});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // check if book exists
    public boolean bookExists(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + BOOKS + " WHERE " + TITLE + " = '" + title + "'";
        Cursor cursor = db.rawQuery(query, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // get book by title
    public BookModel getBookByTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + BOOKS + " WHERE " + TITLE + " = '" + title + "'";
        Cursor cursor = db.rawQuery(query, null);

        BookModel book = null;
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String bookTitle = cursor.getString(1);
            int quantity = cursor.getInt(2);
            book = new BookModel(id, bookTitle, quantity);
        }

        cursor.close();
        return book;
    }
}