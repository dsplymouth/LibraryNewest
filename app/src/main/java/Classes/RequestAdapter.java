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
    private OnRequestActionListener listener;
    private boolean isStaffMode;

    public interface OnRequestActionListener {
        void onApproveRequest(Request request);
        void onDenyRequest(Request request);
    }

    public RequestAdapter(Context context, List<Request> requestsList, OnRequestActionListener listener) {
        this.context = context;
        this.requestsList = requestsList;
        this.listener = listener;
        this.isStaffMode = (listener != null); // staff mode if listener is provided
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

        holder.textViewBookTitle.setText("Book: " + request.getBookTitle());
        holder.textViewMemberName.setText("Member: " + request.getMemberName());
        holder.textViewRequestDate.setText("Date: " + request.getRequestDate());

        if (isStaffMode) {
            // staff mode - show approve/deny buttons
            holder.buttonApprove.setVisibility(View.VISIBLE);
            holder.buttonDeny.setVisibility(View.VISIBLE);
        } else {
            // member mode - hide buttons
            holder.buttonApprove.setVisibility(View.GONE);
            holder.buttonDeny.setVisibility(View.GONE);
        }

        holder.buttonApprove.setOnClickListener(v -> {
            if (listener != null) {
                listener.onApproveRequest(request);
            }
        });

        holder.buttonDeny.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDenyRequest(request);
            }
        });
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
        TextView textViewBookTitle, textViewMemberName, textViewRequestDate;
        Button buttonApprove, buttonDeny;

        RequestViewHolder(View itemView) {
            super(itemView);
            textViewBookTitle = itemView.findViewById(R.id.textViewBookTitle);
            textViewMemberName = itemView.findViewById(R.id.textViewMemberName);
            textViewRequestDate = itemView.findViewById(R.id.textViewRequestDate);
            buttonApprove = itemView.findViewById(R.id.buttonApprove);
            buttonDeny = itemView.findViewById(R.id.buttonDeny);
        }
    }
}