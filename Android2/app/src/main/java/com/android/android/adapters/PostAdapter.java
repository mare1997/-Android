package com.android.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.android.R;
import com.android.android.model.Post;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class PostAdapter extends ArrayAdapter<Post> {

    ArrayList<Post> mPosts = new ArrayList<>();
    Context mContext = null;

    public PostAdapter(Context context, ArrayList<Post> posts){
        super(context,0,posts);
    }



    @Override
    public View getView(int position, View view, ViewGroup viewGroup){
        Post post = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.post_layout_list_items,viewGroup,false);
        }

        TextView date_view = view.findViewById(R.id.date_post_list);
        TextView title_view = view.findViewById(R.id.title_post_list);

        String dateTime = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        if(post.getDate() != null){
            dateTime = dateFormat.format(post.getDate());
        }else{
            dateTime = "15.11.2014";
        }



        date_view.setText(dateTime);
        title_view.setText(post.getTitle());


        return view;
    }
}
