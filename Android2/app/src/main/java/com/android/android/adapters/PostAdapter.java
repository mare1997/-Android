package com.android.android.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.android.R;
import com.android.android.model.Post;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


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

        String newDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(post.getDate());

        date_view.setText(newDate);
        title_view.setText(post.getTitle());


        return view;
    }
}
