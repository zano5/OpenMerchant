package tut.ac.za.openmerchant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.List;

import tut.ac.za.openmerchant.Adapters.SlipAdapter;
import tut.ac.za.openmerchant.Classes.Constants;
import tut.ac.za.openmerchant.Classes.Item;
import tut.ac.za.openmerchant.Classes.Order;
import tut.ac.za.openmerchant.Classes.Payment;
import tut.ac.za.openmerchant.ui.SpacesItemDecoration;

public class SlipActivity extends AppCompatActivity {


    private Order order;
    private TextView  tvTotalSlip;
    private RecyclerView rvSlip;
    private SlipAdapter adapter;
    private DatabaseReference db;
    private double totAmnt=0;
    private Button btnSubmit;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slip);

        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("Slip");
        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra(Constants.ORDER);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        rvSlip = (RecyclerView) findViewById(R.id.rvSlip);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.card_spacing_cart);
        rvSlip.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        rvSlip.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseDatabase.getInstance().getReference();
        tvTotalSlip = (TextView) findViewById(R.id.tvTotalSlip);
      adapter = new SlipAdapter(this, order.getStore().getItems());

       rvSlip.setAdapter(adapter);

       initialize();

    }


    public  void  onClick(View view)
    {

        Payment payment = new Payment();
        payment.setPaymentType("online");
        payment.setAmntDue(totAmnt);
        payment.setAmntPaid(0);
        order.setTotItemPrice(totAmnt);
        order.setPayment(payment);
        order.setDate(new Date().toString());
      db.child("Order").child("OrderList").child(mAuth.getCurrentUser().getUid()).push().setValue(order);
        Toast.makeText(SlipActivity.this,"Order request made",Toast.LENGTH_SHORT).show();
        Intent intent1 = new Intent(SlipActivity.this,MainActivity.class);
       startActivity(intent1);


    }




    public double totItemAmnt(List<Item> itemList)
    {

        double amnt = 0;
        for(Item item: itemList)
        {

            amnt+=item.getPrice();
        }

        return   amnt;
    }


    private void initialize() {

        double amnt = totItemAmnt(order.getStore().getItems());
        tvTotalSlip.setText("Total: R" + String.format("%.2f",amnt));
    }
}
