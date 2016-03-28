package com.benwong.buffettman;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements LocationListener {

    String consumerKey;
    String consumerSecret;
    String token;
    String tokenSecret;
    private List<Buffet> mBuffetList;

    private List<String> mBuffetSummaryList;

    ArrayAdapter<String> arrayAdapter;

    ListView buffetListView;


    Double lat;
    Double lng;
    Double alt;
    Float bearing;
    Float speed;
    Float accuracy;

    TextView addressTV;

    LocationManager locationManager;
    String provider;
    Location location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addressTV = (TextView) findViewById(R.id.addressTV);
        mBuffetList = new ArrayList<Buffet>();
        mBuffetSummaryList = new ArrayList<String>();

        buffetListView = (ListView) findViewById(R.id.listView);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        // check to see if location is turned on
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (gpsEnabled && networkEnabled && location != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0.0f, this);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 0.0f, this);
        }

        provider = locationManager.getBestProvider(new Criteria(), false);

        location = locationManager.getLastKnownLocation(provider);

        onLocationChanged(location);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//
//            startActivity(intent);
            onLocationChanged(location);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {


            lat = location.getLatitude();
            lng = location.getLongitude();
            alt = location.getAltitude();
            bearing = location.getBearing();
            speed = location.getSpeed();
            accuracy = location.getAccuracy();


            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());


            List<Address> listAddresses = null;
            try {
                listAddresses = geocoder.getFromLocation(lat, lng, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (listAddresses != null && listAddresses.size() > 0) {

                Log.i("PlaceInfo", listAddresses.get(0).toString());

                String addressHolder = "";

                for (int i = 0; i <= listAddresses.get(0).getMaxAddressLineIndex(); i++) {

                    addressHolder += listAddresses.get(0).getAddressLine(i) + "\n";
                    System.out.println(addressHolder);
                }

                addressTV.setText(addressHolder);

            }

            NumberFormat formatter = new DecimalFormat("#0.00");
            lat = Double.valueOf(formatter.format(lat));
            lng = Double.valueOf(formatter.format(lng));

            System.out.println("Latitude" + String.valueOf(lat));
            System.out.println("Longitude" + String.valueOf(lng));

            consumerKey = "IoIwiQhdfvWH0Lwa5yk2OA";
            consumerSecret = "G-K0sy5M56VQlqNdZg_0hvkDkbM";
            token = "00NVtQ9wgsG1k6EjmKrLdq4fr5uOVWuA";
            tokenSecret = "3rBTB-G7ejHUZsvmiKpoYWWCtWI";

            YelpAPIFactory apiFactory = new YelpAPIFactory(consumerKey, consumerSecret, token, tokenSecret);
            YelpAPI yelpAPI = apiFactory.createAPI();

            Map<String, String> params = new HashMap<>();

            params.put("term", "buffet");
            params.put("term", "all you can eat");
//        params.put("limit", "30");

            CoordinateOptions coordinate = CoordinateOptions.builder()
                    .latitude(lat)
                    .longitude(lng).build();
//
            Call<SearchResponse> call = yelpAPI.search(coordinate, params);

            Callback<SearchResponse> callback = new Callback<SearchResponse>() {
                @Override
                public void onResponse(Response<SearchResponse> response, Retrofit retrofit) {
                    SearchResponse searchResponse = response.body();
//                // Update UI text with the searchResponse.
//                System.out.println("Response from YELP " + response.body().businesses().get(0));
                    mBuffetList.clear();
                    mBuffetSummaryList.clear();
                    System.out.println(response.body().businesses().get(0));
                    for (int i = 0; i < response.body().businesses().size(); i++) {
                        System.out.println("YELP " + i + " " + response.body().businesses().get(i));
//                    System.out.println("YELP " + i + " " +  response.body().businesses().get(i).distance()/1000 + " m");
                        Buffet buffetItem = new Buffet();
                        buffetItem.setName(response.body().businesses().get(i).name());
                        buffetItem.setDistance(response.body().businesses().get(i).distance() / 1000);
                        buffetItem.setCategory(String.valueOf(response.body().businesses().get(i).categories().get(0).name()));
                        buffetItem.setRating(response.body().businesses().get(i).rating());
                        buffetItem.setPhoneNumber(response.body().businesses().get(i).phone());
                        buffetItem.setAddress(String.valueOf(response.body().businesses().get(i).location().displayAddress()));
                        buffetItem.setImageURL(response.body().businesses().get(i).imageUrl());
                        buffetItem.setSummary(response.body().businesses().get(i).snippetText());
                        mBuffetList.add(buffetItem);


                        //                    mBuffetSummaryList.add(response.body().businesses().get(i).name() + " " + response.body().businesses().get(i).distance());
//
//                    System.out.println(response.body().businesses().get(i).name() + " " + response.body().businesses().get(i).distance());
                    }

                    Collections.sort(mBuffetList);
                    for (Buffet item : mBuffetList) {
                        String formattedDistance = String.format("%.2f", item.getDistance());
                        mBuffetSummaryList.add(item.getName() + " - " + item.getCategory() + " - " + formattedDistance + " km " + " - " + item.getRating() + "/5 ");

                    }


                    arrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, mBuffetSummaryList);

                    buffetListView.setAdapter(arrayAdapter);

                    buffetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            System.out.println(position);

                            Intent intent = new Intent(getApplicationContext(), BuffetDetailActivity.class);
                            intent.putExtra("name", mBuffetList.get(position).getName());
                            intent.putExtra("address", mBuffetList.get(position).getAddress());
                            intent.putExtra("category", mBuffetList.get(position).getCategory());
                            intent.putExtra("rating", mBuffetList.get(position).getRating());
                            intent.putExtra("phone", mBuffetList.get(position).getPhoneNumber());
                            intent.putExtra("image", mBuffetList.get(position).getImageURL());
                            intent.putExtra("summary", mBuffetList.get(position).getSummary());
                            startActivity(intent);
                        }
                    });

                }

                @Override
                public void onFailure(Throwable t) {
                    // HTTP error happened, do something to handle it.
                }
            };

            call.enqueue(callback);


        } else {
            Toast.makeText(getApplicationContext(), "Location cannot be found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
