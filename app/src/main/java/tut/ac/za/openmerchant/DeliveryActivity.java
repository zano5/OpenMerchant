package tut.ac.za.openmerchant;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import tut.ac.za.openmerchant.Classes.Constants;
import tut.ac.za.openmerchant.Classes.DistanceCounter;
import tut.ac.za.openmerchant.Classes.Order;
import tut.ac.za.openmerchant.ui.GeoTask;

public class DeliveryActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String TAG="Delivery";
    private Geocoder mGeocoder;
    private Order order;
    private List<Address> addressList;
    private String storeArea;
    private double storeLatitude;
    private double storeLongitude;
    PlaceAutocompleteFragment autocompleteFragment;
    private String json_String="";
    private DistanceCounter counter;
    @BindView(R.id.btnSubmit) Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        ButterKnife.bind(this);
        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra(Constants.ORDER);
        mGeocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        storeArea = order.getStore().getAddress().getCity() + " " + order.getStore().getAddress().getTown() + " " + order.getStore().getAddress().getStreet();
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    json_String = generateMatrix();
                    counter = jsonConvert(json_String);
                   order.setCounter(counter);
                    order.setStoreArea(storeArea);
                    Intent intent = new Intent(DeliveryActivity.this,OrderReviewActivity.class);
                   intent.putExtra(Constants.ORDER,order);
                    startActivity(intent);

                    Toast.makeText(DeliveryActivity.this,counter.getDistanceInMiles(),Toast.LENGTH_SHORT).show();
                    Toast.makeText(DeliveryActivity.this,counter.getDurationInMinutes(),Toast.LENGTH_SHORT).show();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera

        try {


            addressList = mGeocoder.getFromLocationName(storeArea,1);

            if(addressList.size() !=0) {
                storeLatitude = addressList.get(0).getLatitude();
                storeLongitude = addressList.get(0).getLongitude();

                String name = addressList.get(0).getLocality();

                LatLng sydney = new LatLng(storeLatitude, storeLongitude);
                mMap.addMarker(new MarkerOptions().position(sydney).title(name));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            }else
            {
                Toast.makeText(DeliveryActivity.this,"Could not locate the store address",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DeliveryActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());


               order.setDeliveryAddress(place.getAddress().toString());

                mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });


    }


   /* public void onSubmit(View view)
    {


           try {
                json_String = generateMatrix();
               counter = jsonConvert(json_String);
                order.setCounter(counter);
                order.setStoreArea(storeArea);
                Intent intent = new Intent(DeliveryActivity.this,OrderReviewActivity.class);
                intent.putExtra(Constants.ORDER,order);
                startActivity(intent);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Toast.makeText(DeliveryActivity.this,"Working",Toast.LENGTH_SHORT).show();



    }*/


    public String generateMatrix() throws ExecutionException, InterruptedException {


        String url ="https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="+storeArea.replace(" ","+")+ "&destinations=" +order.getDeliveryAddress().toString().replace(" ","+")+"&mode=driving&key=AIzaSyA0NVj3vJFMPzqmJagB38nJuC8c_wGsx9U";


        return new GeoTask(DeliveryActivity.this).execute(url).get();
    }


    public DistanceCounter jsonConvert(String respose)
    {

        String distanceText="";
        String durationText="";


        if (respose != null) {
            try {
                JSONObject json_response = new JSONObject(respose);

                Log.v("JSON", json_response.toString());

                if ("OK".equals(json_response.getString("status"))) {

                    JSONArray rows = json_response.getJSONArray("rows");

                    for (int i = 0; i < rows.length(); i++) {

                        JSONObject row = rows.getJSONObject(i);

                        JSONArray elements = row.getJSONArray("elements");

                        JSONObject element = elements.getJSONObject(i);

                        JSONObject distance = element.getJSONObject("distance");

                        distanceText = distance.getString("text");

                        JSONObject duration = element.getJSONObject("duration");

                        durationText = duration.getString("text");

                        Log.v("JSON", durationText.toString());

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(DeliveryActivity.this, "Error4!Please Try Again wiht proper values", Toast.LENGTH_SHORT).show();


        return  new DistanceCounter(distanceText,durationText);
    }
}
