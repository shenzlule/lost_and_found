package com.example.lost_and_found.adapters;

import android.app.AlertDialog;
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
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.lost_and_found.DataBase.AppDatabase;
import com.example.lost_and_found.MainTabs;
import com.example.lost_and_found.R;
import com.example.lost_and_found.models.Item;
import com.example.lost_and_found.models.ItemDao;
import com.example.lost_and_found.ui.frags.MyFragment;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class ItemMyAdapter extends RecyclerView.Adapter<ItemMyAdapter.ItemViewHolder> {
    private final List<Item> itemList;
    public  Context context;


    private final OnItemClickListener listener;


    // Define an interface for click handling
    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public ItemMyAdapter(List<Item> itemList, Context context, OnItemClickListener listener) {
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

            holder.itemstatus_.setText("Status: Lost");

        }else {
            holder.itemstatus_.setText("Status: Found");

        }

//        // Long press listener to delete the item
//        holder.itemView.setOnLongClickListener(v -> {
//            showDeleteDialog(item);
//            return true; // Indicate the event has been handled
//        });
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

        // Long press listener to delete the item
        holder.itemView.setOnLongClickListener(v -> {
            showDeleteDialog(item, position);

            return true;
        });


    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }



    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle, itemDescription,itemstatus_;
        ImageView itemImage;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemDescription = itemView.findViewById(R.id.itemDescription);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemstatus_ = itemView.findViewById(R.id.itemstatus);

        }

        public void bind(Item item, OnItemClickListener listener) {
            itemTitle.setText(item.getTitle());
            itemView.setOnClickListener(v -> listener.onItemClick(item));


        }



    }



    private void showDeleteDialog(Item item, int position) {
        try {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Item")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        deleteItem(item,position);  // Delete item from the database
                        itemList.remove(position);
                        notifyItemRemoved(position);  // Notify adapter
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        } catch (Exception e) {
            notifyItemRemoved(position);  // Notify adapter

        }

    }

    private void deleteItem(Item item, int position) {
        AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, "app_database").build();
        ItemDao itemDao = db.itemDao();

        new Thread(() -> {
            itemDao.delete(item);



        }).start();
    }


}
