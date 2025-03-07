package com.example.lost_and_found.ui.frags;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.lost_and_found.DataBase.AppDatabase;
import com.example.lost_and_found.GoogleSignInActivity;
import com.example.lost_and_found.R;
import com.example.lost_and_found.adapters.ItemMyAdapter;
import com.example.lost_and_found.models.Item;
import com.example.lost_and_found.models.ItemDao;
import com.example.lost_and_found.ui.pages.ItemDetailActivity;
import com.example.lost_and_found.ui.pages.ItemDetailActivityMy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private List<Item> myItems;
    private ItemMyAdapter adapter;
    private FirebaseFirestore db;
    private String userId;
    private  TextView noItem;

    private ItemDao itemDao;

    public MyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance(String param1, String param2) {
        MyFragment fragment = new MyFragment();
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
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_my);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        myItems = new ArrayList<>();
        noItem = view.findViewById(R.id.noitems_my);

        adapter = new ItemMyAdapter(myItems, getContext(), item -> {
            Intent intent = new Intent(getContext(), ItemDetailActivityMy.class);
            intent.putExtra("item", item);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        // Ensure `itemDao` is properly initialized
        AppDatabase database = Room.databaseBuilder(requireContext(), AppDatabase.class, "app_database").build();
        itemDao = database.itemDao();

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (itemDao != null) {
            loadItems();  // Only call if `itemDao` is initialized
        }

        return view;
    }


    public void refresh() {
        loadItems();  // Reload the items when refreshing the fragment
    }

    private void loadItems() {
        if (itemDao == null) {
            return;  // Exit early if `itemDao` is not initialized
        }

        new Thread(() -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            String userEmail = (user != null && user.getEmail() != null) ? user.getEmail() : "none";

            // Check if `itemDao` is still null before querying
            if (itemDao != null) {
                List<Item> itemsFromDb = itemDao.getItemsByUserId(userEmail);
                if(getActivity() != null) {

                    getActivity().runOnUiThread(() -> {
                        myItems.clear();
                        myItems.addAll(itemsFromDb);
                        adapter.notifyDataSetChanged();
                        noItem.setVisibility(myItems.isEmpty() ? VISIBLE : GONE);
                    });
                }
            }
        }).start();
    }




}