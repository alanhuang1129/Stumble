package com.example.stumble;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
//    String listings[], descriptions[];
    private List<MyDatabase.Listing> listings;
    private Context context;

    public MyAdapter(Context c, List<MyDatabase.Listing>listings) {
        this.listings = listings;
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
        String name = listings.get(position).lName;
        String type = listings.get(position).lType;
        String imageURL = listings.get(position).lImageUrl;
        boolean isClosed = listings.get(position).lIsClosed;
        double rating = listings.get(position).lRating;
        String price = listings.get(position).lPrice;
        String location = listings.get(position).lLocation;
        double latitude = listings.get(position).lLatitude;
        double longitude = listings.get(position).lLongitude;
        double distance = listings.get(position).lDistance;

        new DownloadImageTask(holder.listingImage)
                .execute(imageURL);
        holder.listingTitle.setText(name);
        holder.listingDescription.setText(rating + " stars");
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, SelectedActivity.class);
                i.putExtra("Name", name);
                i.putExtra("Type", type);
                i.putExtra("Image", imageURL);
                i.putExtra("IsClosed", isClosed);
                i.putExtra("Rating", rating);
                i.putExtra("Price", price);
                i.putExtra("Location", location);
                i.putExtra("Latitude", latitude);
                i.putExtra("Longitude", longitude);
                i.putExtra("Distance", distance);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView listingTitle, listingDescription;
        ImageView listingImage;
        ConstraintLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            listingImage = itemView.findViewById(R.id.listingImage);
            listingTitle = itemView.findViewById(R.id.titleTextView);
            listingDescription = itemView.findViewById(R.id.descriptionTextView);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }

    /*** Implemented from
     * https://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
     * to load image urls into image view
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
