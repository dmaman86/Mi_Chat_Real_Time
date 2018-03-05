package com.example.david_maman_c.mi_chat_real_time.ActivityOfUsers.Users;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.david_maman_c.mi_chat_real_time.R;

/**
 * Created by davidmaman on 10/08/2017.
 */

public class HolderSearchUsers extends RecyclerView.ViewHolder {

    private CardView cardViewSearch;
    private ImageView photo;
    private TextView nameUser;
    private TextView statusUser;
    private Button btnRigth;
    private Button btnLeft;


    public HolderSearchUsers(View itemView) {
        super(itemView);

        photo = (ImageView) itemView.findViewById(R.id.photo_rf);
        nameUser = (TextView)itemView.findViewById(R.id.name_rf);
        statusUser = (TextView)itemView.findViewById(R.id.status_user);
        btnRigth = (Button)itemView.findViewById(R.id.btnRigth);
        btnLeft = (Button)itemView.findViewById(R.id.btnLeft);
        cardViewSearch = (CardView)itemView.findViewById(R.id.cardViewSearch);

    }

    public ImageView getPhoto() {
        return photo;
    }

    public void setPhoto(ImageView photo) {
        this.photo = photo;
    }

    public TextView getNameUser() {
        return nameUser;
    }

    public void setNameUser(TextView nameUser) {
        this.nameUser = nameUser;
    }

    public TextView getStatusUser() {
        return statusUser;
    }

    public void setStatusUser(TextView statusUser) {
        this.statusUser = statusUser;
    }

    public Button getBtnRigth() {
        return btnRigth;
    }

    public void setBtnRigth(Button btnRigth) {
        this.btnRigth = btnRigth;
    }

    public Button getBtnLeft() {
        return btnLeft;
    }

    public void setBtnLeft(Button btnLeft) {
        this.btnLeft = btnLeft;
    }

    public CardView getCardViewSearch() {
        return cardViewSearch;
    }

    public void setCardViewSearch(CardView cardViewSearch) {
        this.cardViewSearch = cardViewSearch;
    }
}
