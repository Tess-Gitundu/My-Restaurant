package com.example.gitundu.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gitundu.Constants;
import com.example.gitundu.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.example.gitundu.adapters.RestaurantListAdapter;
import com.example.gitundu.models.Business;
import com.example.gitundu.models.YelpBusinessesSearchResponse;
import com.example.gitundu.network.YelpApi;
import com.example.gitundu.network.YelpClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantListActivity extends AppCompatActivity {

//    private SharedPreferences mSharedPreferences;
//    private String mRecentAddress;

    private static final String TAG = RestaurantListActivity.class.getSimpleName();
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.errorTextView) TextView mErrorTextView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;

    private RestaurantListAdapter mAdapter;

    public List<Business> restaurants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

//        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        mRecentAddress = mSharedPreferences.getString(Constants.PREFERENCES_LOCATION_KEY, null);
//        Log.d("Shared Pref Location", mRecentAddress);

        Intent intent = getIntent(); // to recreate the intent in the main activity
        String location = intent.getStringExtra("location");

//        String location = mRecentAddress;


        YelpApi client = YelpClient.getClient();
        Call<YelpBusinessesSearchResponse> call = client.getRestaurants(location, "restaurants");
        call.enqueue(new Callback<YelpBusinessesSearchResponse>() {
            @Override
            public void onResponse(Call<YelpBusinessesSearchResponse> call, Response<YelpBusinessesSearchResponse> response) {

                hideProgressBar();

                if (response.isSuccessful()) {
                    restaurants = response.body().getBusinesses();
                    mAdapter = new RestaurantListAdapter(RestaurantListActivity.this, restaurants);
                    mRecyclerView.setAdapter(mAdapter);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RestaurantListActivity.this);
                    mRecyclerView.setLayoutManager(layoutManager);
                    mRecyclerView.setHasFixedSize(true);

                    showRestaurants();
                } else {
                    showUnsuccessfulMessage();
                }
            }

            @Override
            public void onFailure(Call<YelpBusinessesSearchResponse> call, Throwable t) {
                Log.e("Error Message", "onFailure: ", t);
                hideProgressBar();
                showFailureMessage();
            }
        });
    }

    private void showFailureMessage() {
        mErrorTextView.setText("Something went wrong. Please check your internet connection and try again later.");
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showUnsuccessfulMessage() {
        mErrorTextView.setText("Something went wrong. Please try again later.");
        mErrorTextView.setVisibility(View.VISIBLE);
    }

    private void showRestaurants() {
       mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }
}