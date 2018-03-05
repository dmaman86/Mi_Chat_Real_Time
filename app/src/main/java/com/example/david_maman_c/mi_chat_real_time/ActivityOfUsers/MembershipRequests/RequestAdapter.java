package com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.MembershipRequests;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david_maman_c.mi_chat_real_time.R;

import java.util.List;

/**
 * Created by davidmaman on 09/08/2017.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.requestHolder> {

    private List<Request> requestList;
    private Context context;
    private Fragment_Request fr;

    public RequestAdapter(List<Request> requestList, Context context, Fragment_Request fr){
        this.requestList = requestList;
        this.context = context;
        this.fr = fr;
    }

    @Override
    public requestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_requests,parent,false);
        return new RequestAdapter.requestHolder(v);
    }

    @Override
    public void onBindViewHolder(requestHolder holder, final int position) {
        holder.photo_request.setImageResource(requestList.get(position).getPhoto());
        holder.name.setText(requestList.get(position).getFullName());
        holder.hour.setText(requestList.get(position).getHour());

        switch (requestList.get(position).getStatus()){
            case 2:
                holder.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fr.cancelRequest(requestList.get(position).getId());
                    }
                });
                holder.accept.setVisibility(View.GONE);
                break;
            case 3:
                holder.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fr.cancelRequest(requestList.get(position).getId());
                    }
                });
                holder.accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fr.aceptRequest(requestList.get(position).getId());
                    }
                });
                holder.accept.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    static class requestHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView photo_request;
        TextView name;
        TextView hour;
        Button cancel;
        Button accept;

        public requestHolder(View itemView) {
            super(itemView);

            photo_request = (ImageView)itemView.findViewById(R.id.photo_userRequest);
            name = (TextView)itemView.findViewById(R.id.name_request);
            hour = (TextView)itemView.findViewById(R.id.hour_request);
            cancel = (Button)itemView.findViewById(R.id.cancelRequest);
            accept = (Button)itemView.findViewById(R.id.acceptRequest);

        }
    }


}
