package com.example.lost_and_found.ui.pages;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.lost_and_found.DataBase.AppDatabase;
import com.example.lost_and_found.GoogleSignInActivity;
import com.example.lost_and_found.MainTabs;
import com.example.lost_and_found.R;
import com.example.lost_and_found.models.Item;
import com.example.lost_and_found.models.ItemDao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ItemDetailActivityMy extends AppCompatActivity {



    private ImageView profileImageView;

    private TextView hi_txt;




    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail_my);

        TextView titleText = findViewById(R.id.titleText1);
        TextView descriptionText = findViewById(R.id.descriptionText1);
        TextView userIdText = findViewById(R.id.userIdText1);
        TextView itemIdText = findViewById(R.id.itemIdText1);
        ImageView itemImage = findViewById(R.id.itemImage1);

        TextView StatusText = findViewById(R.id.status);
        TextView  ConstactText = findViewById(R.id.contact);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(this, GoogleSignInActivity.class));
            finish();
        }


        profileImageView = findViewById(R.id.profileImage2);

        hi_txt = findViewById(R.id.hi2);

        // Load profile picture using Glide (make sure to include Glide in your dependencies)
        if (currentUser.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(currentUser.getPhotoUrl()) // Use the photo URL from FirebaseAuth
                    .circleCrop() // To make it round
                    .into(profileImageView);
        }



        // Retrieve passed item
        Item item = (Item) getIntent().getSerializableExtra("item");

        if (item != null) {
            titleText.setText(item.getTitle());
            descriptionText.setText("Description: " + item.getDescription());
            userIdText.setText("User ID: " + item.getUserId());
            itemIdText.setText("Item ID: " + item.getId());

            if(item.isLost()){
                StatusText.setText("Status: " + "Lost");
            }else{
                StatusText.setText("Status: " + "Found");

            }
            ConstactText.setText("Contact: " + item.getContact());



            // Decode the Base64 string into a Bitmap
            String base64String = item.getImageBase64();
            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
            InputStream inputStream = new ByteArrayInputStream(decodedString);
            Bitmap decodedBitmap = BitmapFactory.decodeStream(inputStream);


            // Load image if available
            Glide.with(this)
                    .load(decodedBitmap)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(itemImage);
        }

        loadUserProfile();




    }




    private void loadUserProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {


            // Get full display name
            String fullName = user.getDisplayName();
            String firstName = "Hi";

            if (fullName != null && !fullName.isEmpty()) {
                // Extract the first word (assumed to be first name)
                String[] nameParts = fullName.split(" ");
                firstName += " " + nameParts[0]; // Get only first name

                // If the name is too long, truncate with "..."
                if (firstName.length() > 12) {
                    firstName = firstName.substring(0, 9) + "...";
                }
            }

            firstName += "!";
            hi_txt.setText(firstName); // Set formatted name


        }






    }


    private void showDeleteDialog(Item item) {
        new AlertDialog.Builder(getApplicationContext())
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deleteItem(item);  // Delete item from the database
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteItem(Item item) {
        // Access the database and delete the item
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        ItemDao itemDao = db.itemDao();

        // Delete the item in the background thread
        new Thread(() -> {
            itemDao.delete(item);  // Delete the item

//            Intent intent = new Intent(this, MainTabs.class);
//            intent.putExtra("goToUploads", 1);
//            startActivity(intent);
        }).start();
    }


}
