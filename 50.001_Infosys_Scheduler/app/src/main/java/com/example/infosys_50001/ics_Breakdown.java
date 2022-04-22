package com.example.infosys_50001;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    ArrayList<String> mContacts;
    ArrayList<String> mContacts1;
    ArrayList<String> mContacts2;

    public MainAdapter(ArrayList<String> contacts, ArrayList<String> contacts1, ArrayList<String> contacts2) {
        mContacts = contacts;
        mContacts1 = contacts1;
        mContacts2 = contacts2;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.free_slots, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        holder.mFullname.setText(mContacts.get(position));
        //holder.mFullname1.setText(mContacts1.get(position));
        holder.card.setCardBackgroundColor(Color.RED);
        if (mContacts2.get(position).contentEquals("1")){
            holder.card.setCardBackgroundColor(Color.GREEN);
        }

    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mFullname;
        public TextView mFullname1;
        public CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            mFullname = itemView.findViewById(R.id.From);
            mFullname1 = itemView.findViewById(R.id.To);
            card = itemView.findViewById(R.id.itemCard);
        }
    }
}
