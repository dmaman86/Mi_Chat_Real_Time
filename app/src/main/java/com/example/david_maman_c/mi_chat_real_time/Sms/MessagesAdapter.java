package com.example.david_maman_c.mi_chat_real_time.Sms;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.david_maman_c.mi_chat_real_time.R;

import java.util.List;

/**
 * Created by david-maman-c on 2.8.2017.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder>{

    private List<MessagesTxt> messagestxt;//messagestxt is vector of messages
    private Context context;

    public MessagesAdapter(List<MessagesTxt> messagestxt,Context context) {
        this.messagestxt = messagestxt;
        this.context = context;
    }

    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_message, parent, false);
        return new MessagesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessagesViewHolder holder, int position) {
        RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) holder.cardView.getLayoutParams();
        FrameLayout.LayoutParams fl = (FrameLayout.LayoutParams)holder.messageBG.getLayoutParams();

        LinearLayout.LayoutParams ll_Message = (LinearLayout.LayoutParams)holder.TvMessages.getLayoutParams();
        LinearLayout.LayoutParams ll_Hr = (LinearLayout.LayoutParams)holder.TvHr.getLayoutParams();

        if(messagestxt.get(position).getKindMessage() == 1){//this is for sender
            holder.messageBG.setBackgroundResource(R.drawable.in_message_bg);
            rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT,0);
            rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            ll_Message.gravity = Gravity.RIGHT;
            ll_Hr.gravity = Gravity.RIGHT;
            fl.gravity = Gravity.RIGHT;
            holder.TvMessages.setGravity(Gravity.RIGHT);
        }
        else if(messagestxt.get(position).getKindMessage() == 2){//this if for receptor
            holder.messageBG.setBackgroundResource(R.drawable.out_message_bg);
            rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,0);
            rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            ll_Message.gravity = Gravity.LEFT;
            ll_Hr.gravity = Gravity.LEFT;
            fl.gravity = Gravity.LEFT;
            holder.TvMessages.setGravity(Gravity.LEFT);
        }
        holder.cardView.setLayoutParams(rl);
        holder.messageBG.setLayoutParams(fl);
        holder.TvMessages.setLayoutParams(ll_Message);
        holder.TvHr.setLayoutParams(ll_Hr);

        holder.TvMessages.setText(messagestxt.get(position).getMessage());
        holder.TvHr.setText(messagestxt.get(position).getHrMessage());

        holder.cardView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent));
    }

    @Override
    public int getItemCount() {
        return messagestxt.size();//getItemCount return vector size of messages
    }

    static class MessagesViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        LinearLayout messageBG;
        TextView TvMessages;
        TextView TvHr;

        MessagesViewHolder(View itemView){
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.cv_message);
            messageBG = (LinearLayout)itemView.findViewById(R.id.messageBG);
            TvMessages = (TextView)itemView.findViewById(R.id.message_txt);
            TvHr = (TextView)itemView.findViewById(R.id.message_hr);
        }
    }


}
