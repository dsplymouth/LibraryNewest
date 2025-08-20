package Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.libraryapp.R;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {
    private List<Request> requestsList;
    private Context context;
    private boolean isStaffMode;

    public RequestAdapter(Context context, List<Request> requestsList, boolean isStaffMode) {
        this.context = context;
        this.requestsList = requestsList;
        this.isStaffMode = isStaffMode;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Request request = requestsList.get(position);

        if (isStaffMode) {
            holder.bookTitleTextView.setText("Book: " + request.getBookTitle());
            holder.memberTextView.setText("Member: " + request.getMemberName());
            holder.requestDateTextView.setText("Date: " + request.getRequestDate());
            holder.memberTextView.setVisibility(View.VISIBLE);

            holder.buttonApprove.setVisibility(View.VISIBLE);
            holder.buttonDeny.setVisibility(View.VISIBLE);

            holder.buttonApprove.setOnClickListener(v -> {
                Request currentRequest = requestsList.get(position);
                String memberUsername = currentRequest.getUsername();
                String bookTitle = currentRequest.getBookTitle();

                NotificationDatabaseHelper.createApprovalNotification(context, memberUsername, bookTitle, true);
                NotificationDatabaseHelper.removeRequestNotification(context, memberUsername, bookTitle);

                RequestDatabaseHelper requestDbHelper = new RequestDatabaseHelper(context);
                requestDbHelper.deleteRequest(currentRequest.getId());

                requestsList.remove(position);
                notifyItemRemoved(position);
            });

            holder.buttonDeny.setOnClickListener(v -> {
                Request currentRequest = requestsList.get(position);
                String memberUsername = currentRequest.getUsername();
                String bookTitle = currentRequest.getBookTitle();
                // create approval notification first, then remove request notification
                // had issues notifications were getting lost and not displaying
                NotificationDatabaseHelper.createApprovalNotification(context, memberUsername, bookTitle, false);
                NotificationDatabaseHelper.removeRequestNotification(context, memberUsername, bookTitle);

                RequestDatabaseHelper requestDbHelper = new RequestDatabaseHelper(context);
                requestDbHelper.deleteRequest(currentRequest.getId());

                requestsList.remove(position);
                notifyItemRemoved(position);
            });
        } else {
            holder.bookTitleTextView.setText("Book: " + request.getBookTitle());
            holder.requestDateTextView.setText("Date: " + request.getRequestDate());
            holder.memberTextView.setVisibility(View.GONE);
            holder.buttonApprove.setVisibility(View.GONE);
            holder.buttonDeny.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return requestsList.size();
    }

    public void updateRequests(List<Request> newRequests) {
        this.requestsList = newRequests;
        notifyDataSetChanged();
    }

    static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView bookTitleTextView;
        TextView memberTextView;
        TextView requestDateTextView;
        Button buttonApprove;
        Button buttonDeny;

        RequestViewHolder(View itemView) {
            super(itemView);
            bookTitleTextView = itemView.findViewById(R.id.textViewBookTitle);
            memberTextView = itemView.findViewById(R.id.textViewMemberName);
            requestDateTextView = itemView.findViewById(R.id.textViewRequestDate);
            buttonApprove = itemView.findViewById(R.id.buttonApprove);
            buttonDeny = itemView.findViewById(R.id.buttonDeny);
        }
    }
}