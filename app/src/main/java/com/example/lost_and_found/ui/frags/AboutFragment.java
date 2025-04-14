package com.example.lost_and_found.ui.frags;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.lost_and_found.DataBase.AppDatabase;
import com.example.lost_and_found.R;
import com.example.lost_and_found.adapters.ItemAdapter;
import com.example.lost_and_found.models.Item;
import com.example.lost_and_found.models.ItemDao;
import com.example.lost_and_found.ui.pages.ItemDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private RecyclerView recyclerView;
    private List<Item> itemList;
    private ItemAdapter adapter;
    private FirebaseFirestore db;
    private Button myUploadsButton, uploadButton,contactSupportButton;

    private ImageView profileImageView;

    private LinearLayout contactl1;
    private RelativeLayout contact;
    private TextView noItem;
    private   ItemDao itemDao;
    private FirebaseAuth mAuth;


    public AboutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimelineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
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
        View view =inflater.inflate(R.layout.fragment_about, container, false);

        contactSupportButton= view.findViewById(R.id.contactSupportButton);

        contact=view.findViewById(R.id.contact);
        contactl1=view.findViewById(R.id.contactl1);

        contact.setVisibility(GONE);
        contactl1.setVisibility(VISIBLE);
        contactSupportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contact.setVisibility(VISIBLE);
                contactl1.setVisibility(GONE);
            }
        });




        return  view;
    }



}