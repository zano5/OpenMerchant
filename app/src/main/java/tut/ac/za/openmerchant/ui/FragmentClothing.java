package tut.ac.za.openmerchant.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import butterknife.ButterKnife;
import tut.ac.za.openmerchant.Classes.Constants;
import tut.ac.za.openmerchant.Classes.Store;
import tut.ac.za.openmerchant.R;
import tut.ac.za.openmerchant.StoreCatalogueActivity;

/**
 * Created by ProJava on 2/17/2017.
 */

public class FragmentClothing  extends Fragment {
    private RecyclerView rvStore;
    private DatabaseReference db;
    private List<Store> stores;
    StorageReference storageRef;
    private FirebaseRecyclerAdapter<Store,StoreViewHolder> adapter;
    public FragmentClothing() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final FirebaseStorage storage = FirebaseStorage.getInstance();

        storageRef = storage.getReference();

        final View view = inflater.inflate(R.layout.fragment_store, container, false);

        final TextView tvData = ButterKnife.findById(view,R.id.tvData);

        final ProgressBar progressBar = ButterKnife.findById(view, R.id.progressBar);
        db = FirebaseDatabase.getInstance().getReference().child("Store").child("StoreList");


        Query query = db.orderByChild("description").equalTo("Clothing");

        adapter = new FirebaseRecyclerAdapter<Store, StoreViewHolder>(Store.class,R.layout.store_view,StoreViewHolder.class,query) {
            @Override
            protected void populateViewHolder(final StoreViewHolder viewHolder, final Store model, final int position) {



                storageRef.child("store/store_image/"+model.getStoreUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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
                tvData.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);

                viewHolder.setStoreName(model.getName());

                CardView cdStore = viewHolder.getCardView();

                cdStore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String key = getRef(position).getKey();

                        Intent intent = new Intent(getActivity(), StoreCatalogueActivity.class);
                        intent.putExtra(Constants.KEY,key);
                        intent.putExtra(Constants.STORE,model);
                        startActivity(intent);




                    }
                });
            }
        };




        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.card_spacing_cart);

        rvStore = (RecyclerView) view.findViewById(R.id.rvStore);
        rvStore.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        rvStore.setLayoutManager(new GridLayoutManager(getActivity(),2));

        rvStore.setAdapter(adapter);












        // Inflate the layout for this fragment
        return view;
    }


    private static class StoreViewHolder extends  RecyclerView.ViewHolder{

        private View mView;

        public StoreViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }


        public void setStoreName(String name)
        {

            TextView tvStore = (TextView) mView.findViewById(R.id.tvStore);
            tvStore.setText(name);
        }

        public void setImage(String url)
        {


            Uri uri = Uri.parse(url);
            SimpleDraweeView draweeView = (SimpleDraweeView) mView.findViewById(R.id.ivStore);
            draweeView.setImageURI(uri);

        }


        public CardView getCardView()
        {
            CardView cardView = (CardView) mView.findViewById(R.id.cdStore);

            return  cardView;

        }
    }
}
