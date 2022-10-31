package com.example.stumble;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    String listings[], descriptions[];
    private Context context;

    public MyAdapter(Context c, String listings[], String descriptions[]) {
        this.listings = listings;
        this.descriptions = descriptions;
        context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_listing, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.listingTitle.setText(listings[position]);
        holder.listingDescription.setText(descriptions[position]);
    }

    @Override
    public int getItemCount() {
        return listings.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView listingTitle, listingDescription;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            listingTitle = itemView.findViewById(R.id.titleTextView);
            listingDescription = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}
