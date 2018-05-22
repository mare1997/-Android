package com.android.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.android.R;
import com.android.android.model.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CommentAdapter extends ArrayAdapter<Comment> {
    ArrayList<Comment> mComm = new ArrayList<>();
    Context mContext = null;

    public CommentAdapter(Context context, ArrayList<Comment> comments){
        super(context,0,comments);
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup){
        Comment comment = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.comment_layout,viewGroup,false);
        }

        TextView author_view = view.findViewById(R.id.authorComm);
        TextView desc_view = view.findViewById(R.id.desc_comm);
        TextView like_view = view.findViewById(R.id.likeComm);
        TextView dislike_view = view.findViewById(R.id.dislkeComm);
        author_view.setText(comment.getAuthor().getName());
        desc_view.setText(comment.getDescription());
        String sl=String.valueOf(comment.getLikes());
        like_view.setText(sl);
        String dis=String.valueOf(comment.getDislikes());
        dislike_view.setText(dis);
         return view;
    }
}
