package tut.ac.za.openmerchant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import tut.ac.za.openmerchant.Classes.Constants;
import tut.ac.za.openmerchant.Classes.Store;

public class StoreConfirmActivity extends AppCompatActivity {

    private Store store;
    @BindView(R.id.btnConfirm) Button btnConfirm;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_confirm);


        ButterKnife.bind(this);
        Intent intent = getIntent();

    }


    public void onClick(View view)
    {
        db.child("Store").child("StoreList").push().setValue(store);
        Toast.makeText(StoreConfirmActivity.this, "Store has been added", Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(StoreConfirmActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
