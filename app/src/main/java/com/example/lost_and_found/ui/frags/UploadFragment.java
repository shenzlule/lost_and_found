package com.example.lost_and_found.ui.frags;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lost_and_found.DataBase.AppDatabase;
import com.example.lost_and_found.MainTabs;
import com.example.lost_and_found.R;
import com.example.lost_and_found.models.Item;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class UploadFragment extends Fragment {

    private EditText titleInput, descriptionInput,contactInput;
    private ImageView itemImage;
    private Button uploadButton, selectImageButton;

    private CheckBox isLostCheckBox;


    private Uri imageUri;
    private ProgressDialog progressDialog;
    private AppDatabase db;

    public UploadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        titleInput = view.findViewById(R.id.titleInput);
        descriptionInput = view.findViewById(R.id.descriptionInput);
        itemImage = view.findViewById(R.id.itemImage);
        uploadButton = view.findViewById(R.id.uploadButton);
        selectImageButton = view.findViewById(R.id.selectImageButton);


        contactInput = view.findViewById(R.id.contactInput);

        isLostCheckBox = view.findViewById(R.id.isLostCheckBox);



        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading...");

        db = AppDatabase.getDatabase(getContext()); // Initialize Room database

        selectImageButton.setOnClickListener(v -> pickImage());
        uploadButton.setOnClickListener(v -> {
            if (validateInputs()) {
                saveItemToDatabase();  // Save the item to local database
            }
        });

        return view;
    }

    // Select image from gallery
    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    public void refresh() {


        if(titleInput != null && descriptionInput != null && itemImage!=null&& isLostCheckBox!=null & contactInput!=null) {
            // Clear the EditTexts and reset the ImageView
            titleInput.setText("");
            descriptionInput.setText("");
            itemImage.setImageURI(null); // Clear the selected image
            isLostCheckBox.setChecked(false);
            contactInput.setText("");
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            itemImage.setImageURI(imageUri);  // Display selected image
        }
    }

    // Validate inputs
    private boolean validateInputs() {
        if (titleInput.getText().toString().trim().isEmpty()) {
            titleInput.setError("Title required");
            return false;
        }
        if (descriptionInput.getText().toString().trim().isEmpty()) {
            descriptionInput.setError("Description required");
            return false;
        }
        if (contactInput.getText().toString().trim().isEmpty()) {
            contactInput.setError("Contact information required");
            return false;
        }
        if (imageUri == null) {
            Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Resize image if necessary (below 1MB)
    private Bitmap resizeImage(Uri imageUri) throws IOException {
        InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        // Resize if the image is too large
        final int MAX_IMAGE_SIZE = 1024 * 1024; // 1MB in bytes
        if (bitmap != null) {
            int imageSize = bitmap.getByteCount();
            if (imageSize > MAX_IMAGE_SIZE) {
                int scaleFactor = (int) Math.ceil(Math.sqrt((double) imageSize / MAX_IMAGE_SIZE));
                bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / scaleFactor, bitmap.getHeight() / scaleFactor, true);
            }
        }
        return bitmap;
    }

    // Save the item to the local database
    private void saveItemToDatabase() {
        progressDialog.show();

        try {

            // Get the current logged-in user's email from Firebase
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            String userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : "unknown_user";  // Use a default value if the user is not logged in

            // Generate the timestamp as the serial number
            String serialNumber = String.valueOf(System.currentTimeMillis());  // Using current timestamp as serial number


            // Resize image before upload if it's too large
            Bitmap resizedBitmap = resizeImage(imageUri);

            // Convert the resized image to a Base64 string
            String base64Image = encodeImageToBase64(resizedBitmap);

            // Get user inputs
            String title = titleInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();
            String contact = contactInput.getText().toString().trim();
            boolean isLost = isLostCheckBox.isChecked(); // Get checkbox status

// Create an Item object with the new fields
            Item item = new Item(title, base64Image, description, userId, serialNumber, isLost, contact);

            // Insert into Room Database
            new Thread(() -> {
                db.itemDao().insert(item);  // Save item in the background thread
                getActivity().runOnUiThread(() -> {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Item Saved", Toast.LENGTH_SHORT).show();

                    // Clear the EditTexts and reset the ImageView
                    titleInput.setText("");
                    descriptionInput.setText("");
                    itemImage.setImageURI(null); // Clear the selected image
                    isLostCheckBox.setChecked(false);
                    contactInput.setText("");


                    ((MainTabs) requireActivity()).navigateToMyUploads();
                });
            }).start();

        } catch (IOException e) {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Error resizing image", Toast.LENGTH_SHORT).show();
        }
    }

    // Convert Bitmap to Base64 string
    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
