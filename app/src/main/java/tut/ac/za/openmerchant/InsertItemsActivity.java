package tut.ac.za.openmerchant;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tut.ac.za.openmerchant.Classes.Constants;
import tut.ac.za.openmerchant.Classes.Item;
import tut.ac.za.openmerchant.Classes.Store;

public class InsertItemsActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1002;
    private Store store;
    @BindView(R.id.ivItem) ImageView ivItem;
    @BindView(R.id.btnUpload) Button btnUpload;
    @BindView(R.id.btnNext) Button btnNext;
    @BindView(R.id.etPrice) EditText etPrice;
    @BindView(R.id.etDescription) EditText etDescription;
    @BindView(R.id.etItemName) EditText etItemName;
    private int RESULT_LOAD_IMAGE = 1001;
    private List<Item> itemList;
    private Item item;
    private String url;
    //getResources().getString(R.string.package_text)+ R.drawable.number_one)

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();
    private String randomStore;
    private String randomItem;
    private StorageReference mStorageRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_items);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        ButterKnife.bind(this);
        Intent intent = getIntent();
        store = (Store) intent.getSerializableExtra(Constants.STORE);
        itemList = new ArrayList<Item>();

    }

    public void onImage(View view)
    {

        if (Build.VERSION.SDK_INT >= 23){
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(InsertItemsActivity.this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(InsertItemsActivity.this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(InsertItemsActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }else{
                ActivityCompat.requestPermissions(InsertItemsActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }

        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);


    }

    public void onNext(View view)
    {

        store.setItems(itemList);

        Intent intent = new Intent(InsertItemsActivity.this,AddressActivity.class);
        intent.putExtra(Constants.STORE,store);
        intent.putExtra(Constants.IS_STORE,true);
        startActivity(intent);
    }

    public void onUpload(View view)
    {
        if("".equals(etPrice.getText().toString()))
        {
            Toast.makeText(InsertItemsActivity.this,"Item Price field Should Not Be Empty",Toast.LENGTH_SHORT).show();

        }else if("".equals(etDescription.getText().toString()))
        {

            Toast.makeText(InsertItemsActivity.this,"Item Description Field Should Not Be Empty",Toast.LENGTH_SHORT).show();
        }else if("".equals(etItemName.getText().toString()))
        {
            Toast.makeText(InsertItemsActivity.this,"Item Name Field Should Not Be Empty",Toast.LENGTH_SHORT).show();
        }else
        {
            item = new Item();
            item.setName(etItemName.getText().toString());
            item.setDescription(etDescription.getText().toString());
            item.setPrice(Double.parseDouble(etPrice.getText().toString()));



                randomItem = randomString(10) +".jpg";

             Uri   file = Uri.fromFile(new File(url));
                StorageReference   storageIRef = mStorageRef.child("store/store_item_image/" +randomItem);

                storageIRef.putFile(file)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();





                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                            }
                        });

                item.setUrl(randomItem);




            ivItem.setImageURI(Uri.parse(getResources().getString(R.string.package_text)+ R.drawable.upload));


            etDescription.setText("");
            etPrice.setText("");
            etDescription.setText("");
            url ="";
            itemList.add(item);

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            ivItem.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            url = picturePath;

            Toast.makeText(InsertItemsActivity.this,selectedImage.toString(),Toast.LENGTH_SHORT).show();
        }
    }


    String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }
}
