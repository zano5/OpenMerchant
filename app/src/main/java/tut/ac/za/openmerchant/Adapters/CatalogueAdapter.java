package tut.ac.za.openmerchant.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.NumberFormat;
import java.util.List;

import butterknife.ButterKnife;
import tut.ac.za.openmerchant.Classes.Constants;
import tut.ac.za.openmerchant.Classes.Item;
import tut.ac.za.openmerchant.Classes.Store;
import tut.ac.za.openmerchant.R;
import tut.ac.za.openmerchant.StoreCatalogueActivity;

/**
 * Created by ProJava on 2/17/2017.
 */

public class CatalogueAdapter extends RecyclerView.Adapter<CatalogueAdapter.CatalogueViewHolder> {


    private Context context;
    private List<Item> itemList;
    private Store store;
    private String key;
    private boolean isUpdate;
    StorageReference storageRef;

    public CatalogueAdapter(Context context, List<Item> itemList)
    {
        this.context = context;
        this.itemList = itemList;
    }


    public CatalogueAdapter(Context context,List<Item> itemList,Store store,String key)
    {

        this.context = context;
        this.itemList = itemList;
        this.store = store;
        this.key = key;
    }
    @Override
    public CatalogueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,null);

        CatalogueViewHolder cvh = new CatalogueViewHolder(view);

        return cvh;
    }

    @Override
    public void onBindViewHolder(final CatalogueViewHolder holder, final int position) {

        final FirebaseStorage storage = FirebaseStorage.getInstance();

        storageRef = storage.getReference();
        Item item = itemList.get(position);


        holder.tvItemPrice.setText("R"+ NumberFormat.getInstance().format(item.getPrice()));
        holder.tvItemName.setText(item.getName());



        storageRef.child("store/store_item_image/"+item.getUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'


                holder.draweeView.setImageURI(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });



        holder.cdItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence[] options = {"Update","Delete"};
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which)
                        {
                            case 0:


                                isUpdate = true;
                                Intent intent = new Intent(context,StoreCatalogueActivity.class);
                                intent.putExtra(Constants.IS_UPDATE,isUpdate);
                                intent.putExtra(Constants.STORE,store);
                                intent.putExtra(Constants.KEY,key);
                                intent.putExtra(Constants.POSITION,position);
                                context.startActivity(intent);


                                break;

                            case 1:

                                itemList.remove(position);
                                StoreCatalogueActivity.cart = itemList;
                                notifyDataSetChanged();

                                break;
                        }

                    }
                });

                Dialog dialog = builder.create();
                dialog.show();



            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class CatalogueViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemName;
        private TextView tvItemPrice;
        private SimpleDraweeView draweeView;
        private View mView;
        private CardView cdItem;

        public CatalogueViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            draweeView = ButterKnife.findById(mView,R.id.ivItem);
            tvItemName = ButterKnife.findById(mView,R.id.tvItem);
            tvItemPrice = ButterKnife.findById(mView,R.id.tvItemPrice);
            cdItem = ButterKnife.findById(mView,R.id.cdItem);
        }
    }
}
