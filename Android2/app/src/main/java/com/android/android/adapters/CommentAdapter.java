package com.android.android.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.android.R;
import com.android.android.database.CommentContract;
import com.android.android.database.CRUD;
import com.android.android.model.Comment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CommentAdapter extends ArrayAdapter<Comment> {
    ArrayList<Comment> mComm = new ArrayList<>();
    Context mContext = null;
    private int user;
    private Comment comment;
    private TextView textView;
    private CRUD CRUD;

    public CommentAdapter(Context context, ArrayList<Comment> comments){
        super(context,0,comments);
        SharedPreferences pref = context.getSharedPreferences("mPref", 0);
        int idUser = pref.getInt("id", -1);
        user=idUser;
        this.mContext= context;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup){
        comment = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.comment_layout,viewGroup,false);
        }
        final View currentView = view;
        TextView author_view = view.findViewById(R.id.authorComm);
        TextView desc_view = view.findViewById(R.id.desc_comm);
        TextView like_view = view.findViewById(R.id.likeComm);
        TextView dislike_view = view.findViewById(R.id.dislkeComm);
        TextView date_view=view.findViewById(R.id.date_comm_);
        TextView title_view = view.findViewById(R.id.title_comm);
        title_view.setText(comment.getTitle());
        author_view.setText(comment.getAuthor().getName());
        desc_view.setText(comment.getDescription());
        String sl=String.valueOf(comment.getLikes());
        like_view.setText(sl);
        String dis=String.valueOf(comment.getDislikes());
        dislike_view.setText(dis);
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String sDate= dateFormat.format(comment.getDate());
        date_view.setText(sDate);

        ImageButton button = (ImageButton) view.findViewById(R.id.likeBtnComm);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(user != comment.getAuthor().getId()){


                comment.setLikes(comment.getLikes()+1);
                CRUD = new CRUD();
                CRUD.updateComment(comment, (Activity) mContext,null,null);
                update(currentView,comment);

                }else{
                    Toast.makeText(mContext, "You cant like your comment!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ImageButton button2 = (ImageButton) view.findViewById(R.id.dislikeBtnComm);
        button2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(user != comment.getAuthor().getId()){
                comment.setDislikes(comment.getDislikes()+1);
                CRUD = new CRUD();
                CRUD.updateComment(comment,(Activity) mContext,null,null);
                    update(currentView,comment);
                }else{
                    Toast.makeText(mContext, "You cant dislike your comment!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ImageButton deleteComm=view.findViewById(R.id.deleteComm);
        deleteComm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user == comment.getAuthor().getId()){
                    Uri uri = Uri.withAppendedPath(CommentContract.CommentEntry.CONTENT_URI, String.valueOf(comment.getId()));
                    mContext.getContentResolver().delete(uri, null, null);


                }else{
                    Toast.makeText(mContext, "You can delete just your comment, noob!", Toast.LENGTH_SHORT).show();
                }

            }
        });


         return view;
    }

    private void update(View view,Comment comment){
        TextView likes = view.findViewById(R.id.likeComm);
        TextView dislikes = view.findViewById(R.id.dislkeComm);
        likes.setText(String.valueOf(comment.getLikes()));
        dislikes.setText(String.valueOf(comment.getDislikes()));
    }
}
