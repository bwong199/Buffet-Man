package com.benwong.buffettman;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class BuffetDetailActivity extends AppCompatActivity  {

//    private GoogleMap mMap;


    Intent intent;

    String name;
    String address;
    String category;
    Double rating;
    String phoneNumber;
    String imageURL;
    String summary;
    Integer reviewCount;


    TextView nameTV;
    TextView categoryTV;
    TextView addressTV;
    TextView phoneTV;
    TextView ratingTV;
    TextView summaryTV;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffet_detail);


        nameTV = (TextView) findViewById(R.id.nameTV);
        categoryTV = (TextView) findViewById(R.id.categoryTV);
        addressTV = (TextView) findViewById(R.id.addressTV);
        phoneTV = (TextView) findViewById(R.id.phoneTV);
        imageView = (ImageView) findViewById(R.id.imageView);
        ratingTV = (TextView) findViewById(R.id.ratingTV);
        summaryTV = (TextView) findViewById(R.id.summaryTV);

        intent = getIntent();

        System.out.println(intent);

        name = intent.getStringExtra("name");
        address = intent.getStringExtra("address");

        address = address.substring(1, address.length() - 1);


        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());


        List<Address> listAddresses = null;
        try {
            listAddresses = geocoder.getFromLocationName(address, 1);
            System.out.println(listAddresses);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        if (listAddresses != null && listAddresses.size() > 0) {
//
//            Log.i("PlaceInfo", listAddresses.get(0).toString());
//
//            String addressHolder = "";
//
//            for (int i = 0; i <= listAddresses.get(0).getMaxAddressLineIndex(); i++) {
//
//                addressHolder += listAddresses.get(0).getAddressLine(i) + "\n";
//                System.out.println(addressHolder);
//            }
//
//            addressTV.setText(addressHolder);
//
//        }

        category = intent.getStringExtra("category");
        rating = intent.getDoubleExtra("rating", 0);
        phoneNumber = intent.getStringExtra("phone");
        imageURL = intent.getStringExtra("image");
        summary = intent.getStringExtra("summary");
        reviewCount = intent.getIntExtra("reviewCount", 0);

        System.out.println(name + address + category + rating + phoneNumber + imageURL);

        nameTV.setText(name);
        categoryTV.setText(category);
        addressTV.setText(address);
        phoneTV.setText(phoneNumber);
        ratingTV.setText(String.valueOf(rating) + "/5 - " + String.valueOf(reviewCount) + " reviews ");
        summaryTV.setText(summary);

        phoneTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        addressTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=" + address));

                startActivity(intent);
            }
        });

        Picasso.with(getApplicationContext()).load(imageURL).into(imageView);


    }


}
