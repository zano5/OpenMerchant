package tut.ac.za.openmerchant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import tut.ac.za.openmerchant.Classes.Comment;
import tut.ac.za.openmerchant.Classes.Feedback;

public class CommentActivity extends AppCompatActivity {
    private EditText etComment;
    private Button btnSubmit;
    private DatabaseReference db;
    private Feedback feedback;
    private FirebaseAuth mAth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        mAth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        etComment = (EditText) findViewById(R.id.etComment);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);


    }


    public void onSubmit(View view)
    {
        Comment comment = new Comment();

         if("".equals(etComment.getText().toString()))
         {

            Toast.makeText(CommentActivity.this,"Enter Comment Field",Toast.LENGTH_SHORT).show();

        }else
        {

            comment.setComment(etComment.getText().toString());
            comment.setUserID(mAth.getCurrentUser().getUid());
            comment.setEmail(mAth.getCurrentUser().getEmail());
            comment.setDate(new Date().toString());


            db.child("Comment").child("Comments").push().setValue(comment);

            Toast.makeText(CommentActivity.this,"Comment was published",Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(CommentActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }
}
