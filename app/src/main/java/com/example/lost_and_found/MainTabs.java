package com.example.lost_and_found;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.lost_and_found.adapters.ItemAdapter;
import com.example.lost_and_found.models.Item;
import com.example.lost_and_found.ui.frags.AboutFragment;
import com.example.lost_and_found.ui.frags.MyFragment;
import com.example.lost_and_found.ui.frags.ProfileFragment;
import com.example.lost_and_found.ui.frags.TimelineFragment;
import com.example.lost_and_found.ui.frags.UploadFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainTabs extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter adapter;

    private ImageView profileImageView;

    private FirebaseAuth mAuth;

    private TextView hi_txt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_main);





        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(this, GoogleSignInActivity.class));
            finish();
        }

        profileImageView = findViewById(R.id.profileImage);

        hi_txt = findViewById(R.id.hi);

        // Load profile picture using Glide (make sure to include Glide in your dependencies)
        if (currentUser.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(currentUser.getPhotoUrl()) // Use the photo URL from FirebaseAuth
                    .circleCrop() // To make it round
                    .into(profileImageView);
        }

        loadUserProfile();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ProfileFragment fragmentProfile = new ProfileFragment();
        UploadFragment fragmentupload = new UploadFragment();
        MyFragment fragmentmy = new MyFragment();
        TimelineFragment fragmenttimeline = new TimelineFragment();

        AboutFragment fragmentAbout = new AboutFragment();








        viewPager = findViewById(R.id.viewpager);
            tabLayout = findViewById(R.id.tabs);
            adapter = new ViewPagerAdapter(this);

            adapter.addFragment(fragmenttimeline, "TimeLine");
            adapter.addFragment(fragmentmy, "My uploads");
            adapter.addFragment(fragmentupload, "Upload");
            adapter.addFragment(fragmentProfile, "Profile");
            adapter.addFragment(fragmentAbout, "About");

            viewPager.setAdapter(adapter);



        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(adapter.getFragmentTitle(position))
        ).attach();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Fragment fragment = adapter.createFragment(position);
//                if (fragment instanceof RefreshableFragment) {
//                    ((RefreshableFragment) fragment).refresh();
//                }
                // Check if the fragment is an instance of MyFragment
                if (fragment instanceof MyFragment) {
                    ((MyFragment) fragment).refresh();  // Call the refresh method to reload data
                }

                if (fragment instanceof TimelineFragment) {
                    ((TimelineFragment) fragment).refresh();  // Call the refresh method to reload data
                }

                if (fragment instanceof UploadFragment) {
                    ((UploadFragment) fragment).refresh();  // Call the refresh method to reload data
                }
            }
        });



        // Check if an extra was passed
        int goToUploads = getIntent().getIntExtra("goToUploads", 0);
        if (goToUploads == 1) {
            viewPager.setCurrentItem(1, true); // Navigate to "My Uploads"
        }



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


    public void navigateToMyUploads() {
        viewPager.setCurrentItem(1, true); // 1 is the index of "My uploads"
    }



    private static class ViewPagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);

        }



        public String getFragmentTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }





}
