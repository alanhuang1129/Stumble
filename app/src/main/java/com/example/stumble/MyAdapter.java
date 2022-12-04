package com.example.stumble;

import android.content.Context;
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
        new DownloadImageTask(holder.listingImage)
                .execute(listings.get(position).lImageUrl);
        holder.listingTitle.setText(listings.get(position).lName);
        holder.listingDescription.setText(listings.get(position).lType);
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView listingTitle, listingDescription;
        ImageView listingImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            listingImage = itemView.findViewById(R.id.listingImage);
            listingTitle = itemView.findViewById(R.id.titleTextView);
            listingDescription = itemView.findViewById(R.id.descriptionTextView);
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
