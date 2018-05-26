package com.android.android.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.android.PostsActivity;
import com.android.android.R;
import com.android.android.ReadPostActivity;
import com.android.android.database.HelperDatabaseRead;
import com.android.android.model.Comment;
import com.android.android.model.Post;
import com.android.android.model.Tag;
import com.android.android.model.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class PostFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ActionBarDrawerToggle toggle;
    private HelperDatabaseRead helperDatabaseRead;
    private ArrayList<Post> posts = new ArrayList<Post>();
    private Post post=null;
    private TextView textView;
    private int idUser;
    private int id;
    private int stanje = 0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public PostFragment() {
    // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_post, container, false);
        init(view);
        likeAndDislike(view);

        return view;
    }

    public void init(View view){
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
        textView=(TextView)view.findViewById(R.id.ReadAuthor);
        textView.setText(post.getAuthor().getName());
        textView=(TextView)view.findViewById(R.id.titleRead);

        textView.setText(post.getTitle());

        textView=(TextView)view.findViewById(R.id.desc);
        textView.setText(post.getDescription());
        textView=(TextView)view.findViewById(R.id.tag);
        List<Tag> tags=post.getTags();
        String tag="";
        int size= -1;
        if(post.getTags() == null){
            size=0;
        }else{
            size=post.getTags().size();
        }
        for(int i =0 ;i< size;i++){
            tag=tag+ ", "+tags.get(i).getName();
            textView.setText(tag);
        }
        textView=(TextView)view.findViewById(R.id.likes);
        String like= String.valueOf(post.getLikes());
        textView.setText(like);
        textView=(TextView)view.findViewById(R.id.dislikes);
        String disslike= String.valueOf(post.getDislikes());
        textView.setText(disslike);
        textView=(TextView)view.findViewById(R.id.date);
        String date = new SimpleDateFormat("dd.MM.yyyy").format(post.getDate());
        textView.setText(date);
        Button button = (Button) view.findViewById(R.id.btnAddComm);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                btnAddComment(v);

            }
        });
    }

    public void btnAddComment(View view) {
        helperDatabaseRead = new HelperDatabaseRead();
        textView=(TextView)view.findViewById(R.id.titleComm);
        String title=textView.getText().toString();
        textView=(TextView)view.findViewById(R.id.descComm);
        String desc=textView.getText().toString();
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("mPref",0);
        idUser = pref.getInt("id",-1);
        User u=null;
        for(User uu:helperDatabaseRead.loadUsersFromDatabase(getActivity())){
            if(uu.getId() == idUser){
                u=uu;
            }
        }
        Comment c= new Comment(title,desc,u,getDateTime(),id,0,0);
        helperDatabaseRead.insertComment(c,getActivity());
        Intent intent = new Intent(getActivity(),ReadPostActivity.class);
        startActivity(intent);
    }
    private Date getDateTime(){
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        String sDate= dateFormat.format(date);
        return  convertStringToDate(sDate);
    }
    public Date convertStringToDate(String dtStart){

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            Date date = format.parse(dtStart);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void likeAndDislike(final View view){
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("mPref",0);
        idUser = pref.getInt("id",-1);
        User u=null;
        for(User uu:helperDatabaseRead.loadUsersFromDatabase(getActivity())){
            if(uu.getId() == idUser){
                u=uu;
            }
        }
        if(post.getAuthor().getId() != u.getId()){
            ImageButton button = (ImageButton) view.findViewById(R.id.likeBtn);
            button.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    switch (stanje){
                        case 0:
                            post.setLikes(post.getLikes()+1);
                            stanje=1;
                            helperDatabaseRead = new HelperDatabaseRead();
                            post.setLocation("gagaga");
                            helperDatabaseRead.updatePost(post,getActivity(),null,null);
                            textView=(TextView)view.findViewById(R.id.likes);
                            textView.setText(String.valueOf(post.getLikes()));
                            break;
                        case 1:
                            post.setLikes(post.getLikes()-1);
                            stanje=0;
                            helperDatabaseRead = new HelperDatabaseRead();
                            helperDatabaseRead.updatePost(post,getActivity(),null,null);
                            textView=(TextView)view.findViewById(R.id.likes);
                            textView.setText(String.valueOf(post.getLikes()));
                            break;
                        case -1:
                            post.setLikes(post.getLikes()+1);
                            post.setDislikes(post.getDislikes()-1);
                            stanje=1;
                            helperDatabaseRead = new HelperDatabaseRead();
                            helperDatabaseRead.updatePost(post,getActivity(),null,null);
                            textView=(TextView)view.findViewById(R.id.likes);
                            textView.setText(String.valueOf(post.getLikes()));
                            textView=(TextView)view.findViewById(R.id.dislikes);
                            textView.setText(String.valueOf(post.getDislikes()));
                            break;
                    }

                }
            });
            ImageButton button2 = (ImageButton) view.findViewById(R.id.dislikeBtn);
            button2.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    switch (stanje){
                        case 0:
                            post.setDislikes(post.getDislikes()+1);
                            stanje=-1;
                            helperDatabaseRead = new HelperDatabaseRead();
                            helperDatabaseRead.updatePost(post,getActivity(),null,null);

                            textView=(TextView)view.findViewById(R.id.dislikes);
                            textView.setText(String.valueOf(post.getDislikes()));
                            break;
                        case 1:
                            post.setLikes(post.getLikes()-1);
                            post.setDislikes(post.getDislikes()+1);
                            stanje=-1;
                            helperDatabaseRead = new HelperDatabaseRead();
                            helperDatabaseRead.updatePost(post,getActivity(),null,null);
                            textView=(TextView)view.findViewById(R.id.likes);
                            textView.setText(String.valueOf(post.getLikes()));
                            textView=(TextView)view.findViewById(R.id.dislikes);
                            textView.setText(String.valueOf(post.getDislikes()));
                            break;
                        case -1:

                            post.setDislikes(post.getDislikes()-1);
                            stanje=0;
                            helperDatabaseRead = new HelperDatabaseRead();
                            helperDatabaseRead.updatePost(post,getActivity(),null,null);

                            textView=(TextView)view.findViewById(R.id.dislikes);
                            textView.setText(String.valueOf(post.getDislikes()));
                            break;
                    }

                }
            });

        }else{
            Toast.makeText(getActivity(), "You cant like your post, noob!", Toast.LENGTH_SHORT).show();
        }
    }
}
