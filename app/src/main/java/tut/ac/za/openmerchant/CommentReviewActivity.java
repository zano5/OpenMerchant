package tut.ac.za.openmerchant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import tut.ac.za.openmerchant.Classes.Comment;

public class CommentReviewActivity extends AppCompatActivity {


    private FirebaseRecyclerAdapter<Comment,CommentViewHolder> adapter;
    private DatabaseReference db;
    private  RecyclerView rvComment;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_review);

        db = FirebaseDatabase.getInstance().getReference().child("Comment").child("Comments");
        rvComment = (RecyclerView) findViewById(R.id.rvComments);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComment.setLayoutManager(linearLayoutManager);


        adapter = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(Comment.class,R.layout.comment_view,CommentViewHolder.class,db) {
            @Override
            protected void populateViewHolder(CommentViewHolder viewHolder, Comment model, int position) {


                viewHolder.setDate(model.getDate());
                viewHolder.setComment(model.getComment());
                viewHolder.setEmail(model.getEmail());
            }
        };


        rvComment.setAdapter(adapter);
    }


    private static  class CommentViewHolder extends RecyclerView.ViewHolder
    {


        private View mView;

        public CommentViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }


        public void setDate(String date)
        {

            TextView tvDate = (TextView) mView.findViewById(R.id.tvDate);
            tvDate.setText(DateFormat.format("MM/dd/yyyy hh:mm:ss a", Date.parse(date)));

        }


        public void setComment(String comment)
        {

            TextView tvComment = (TextView) mView.findViewById(R.id.tvComment);
            tvComment.setText(comment);

        }


        public void setEmail(String email)
        {

            TextView tvEmail = (TextView) mView.findViewById(R.id.tvEmail);
            tvEmail.setText(email);

        }
    }




}
