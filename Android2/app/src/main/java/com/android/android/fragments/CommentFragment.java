package com.android.android.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.android.PostsActivity;
import com.android.android.R;
import com.android.android.ReadPostActivity;
import com.android.android.adapters.CommentAdapter;
import com.android.android.database.HelperDatabaseRead;
import com.android.android.model.Comment;
import com.android.android.model.Post;



import java.util.ArrayList;


public class CommentFragment extends Fragment {


    private HelperDatabaseRead helperDatabaseRead;
    private ArrayList<Post> posts = new ArrayList<Post>();
    private Post post=null;
    private int id;
    private int stanje = 0;
    private Comment comment;
    private TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_comment, container, false);
        Intent myIntent = getActivity().getIntent();
        id= myIntent.getIntExtra("id",-1);
        helperDatabaseRead = new HelperDatabaseRead();
        posts=helperDatabaseRead.loadPostsFromDatabase(getActivity());
        if(id != -1){
            for(Post pp: posts){
                if(pp.getId() == id){
                    post = pp;
                }
            }
        }
       CommentAdapter commentAdapter=new CommentAdapter(getActivity(),post.getComments());
        ListView listView = view.findViewById(R.id.readpost_list_view_comment);
        listView.setAdapter(commentAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Comment clickedObj = (Comment) adapterView.getItemAtPosition(i);
                comment=clickedObj;
            }
        });

        return view;


    }
    public void likesAndDislikes(final View view){

            ImageButton button = (ImageButton) view.findViewById(R.id.likeBtnComm);
            button.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    switch (stanje){
                        case 0:
                            comment.setLikes(comment.getLikes()+1);
                            stanje=1;
                            helperDatabaseRead = new HelperDatabaseRead();

                            helperDatabaseRead.updateComment(comment,getActivity(),null,null);
                            textView=(TextView)view.findViewById(R.id.likeComm);
                            textView.setText(String.valueOf(comment.getLikes()));
                            break;
                        case 1:
                            comment.setLikes(comment.getLikes()-1);
                            stanje=0;
                            helperDatabaseRead = new HelperDatabaseRead();
                            helperDatabaseRead.updateComment(comment,getActivity(),null,null);
                            textView=(TextView)view.findViewById(R.id.likeComm);
                            textView.setText(String.valueOf(comment.getLikes()));
                            break;
                        case -1:
                            comment.setLikes(comment.getLikes()+1);
                            comment.setDislikes(comment.getDislikes()-1);
                            stanje=1;
                            helperDatabaseRead = new HelperDatabaseRead();
                            helperDatabaseRead.updateComment(comment,getActivity(),null,null);
                            textView=(TextView)view.findViewById(R.id.likeComm);
                            textView.setText(String.valueOf(comment.getLikes()));
                            textView=(TextView)view.findViewById(R.id.dislkeComm);
                            textView.setText(String.valueOf(comment.getDislikes()));
                            break;
                    }

                }
            });
            ImageButton button2 = (ImageButton) view.findViewById(R.id.dislikeBtnComm);
            button2.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    switch (stanje){
                        case 0:
                            comment.setDislikes(comment.getDislikes()+1);
                            stanje=-1;
                            helperDatabaseRead = new HelperDatabaseRead();
                            helperDatabaseRead.updateComment(comment,getActivity(),null,null);

                            textView=(TextView)view.findViewById(R.id.dislkeComm);
                            textView.setText(String.valueOf(comment.getDislikes()));
                            break;
                        case 1:
                            comment.setLikes(comment.getLikes()-1);
                            comment.setDislikes(comment.getDislikes()+1);
                            stanje=-1;
                            helperDatabaseRead = new HelperDatabaseRead();
                            helperDatabaseRead.updateComment(comment,getActivity(),null,null);
                            textView=(TextView)view.findViewById(R.id.likeComm);
                            textView.setText(String.valueOf(comment.getLikes()));
                            textView=(TextView)view.findViewById(R.id.dislkeComm);
                            textView.setText(String.valueOf(comment.getDislikes()));
                            break;
                        case -1:

                            comment.setDislikes(comment.getDislikes()-1);
                            stanje=0;
                            helperDatabaseRead = new HelperDatabaseRead();
                            helperDatabaseRead.updateComment(comment,getActivity(),null,null);

                            textView=(TextView)view.findViewById(R.id.dislkeComm);
                            textView.setText(String.valueOf(comment.getDislikes()));
                            break;
                    }

                }
            });


    }


}
