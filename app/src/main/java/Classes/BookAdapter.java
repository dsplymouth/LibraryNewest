package Classes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.R;
import com.example.libraryapp.EditBook;
import com.example.libraryapp.ViewBook;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private Context context;
    private List<?> bookList;
    private boolean isStaffMode;
    private String currentUsername;

    public BookAdapter(Context context, List<?> bookList, boolean isStaffMode, String username) {
        this.context = context;
        this.bookList = bookList;
        this.isStaffMode = isStaffMode;
        this.currentUsername = username;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Object bookObj = bookList.get(position);
        String title;
        int quantity;

        if (bookObj instanceof BookModel) {
            BookModel bookModel = (BookModel) bookObj;
            title = bookModel.getTitle();
            quantity = bookModel.getQuantity();
        } else if (bookObj instanceof Book) {
            Book book = (Book) bookObj;
            title = book.getTitle();
            quantity = book.getQuantity();
        } else {
            title = "Unknown Book";
            quantity = 0;
        }

        // log
        Log.d("BookAdapter", "Binding book: " + title + " with quantity: " + quantity + " (Staff mode: " + isStaffMode + ")");

        holder.titleTextView.setText(title);

        // check if quantityTextView is null
        if (holder.quantityTextView != null) {
            holder.quantityTextView.setText("Quantity: " + quantity);
            Log.d("BookAdapter", "Set quantity text: " + quantity);
        } else {
            Log.e("BookAdapter", "quantityTextView is null!");
        }

        // set status
        String status = quantity > 0 ? "Available" : "Not Available";
        holder.statusTextView.setText("Status: " + status);

        if (quantity > 0) {
            holder.statusTextView.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark));
        } else {
            holder.statusTextView.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
        }

        // set buttons for actions
        if (isStaffMode) {
            holder.actionButton.setText("Edit");
            holder.actionButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, EditBook.class);
                if (bookObj instanceof BookModel) {
                    BookModel bookModel = (BookModel) bookObj;
                    intent.putExtra("mode", "edit");
                    intent.putExtra("book_id", bookModel.getId());
                    intent.putExtra("book_title", bookModel.getTitle());
                    intent.putExtra("book_quantity", bookModel.getQuantity());
                } else if (bookObj instanceof Book) {
                    Book book = (Book) bookObj;
                    intent.putExtra("mode", "edit");
                    intent.putExtra("book_id", book.getId());
                    intent.putExtra("book_title", book.getTitle());
                    intent.putExtra("book_quantity", book.getQuantity());
                }
                context.startActivity(intent);
            });
        } else {
            holder.actionButton.setText("View");
            holder.actionButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, ViewBook.class);
                if (bookObj instanceof BookModel) {
                    BookModel bookModel = (BookModel) bookObj;
                    intent.putExtra("book_id", bookModel.getId());
                } else if (bookObj instanceof Book) {
                    Book book = (Book) bookObj;
                    intent.putExtra("book_id", book.getId());
                }
                intent.putExtra("username", currentUsername);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void updateBooks(List<?> newBooks) {
        this.bookList = newBooks;
        notifyDataSetChanged();
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView quantityTextView;
        TextView statusTextView;
        Button actionButton;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.item_title);
            quantityTextView = itemView.findViewById(R.id.item_quantity);
            statusTextView = itemView.findViewById(R.id.item_status);
            actionButton = itemView.findViewById(R.id.item_edit_button);


            Log.d("BookAdapter", "ViewHolder created - quantityTextView: " + (quantityTextView != null ? "found" : "null"));
        }
    }
}