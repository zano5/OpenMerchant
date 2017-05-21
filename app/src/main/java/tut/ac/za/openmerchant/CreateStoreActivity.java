package tut.ac.za.openmerchant;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.security.SecureRandom;

import butterknife.BindView;
import butterknife.ButterKnife;
import tut.ac.za.openmerchant.Classes.Constants;
import tut.ac.za.openmerchant.Classes.Store;

public class CreateStoreActivity extends AppCompatActivity {

    @BindView(R.id.etStoreName) EditText etStoreName;
    @BindView(R.id.spDescription) Spinner spDescription;
    @BindView(R.id.btnNext) Button btnNext;
    @BindView(R.id.ivStore) ImageView ivStore;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1002;
    private Store store;
    private String description;
    private String[] descriptionArray;
    private int RESULT_LOAD_IMAGE = 1001;
    private FirebaseAuth mAuth;
    private String url;
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private String randomStore;
    private String randomItem;
    private StorageReference mStorageRef;
    static SecureRandom rnd = new SecureRandom();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_store);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);

        descriptionArray = getResources().getStringArray(R.array.store_description);
        ArrayAdapter<String> descAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,descriptionArray);
        spDescription.setAdapter(descAdapter);

        spDescription.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                description = descriptionArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void onImage(View view)
    {
        if (Build.VERSION.SDK_INT >= 23){
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(CreateStoreActivity.this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(CreateStoreActivity.this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(CreateStoreActivity.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }else{
                ActivityCompat.requestPermissions(CreateStoreActivity.this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }

        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);


    }

    public void onNext(View view)
    {
        if("".equals(etStoreName.getText().toString()))
        {
            Toast.makeText(CreateStoreActivity.this,"Enter Store Name Field",Toast.LENGTH_SHORT).show();
        }else
        {

            store = new Store();
            store.setUserID(mAuth.getCurrentUser().getUid());
            store.setName(etStoreName.getText().toString());
            store.setDescription(description);



            randomStore =  randomString(10)+".jpg";
            Uri file = Uri.fromFile(new File(url));
            StorageReference riversRef = mStorageRef.child("store/store_image/"+randomStore);

            riversRef.putFile(file)
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
                                          }


                    );

            store.setStoreUrl(randomStore);


            Intent intent = new Intent(CreateStoreActivity.this,InsertItemsActivity.class);
            intent.putExtra(Constants.STORE,store);
            startActivity(intent);
            finish();


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
            ivStore.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            url = picturePath;



        }
    }


    String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }
}
