package com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Friends;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.david_maman_c.mi_chat_real_time.R;
import com.example.david_maman_c.mi_chat_real_time.Sms.Messages;

import java.util.List;

/**
 * Created by davidmaman on 08/08/2017.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.HolderFriends> {

    private List<ConstractorFriendsList> friendsList;
    private Context context;
    private Fragment_FriendsList f;

    public FriendsAdapter(List<ConstractorFriendsList> friendsList, Context context, Fragment_FriendsList f){
        this.friendsList = friendsList;
        this.context = context;
        this.f = f;
    }


    @Override
    public HolderFriends onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_friends,parent,false);
        return new FriendsAdapter.HolderFriends(v);
    }

    @Override
    public void onBindViewHolder(HolderFriends holder, final int position) {
        holder.imageView.setImageResource(friendsList.get(position).getPhoto());
        holder.name.setText(friendsList.get(position).getFullName());
        holder.message.setText(friendsList.get(position).getMessage());
        holder.hour.setText(friendsList.get(position).getHour());



        if(friendsList.get(position).getMessage().equals("null")){
            holder.hour.setVisibility(View.GONE);
            holder.message.setText("Send message to start chat");
        }
        else{
            holder.hour.setVisibility(View.VISIBLE);
            if(friendsList.get(position).getKind_message().equals("1")){
                holder.message.setTextColor(ContextCompat.getColor(context, R.color.red_400));
            }
            else{
                holder.message.setTextColor(ContextCompat.getColor(context, R.color.blue_400));
            }
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Messages.class);
                i.putExtra("key_receptor", friendsList.get(position).getId());
                context.startActivity(i);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context).
                        setMessage("Are you sure to delete this user?").
                        setPositiveButton("acept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                f.deleteFriends(friendsList.get(position).getId());
                                Toast.makeText(context, "this friend is delete", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show();
                    }
                }).show();

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    static class HolderFriends extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView imageView;
        TextView name;
        TextView message;
        TextView hour;

        public HolderFriends(View itemView) {
            super(itemView);

            cardView = (CardView)itemView.findViewById(R.id.cardView_friends);
            imageView = (ImageView)itemView.findViewById(R.id.photo_user);
            name = (TextView)itemView.findViewById(R.id.name_friend);
            message = (TextView)itemView.findViewById(R.id.message_friend);
            hour = (TextView)itemView.findViewById(R.id.hour_friend);

        }
    }


}
