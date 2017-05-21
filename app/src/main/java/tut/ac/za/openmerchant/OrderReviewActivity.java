package tut.ac.za.openmerchant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tut.ac.za.openmerchant.Classes.Constants;
import tut.ac.za.openmerchant.Classes.Item;
import tut.ac.za.openmerchant.Classes.Order;

public class OrderReviewActivity extends AppCompatActivity {

    private Order order;
    private Button btnCheckout;
    private DatabaseReference db;
    private FirebaseAuth mAuth;
    @BindView(R.id.tvDeliveryFee)
    TextView tvDeliveryFee;
    @BindView(R.id.tvDestinationLocation) TextView tvDestinationLocation;
    @BindView(R.id.tvPickupLocation) TextView tvPickUpLocation;
    @BindView(R.id.tvTotItemPrice) TextView tvTotItemPrice;
    @BindView(R.id.tvItemsNo) TextView tvItemsNo;
    @BindView(R.id.tvDistance) TextView tvDistance;
    @BindView(R.id.tvTime) TextView tvTime;
    @BindView(R.id.tvTotPrice) TextView tvTotPrice;
    private double totAmnt=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_review);


        Intent intent = getIntent();
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        order = (Order) intent.getSerializableExtra(Constants.ORDER);
        totAmnt = totItemPrice(order.getStore().getItems());

       Toast.makeText(OrderReviewActivity.this,String.valueOf(order.getCounter().getDistanceInMiles()), Toast.LENGTH_SHORT).show();
        initialize();
    }


    public void onCheckOut(View view)
    {


        Intent intent = new Intent(OrderReviewActivity.this,CardActivity.class);
        intent.putExtra(Constants.ORDER,order);
        startActivity(intent);


    }


    private void initialize()
    {


       order.setTotItemPrice(totAmnt);
        double distanceKM = (Double.parseDouble(order.getCounter().getDistanceInMiles().trim().replace("mi","").trim()) *Constants.MILES_TO_KN);
        double deliveryFee = Double.parseDouble(String.format("%.2f",distanceKM *Constants.COST_PER_KILOMETER));
        tvDeliveryFee.setText("R"+ String.format("%.2f",distanceKM *Constants.COST_PER_KILOMETER));
        tvDestinationLocation.setText(order.getDeliveryAddress());
        tvPickUpLocation.setText(order.getStoreArea());
        tvTotItemPrice.setText("R"+order.getTotItemPrice());
        tvItemsNo.setText(String.valueOf(order.getStore().getItems().size()));
        tvDistance.setText(String.format("%.2f",distanceKM) +" KM");
        tvTime.setText(order.getCounter().getDurationInMinutes());
        order.setDeliveryFee(Double.parseDouble(String.format("%.2f",distanceKM *Constants.COST_PER_KILOMETER)));
        tvTotPrice.setText("R"+ String.format("%.2f",(order.getDeliveryFee() + order.getTotItemPrice())));


    }

    private double totItemPrice(List<Item> items)
    {

        double amnt =0;


        for(Item item: items)
        {

            amnt+=item.getPrice();

        }

        return  amnt;
    }

}
