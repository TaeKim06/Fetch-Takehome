package com.example.fetchtakehome;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<Item> groupedItems;
    Context context;

    public RecyclerAdapter(Context context, ArrayList<Item> groupedItems) {
        this.context = context;
        this.groupedItems = groupedItems;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // creates a new view
        LayoutInflater inflater = LayoutInflater.from(context);
        View view =  inflater.inflate(R.layout.listlayout, parent, false);

        return new RecyclerAdapter.ViewHolder(view);
    }

    // Assigns values to the views created in layout file
    // Based on the position of the recycler view
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = this.groupedItems.get(position);
        String formatting = item.getListId() + ": " + item.getName();
        holder.itemTextView.setText(formatting);

    }

    @Override
    public int getItemCount() {
        return this.groupedItems.size();
    }

    public void setGroupedItems(ArrayList<Item> groupedItems) {
        this.groupedItems = groupedItems;
    }


    // Grabs the view from the listlayout.xml layout file
    // Grossly oversimplification is that it acts like the OnCreate method
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView itemTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTextView = itemView.findViewById(R.id.itemTextView);
        }
    }
}