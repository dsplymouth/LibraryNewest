package Classes;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.libraryapp.R;
import java.util.List;
import Classes.NotificationDatabaseHelper;

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.NotifViewHolder> {
    private List<NotifItem> notificationsList;
    private Context context;

    public NotifAdapter(Context context, List<NotifItem> notificationsList) {
        this.context = context;
        this.notificationsList = notificationsList;
    }

    @NonNull
    @Override
    public NotifViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notif_item, parent, false);
        return new NotifViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifViewHolder holder, int position) {
        NotifItem notification = notificationsList.get(position);

        holder.titleTextView.setText(notification.getTitle());
        holder.messageTextView.setText(notification.getMessage());
        holder.timeTextView.setText(notification.getTimestamp());
        // change background color based on read status
        // tried different colors, blue works best for unread
        if (notification.isRead()) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_blue_light));
        }

        holder.itemView.setOnClickListener(v -> {
            if (!notification.isRead()) {
                notification.setRead(true);
                NotificationDatabaseHelper.markAsRead(context, notification.getId());
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    static class NotifViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView messageTextView;
        TextView timeTextView;

        NotifViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textViewNotificationTitle);
            messageTextView = itemView.findViewById(R.id.textViewNotificationMessage);
            timeTextView = itemView.findViewById(R.id.textViewNotificationTime);
        }
    }
}