package com.benwong.buffettman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class BuffetDetailActivity extends AppCompatActivity {

    Intent intent;

    String name;
    String address;
    String category;
    Double rating;
    String phoneNumber;
    String imageURL;


    TextView nameTV;
    TextView categoryTV;
    TextView addressTV;
    TextView phoneTV;
    TextView ratingTV;
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


        intent = getIntent();

        System.out.println(intent);

        name = intent.getStringExtra("name");
        address = intent.getStringExtra("address");
        category = intent.getStringExtra("category");
        rating = intent.getDoubleExtra("rating", 0);
        phoneNumber = intent.getStringExtra("phone");
        imageURL = intent.getStringExtra("image");

        System.out.println(name + address + category + rating + phoneNumber + imageURL);

        nameTV.setText(name);
        categoryTV.setText(category);
        addressTV.setText(address);
        phoneTV.setText(phoneNumber);
        ratingTV.setText(String.valueOf(rating) + "/5 Rating");

        Picasso.with(getApplicationContext()).load(imageURL).into(imageView);


    }

}
