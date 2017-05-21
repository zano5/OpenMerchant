package tut.ac.za.openmerchant;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import tut.ac.za.openmerchant.Adapters.CatalogueAdapter;
import tut.ac.za.openmerchant.Classes.Constants;
import tut.ac.za.openmerchant.Classes.Order;
import tut.ac.za.openmerchant.Classes.Store;
import tut.ac.za.openmerchant.Classes.User;
import tut.ac.za.openmerchant.ui.SpacesItemDecoration;

public class ShoppingCartActivity extends AppCompatActivity {

    private Store store;
    private CatalogueAdapter adapter;
    @BindView(R.id.rvCart) RecyclerView rvCart;
    private String key;
    private FirebaseAuth mAuth;
    private Order order;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        mAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);
        Intent intent = getIntent();
        store = (Store)intent.getSerializableExtra(Constants.STORE);
        key = intent.getStringExtra(Constants.KEY);

       getSupportActionBar().setTitle(store.getName()+"(Cart)");


        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.card_spacing_cart);
        rvCart.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        rvCart.setLayoutManager(new GridLayoutManager(this,2));
       adapter = new CatalogueAdapter(ShoppingCartActivity.this,store.getItems(),store,key);
        rvCart.setAdapter(adapter);



    }


    public void onNext(View view)
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingCartActivity.this);
        builder.setTitle("Payment Options");
        builder.setMessage("What Type payment do you want to do?");
        builder.setPositiveButton("Cash", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                User user = new User();
                user.setUserID(mAuth.getCurrentUser().getUid());
                user.setEmail(mAuth.getCurrentUser().getEmail());
                order  = new Order();
                order.setUser(user);
                order.setStore(store);
                Intent intent1 = new Intent(ShoppingCartActivity.this,SlipActivity.class);
                intent1.putExtra(Constants.ORDER,order);
                startActivity(intent1);
            }
        })
                .setNegativeButton("Online", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        User user = new User();
                        user.setUserID(mAuth.getCurrentUser().getUid());
                        user.setEmail(mAuth.getCurrentUser().getEmail());
                        order  = new Order();
                        order.setUser(user);
                        order.setStore(store);
                            Intent intent = new Intent(ShoppingCartActivity.this,DeliveryActivity.class);
                            intent.putExtra(Constants.ORDER,order);
                            startActivity(intent);
                    }
                });

        Dialog dialog = builder.create();
        dialog.show();


    }
}
