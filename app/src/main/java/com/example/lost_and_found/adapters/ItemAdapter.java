package com.example.lost_and_found.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lost_and_found.R;
import com.example.lost_and_found.models.Item;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private final List<Item> itemList;
    private final Context context;
    private final OnItemClickListener listener;

    // Define an interface for click handling
    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public ItemAdapter(List<Item> itemList, Context context, OnItemClickListener listener) {
        this.itemList = itemList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.itemTitle.setText(item.getTitle());
        holder.itemDescription.setText(item.getDescription());
        holder.bind(item, listener);
        if(item.isLost()){

            holder.itemstatus.setText("Status: Lost");

        }else {
            holder.itemstatus.setText("Status: Found");

        }

        // Decode the Base64 string into a Bitmap
        String base64String = item.getImageBase64();
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        InputStream inputStream = new ByteArrayInputStream(decodedString);
        Bitmap decodedBitmap = BitmapFactory.decodeStream(inputStream);

        // Load the image into the ImageView using Glide
        Glide.with(context)
                .load(decodedBitmap)  // Pass Bitmap directly
                .placeholder(R.drawable.ic_launcher_background) // Show a placeholder while loading
                .into(holder.itemImage);
    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle, itemDescription,itemstatus;
        ImageView itemImage;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemstatus = itemView.findViewById(R.id.itemstatus);

            itemDescription = itemView.findViewById(R.id.itemDescription);
            itemImage = itemView.findViewById(R.id.itemImage);
        }

        public void bind(Item item, OnItemClickListener listener) {
            itemTitle.setText(item.getTitle());
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }
}
