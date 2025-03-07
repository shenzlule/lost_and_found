package com.example.lost_and_found.ui.frags;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lost_and_found.DataBase.AppDatabase;
import com.example.lost_and_found.R;
import com.example.lost_and_found.adapters.ItemAdapter;
import com.example.lost_and_found.models.Item;
import com.example.lost_and_found.models.ItemDao;
import com.example.lost_and_found.ui.pages.ItemDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimelineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimelineFragment extends Fragment {

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
    private Button myUploadsButton, uploadButton;

    private ImageView profileImageView;

    private TextView noItem;
    private   ItemDao itemDao;
    private FirebaseAuth mAuth;


    public TimelineFragment() {
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
    public static TimelineFragment newInstance(String param1, String param2) {
        TimelineFragment fragment = new TimelineFragment();
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
        View view =inflater.inflate(R.layout.fragment_timeline, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        noItem = view.findViewById(R.id.noitems);


        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 items per row


        itemList = new ArrayList<>();






        adapter = new ItemAdapter(itemList, getContext(), item -> {
            Intent intent = new Intent(getContext(), ItemDetailActivity.class);
            intent.putExtra("item", item);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);



        recyclerView.setAdapter(adapter);


        AppDatabase db = Room.databaseBuilder(getContext(), AppDatabase.class, "app_database").build();
         itemDao = db.itemDao();


        if (itemDao != null) {
            loadItems();  // Only call if `itemDao` is initialized
        }


        return  view;
    }


    public void refresh() {
        loadItems();  // Reload the items when refreshing the fragment
    }



    private void loadItems() {

        if (itemDao == null) {
            return;  // Exit early if `itemDao` is not initialized
        }
        // Perform the query on a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Query the Room database for all items

                if (itemDao != null) {
                    List<Item> itemsFromDb = itemDao.getAllItems();

                    if(getActivity() != null){
                    getActivity().runOnUiThread(() -> {
                        itemList.clear();
                        itemList.addAll(itemsFromDb);
                        adapter.notifyDataSetChanged();
                        noItem.setVisibility(itemList.isEmpty() ? VISIBLE : GONE);
                    });}
                }


            }
        }).start();
    }

}