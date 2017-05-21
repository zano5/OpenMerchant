package tut.ac.za.openmerchant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import tut.ac.za.openmerchant.Classes.Constants;
import tut.ac.za.openmerchant.Classes.Order;

public class OrderActivity extends AppCompatActivity {

    private DatabaseReference db;
    private FirebaseAuth mAuth;
    private FirebaseRecyclerAdapter<Order,PreviousOrderViewHolder> adapter;
    private RecyclerView rvPreviousOrder;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference().child("Order").child("OrderList").child(mAuth.getCurrentUser().getUid());

        rvPreviousOrder = (RecyclerView) findViewById(R.id.rvOrder);
        rvPreviousOrder.setLayoutManager(new LinearLayoutManager(this));

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        adapter = new FirebaseRecyclerAdapter<Order, PreviousOrderViewHolder>(Order.class,R.layout.previous_order_view,PreviousOrderViewHolder.class,db) {
            @Override
            protected void populateViewHolder(PreviousOrderViewHolder viewHolder, final Order model, final int position) {

                progressBar.setVisibility(View.GONE);
                viewHolder.setDate(model.getDate());
                CardView cardView = viewHolder.getCdPrevious();

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String key = getRef(position).getKey();
                        Intent intent = new Intent(OrderActivity.this,OrderItemActivity.class);
                        intent.putExtra(Constants.KEY,key);
                        intent.putExtra(Constants.ORDER,model);
                        startActivity(intent);
                    }
                });

            }
        };

        rvPreviousOrder.setAdapter(adapter);
    }


    private static  class PreviousOrderViewHolder extends RecyclerView.ViewHolder
    {
        private View mView;

        public PreviousOrderViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public CardView getCdPrevious()
        {

            CardView cdPrevious = (CardView) mView.findViewById(R.id.cdPrevious);

            return cdPrevious;
        }


        public void setDate(String date)
        {

            TextView tvDate =(TextView) mView.findViewById(R.id.tvDate);
            tvDate.setText(DateFormat.format("MM/dd/yyyy hh:mm:ss a", Date.parse(date)));
        }

    }
}
