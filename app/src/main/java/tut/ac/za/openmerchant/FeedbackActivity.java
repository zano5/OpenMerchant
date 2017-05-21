package tut.ac.za.openmerchant;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tut.ac.za.openmerchant.Classes.Feedback;

public class FeedbackActivity extends AppCompatActivity {

    private EditText etSubject,etComment;
    private Button btnSubmit;
    private DatabaseReference db;
    private Feedback feedback;
    private FirebaseAuth mAth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


        mAth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        etSubject = (EditText) findViewById(R.id.etSubject);
        etComment = (EditText) findViewById(R.id.etComment);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

    }

    public void onSubmit(View view) {
        feedback = new Feedback();

        if ("".equals(etSubject.getText().toString())) {

            Toast.makeText(FeedbackActivity.this, "Enter Subject Field", Toast.LENGTH_SHORT).show();

        } else if ("".equals(etComment.getText().toString())) {

            Toast.makeText(FeedbackActivity.this, "Enter Comment Field", Toast.LENGTH_SHORT).show();

        } else {

            feedback.setComment(etComment.getText().toString());
            feedback.setSubject(etSubject.getText().toString());



            Intent emailIntent = new Intent(Intent.ACTION_SEND);

            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"temathebula@gmail.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, feedback.getSubject());
            emailIntent.putExtra(Intent.EXTRA_TEXT, feedback.getComment());



            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();


        }
    }

}
