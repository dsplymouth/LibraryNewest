package Classes;
import android.content.Context;
import android.widget.Button;
import android.view.LayoutInflater;
import android.widget.Toast;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.R;
import java.util.List;
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<BookModel> bookList;
    private Context context;
    private BookDatabaseHelper dbHelper;

    public BookAdapter(List<BookModel> bookList, Context context) {
        this.bookList = bookList;
        this.context = context;
        this.dbHelper = new BookDatabaseHelper(context);
    }


}
