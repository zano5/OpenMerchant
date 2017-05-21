package tut.ac.za.openmerchant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import tut.ac.za.openmerchant.Classes.Constants;
import tut.ac.za.openmerchant.Classes.Order;

public class CardActivity extends AppCompatActivity {

    @BindView(R.id.etExpiry) EditText etExpiry;
    @BindView(R.id.etCvc) EditText etCvc;
    @BindView(R.id.etCarNo) EditText etCardNo;
    @BindView(R.id.btnSubmit) Button btnSubmit;
    private DatabaseReference db;
    private Order order;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        ButterKnife.bind(this);
        db = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra(Constants.ORDER);
    }


    public void onSubmit(View view)
    {

      if("".equals(etCardNo.getText().toString()))
        {
            Toast.makeText(CardActivity.this,"Card No Field Must Not Be Empty",Toast.LENGTH_SHORT).show();

        }else if("".equals(etCvc.getText().toString()))
        {
            Toast.makeText(CardActivity.this,"Card No Field Must Not Be Empty",Toast.LENGTH_SHORT).show();

        }else if("".equals(etExpiry.getText().toString()))
        {
            Toast.makeText(CardActivity.this,"Card Expiry Field Must Not Be Empty",Toast.LENGTH_SHORT).show();
        }else  if(etCardNo.getText().toString().length() != 15)
      {

          Toast.makeText(CardActivity.this,"Card No Must Be 15 Numbers Long",Toast.LENGTH_SHORT).show();
      }else if(etCvc.getText().length() != 3)
      {
          Toast.makeText(CardActivity.this,"CVC No Must Be 15 Numbers Long",Toast.LENGTH_SHORT).show();
      } else
        {
            order.setDate(new Date().toString());
            db.child("Order").child("OrderList").child(order.getUser().getUserID()).push().setValue(order);
            Toast.makeText(CardActivity.this,"Order Request Made",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CardActivity.this,MainActivity.class);
            startActivity(intent);
        }

    }
}
