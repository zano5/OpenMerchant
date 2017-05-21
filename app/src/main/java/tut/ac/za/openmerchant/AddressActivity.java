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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tut.ac.za.openmerchant.Classes.Address;
import tut.ac.za.openmerchant.Classes.Constants;
import tut.ac.za.openmerchant.Classes.Item;
import tut.ac.za.openmerchant.Classes.Store;

public class AddressActivity extends AppCompatActivity {

    @BindView(R.id.etCountry)
    EditText etCountry;
    @BindView(R.id.etCity) EditText etCity;
    @BindView(R.id.etCode) EditText etCode;
    @BindView(R.id.etStreet) EditText etStreet;
    @BindView(R.id.etTown) EditText etTown;
    @BindView(R.id.etProvince) EditText etProvince;
    @BindView(R.id.btnSubmit) Button btnSubmit;
    private Store store;
    private DatabaseReference db;
    private boolean is_store = false;
    private StorageReference mStorageRef;
    private List<Item>  items;
    private Address address;
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();
    private Store data;
    private String url;
    private String randomStore;
    private String randomItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        Intent intent = getIntent();
        db = FirebaseDatabase.getInstance().getReference();
        store = (Store) intent.getSerializableExtra(Constants.STORE);
        is_store = intent.getBooleanExtra(Constants.IS_STORE,false);
        items = new ArrayList<Item>();


        Toast.makeText(AddressActivity.this,store.getStoreUrl(),Toast.LENGTH_SHORT).show();

    }


    public void onSubmit(View view)
    {

    if("".equals(etCity.getText().toString()))
    {
        Toast.makeText(AddressActivity.this,"City Field Must Not Be Empty",Toast.LENGTH_SHORT).show();
    }else if("".equals(etCode.getText().toString()))
    {
        Toast.makeText(AddressActivity.this,"Code Field Must Not Be Empty",Toast.LENGTH_SHORT).show();

    }else if("".equals(etCountry.getText().toString()))
    {

        Toast.makeText(AddressActivity.this,"Country Field Must Not Be Empty",Toast.LENGTH_SHORT).show();
    }else if("".equals(etStreet.getText().toString()))
    {
        Toast.makeText(AddressActivity.this,"Street Field Must Not Be Empty",Toast.LENGTH_SHORT).show();

    }else if("".equals(etTown.getText().toString()))
    {
        Toast.makeText(AddressActivity.this,"Town Field Must Not Be Empty",Toast.LENGTH_SHORT).show();
    }else if("".equals(etProvince.getText().toString()))
    {
        Toast.makeText(AddressActivity.this,"Province Field Must Not Be Empty",Toast.LENGTH_SHORT).show();
    }else
    {





            address = new Address();

            address.setCity(etCity.getText().toString());
            address.setCountry(etCountry.getText().toString());
            address.setProvince(etProvince.getText().toString());
            address.setStreet(etStreet.getText().toString());
            address.setTown(etTown.getText().toString());
            address.setCode(etCode.getText().toString());

            store.setAddress(address);





        db= FirebaseDatabase.getInstance().getReference();

        db.child("Store").child("StoreList").push().setValue(store);
        Toast.makeText(AddressActivity.this, "Store has been added", Toast.LENGTH_SHORT).show();


        Intent intent = new Intent(AddressActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
            }

    }



}
