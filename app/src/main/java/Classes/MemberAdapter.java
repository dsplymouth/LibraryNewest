package Classes;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.R;
import com.example.libraryapp.EditMember;
import java.util.List;


public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

    private Context context;
    private List<MemberModel> memberList;

    public MemberAdapter(List<MemberModel> memberList, Context context) {
        this.memberList = memberList;
        this.context = context;
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_member, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        MemberModel member = memberList.get(position);

        holder.nameTextView.setText(member.getFullName());
        holder.usernameTextView.setText("Username: " + member.getUsername());
        holder.emailTextView.setText("Email: " + member.getEmail());
        holder.contactTextView.setText("Contact: " + member.getContact());
        holder.membershipTextView.setText("Membership: " + member.getMembershipEndDate());

        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditMember.class);
            intent.putExtra("mode", "edit");
            intent.putExtra("member_username", member.getUsername());
            intent.putExtra("member_firstname", member.getFirstname());
            intent.putExtra("member_lastname", member.getLastname());
            intent.putExtra("member_email", member.getEmail());
            intent.putExtra("member_contact", member.getContact());
            intent.putExtra("member_membership_end_date", member.getMembershipEndDate());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    class MemberViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView usernameTextView;
        TextView emailTextView;
        TextView contactTextView;
        TextView membershipTextView;
        Button editButton;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.item_member_name);
            usernameTextView = itemView.findViewById(R.id.item_member_username);
            emailTextView = itemView.findViewById(R.id.item_member_email);
            contactTextView = itemView.findViewById(R.id.item_member_contact);
            membershipTextView = itemView.findViewById(R.id.item_member_membership);
            editButton = itemView.findViewById(R.id.item_edit_button);
        }
    }
}