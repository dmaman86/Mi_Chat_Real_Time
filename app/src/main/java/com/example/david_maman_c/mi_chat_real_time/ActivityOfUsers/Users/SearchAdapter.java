package com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Users;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.david_maman_c.mi_chat_real_time.R;

import java.util.List;

/**
 * Created by davidmaman on 10/08/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<HolderSearchUsers> {

    private List<UsersSearch_Constractor> list_search;
    private Context context;
    private Fragment_Users f;

    public SearchAdapter(List<UsersSearch_Constractor> list_search, Context context, Fragment_Users f) {
        this.list_search = list_search;
        this.context = context;
        this.f = f;
    }

    @Override
    public HolderSearchUsers onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_searchusers, parent, false);
        return new HolderSearchUsers(v);
    }

    @Override
    public void onBindViewHolder(HolderSearchUsers holder, final int position) {
        holder.getPhoto().setImageResource(list_search.get(position).getPhoto());
        holder.getNameUser().setText(list_search.get(position).getFullName());
        switch (list_search.get(position).getStatus()){
            case 1:
                holder.getBtnRigth().setHint("Request Send");
                holder.getBtnLeft().setVisibility(View.GONE);

                holder.getBtnRigth().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //event was we send request friendly
                        f.sendRequest(list_search.get(position).getId());
                        Toast.makeText(context, "Send request...", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.getBtnRigth().setHintTextColor(ContextCompat.getColor(context, R.color.green_A200));
                break;//no friends and no requesr friends
            case 2:
                holder.getBtnLeft().setVisibility(View.GONE);
                holder.getBtnRigth().setHint("Cancel");
                holder.getBtnRigth().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        f.cancelRequest(list_search.get(position).getId());
                        Toast.makeText(context, "Cancel request...", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.getBtnRigth().setHintTextColor(ContextCompat.getColor(context, R.color.red_700));
                break;//pendding to acept requesr friends in receptor
            case 3:
                holder.getBtnLeft().setVisibility(View.VISIBLE);
                holder.getBtnRigth().setHint("Cancel");
                holder.getBtnLeft().setHint("Acept");
                holder.getBtnLeft().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        f.aceptRequest(list_search.get(position).getId());
                        Toast.makeText(context, "acepted request friendly", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.getBtnRigth().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        f.cancelRequest(list_search.get(position).getId());
                        Toast.makeText(context, "canceled request friendly", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.getBtnRigth().setHintTextColor(ContextCompat.getColor(context, R.color.red_700));
                break;//request friends pendding to accept
            case 4:
                holder.getBtnLeft().setVisibility(View.GONE);
                holder.getBtnRigth().setHint("Go to Profile");

                holder.getBtnRigth().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "See Profile", Toast.LENGTH_SHORT).show();
                    }
                });
                holder.getCardViewSearch().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        new AlertDialog.Builder(context).
                                setMessage("Are you sure to delete this user?").
                                setPositiveButton("acept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                f.deleteUser(list_search.get(position).getId());
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
                holder.getBtnRigth().setHintTextColor(ContextCompat.getColor(context, R.color.light_blue_300));
                break;//friends!
        }
        holder.getStatusUser().setText(""+list_search.get(position).getStatus());



    }

    @Override
    public int getItemCount() {
        return list_search.size();
    }
}
