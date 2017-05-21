package tut.ac.za.openmerchant;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tut.ac.za.openmerchant.Classes.Constants;
import tut.ac.za.openmerchant.Classes.Item;
import tut.ac.za.openmerchant.Classes.Store;
import tut.ac.za.openmerchant.ui.SpacesItemDecoration;

public class StoreCatalogueActivity extends AppCompatActivity {

    private Store store;
    private String key;
    private DatabaseReference db;
    private FirebaseRecyclerAdapter<Item,ItemViewHolder> adapter;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.rvCatalogue) RecyclerView rvCatalogue;
    public static List<Item> cart = new ArrayList<Item>();
    private boolean isUpdate = false;
    private int position= -1;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        cart.clear();
        setContentView(R.layout.activity_store_catalogue);

        ButterKnife.bind(this);

        final FirebaseStorage storage = FirebaseStorage.getInstance();

        storageRef = storage.getReference();
        Intent intent = getIntent();
        position = intent.getIntExtra(Constants.POSITION,-1);
        isUpdate = intent.getBooleanExtra(Constants.IS_UPDATE,false);
        store = (Store) intent.getSerializableExtra(Constants.STORE);
        key = intent.getStringExtra(Constants.KEY);
        db = FirebaseDatabase.getInstance().getReference().child("Store").child("StoreList").child(key).child("items");

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.card_spacing_cart);
        rvCatalogue.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        rvCatalogue.setLayoutManager(new GridLayoutManager(this,2));




        adapter = new FirebaseRecyclerAdapter<Item,ItemViewHolder>(Item.class,R.layout.item_view,ItemViewHolder.class,db) {
            @Override
            protected void populateViewHolder(final ItemViewHolder viewHolder, final Item model, final int position) {

                progressBar.setVisibility(View.GONE);

                storageRef.child("store/store_item_image/"+model.getUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'


                        viewHolder.setImage(uri.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });

                viewHolder.setItemName(model.getName());
                viewHolder.setItemPrice(model.getPrice());
                CardView cardView = viewHolder.getCardView();


                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        AlertDialog.Builder builder = new AlertDialog.Builder(StoreCatalogueActivity.this);


                        builder.setTitle("Store Cart");
                        builder.setMessage("Do you want to add item to shopping cart?");

                        View layout = LayoutInflater.from(StoreCatalogueActivity.this).inflate(R.layout.inflater_item_view,null);

                       final SimpleDraweeView ivItem = (SimpleDraweeView) layout.findViewById(R.id.ivItem);



                        storageRef.child("store/store_item_image/"+model.getUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Got the download URL for 'users/me/profile.png'


                                ivItem.setImageURI(uri);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });

                        TextView tvItemName = ButterKnife.findById(layout,R.id.tvItem);
                        tvItemName.setText("Name: " +model.getName());
                        TextView tvItemPrice = ButterKnife.findById(layout,R.id.tvItemPrice);
                        tvItemPrice.setText("Price: R"+model.getPrice());


                        builder.setView(layout);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                if(isUpdate == false) {
                                    StoreCatalogueActivity.cart.add(model);
                                }else
                                {

                                    StoreCatalogueActivity.cart =store.getItems();
                                    StoreCatalogueActivity.cart.set(position,model);
                                }

                            }
                        })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        dialog.dismiss();
                                    }
                                });

                        Dialog dialog = builder.create();
                        dialog.show();
                    }


                });



            }
        };


        rvCatalogue.setAdapter(adapter);





    }


    public void onNext(View view)
    {

            store.setItems(cart);
            Intent intent = new Intent(StoreCatalogueActivity.this,ShoppingCartActivity.class);
           intent.putExtra(Constants.STORE,store);
           intent.putExtra(Constants.KEY,key);
            startActivity(intent);


    }


    private static class ItemViewHolder extends RecyclerView.ViewHolder
    {


        private View mView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }


        public CardView getCardView() {



            CardView cdItem = (CardView) mView.findViewById(R.id.cdItem);
            return cdItem;
        }

        public void setImage(String url)
        {

            SimpleDraweeView ivItem = (SimpleDraweeView) mView.findViewById(R.id.ivItem);
            Uri uri = Uri.parse(url);
            ivItem.setImageURI(uri);

        }

        public void  setItemName(String name)
        {
            TextView tvItem = (TextView)mView.findViewById(R.id.tvItem);
            tvItem.setText(name);
        }


        public void setItemPrice(double price)
        {

            TextView   tvItemPrice = (TextView) mView.findViewById(R.id.tvItemPrice);
            tvItemPrice.setText("R"+price);
        }
    }
}
