package com.example.lost_and_found.utils;

import com.example.lost_and_found.models.Item;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseHelper {
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void uploadItem(Item item, Runnable onSuccess, Runnable onFailure) {
        db.collection("items").add(item)
                .addOnSuccessListener(documentReference -> onSuccess.run())
                .addOnFailureListener(e -> onFailure.run());
    }
}
