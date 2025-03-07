package com.example.lost_and_found.ui.frags;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lost_and_found.GoogleSignInActivity;
import com.example.lost_and_found.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ImageView profileImage;
    private TextView profileName, profileEmail;
    private Button logoutButton;
    private FirebaseAuth mAuth;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Views
        profileImage = view.findViewById(R.id.profile_image);
        profileName = view.findViewById(R.id.profile_name);
        profileEmail = view.findViewById(R.id.profile_email);
        logoutButton = view.findViewById(R.id.btn_logout);

        // Load User Data
        loadUserProfile();

        // Logout Button Click Listener
        logoutButton.setOnClickListener(v -> logoutUser());

        return view;
    }


    private void loadUserProfile() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            profileEmail.setText(user.getEmail());

            // Set full name (assuming displayName contains "FirstName LastName")
            profileName.setText(user.getDisplayName());

            // Load profile image using Glide
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .placeholder(R.drawable.icon) // Default image
                        .circleCrop() // Ensures the image is circular
                        .into(profileImage);
            }

        }
    }

    private void logoutUser() {
        if (getActivity() == null) return; // Prevent null reference

        FirebaseAuth.getInstance().signOut(); // Firebase logout

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(),
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build());

        googleSignInClient.signOut().addOnCompleteListener(getActivity(), task -> {
            googleSignInClient.revokeAccess().addOnCompleteListener(getActivity(), task1 -> {
                Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();

                // Redirect to SignInActivity
                Intent intent = new Intent(getActivity(), GoogleSignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            });
        });
    }






}

