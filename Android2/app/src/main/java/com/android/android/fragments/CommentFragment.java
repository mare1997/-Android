package com.android.android.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.android.R;
import com.android.android.adapters.CommentAdapter;
import com.android.android.database.CRUD;
import com.android.android.model.Comment;
import com.android.android.model.Post;
import com.android.android.model.User;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class CommentFragment extends Fragment {


    private CRUD CRUD;
    private ArrayList<Post> posts = new ArrayList<Post>();
    private Post post=null;
    private int id;
    private int stanje = 0;
    private Comment comment;
    private int idUser;
    private TextView textView;
    private EditText textView1;
    private EditText textView2;
    private boolean sortPostbyDate;
    private boolean sortPostbyPppularity;
    private SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_comment, container, false);
        Intent myIntent = getActivity().getIntent();
        id= myIntent.getIntExtra("id",-1);
        CRUD = new CRUD();
        posts= CRUD.loadPostsFromDatabase(getActivity());
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
        textView1=view.findViewById(R.id.titleComm);
        textView2=view.findViewById(R.id.descComm);

        Button button = (Button) view.findViewById(R.id.btnAddComm);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                CRUD = new CRUD();

                String title=textView1.getText().toString();
                String desc=textView2.getText().toString();
                SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("mPref",0);
                idUser = pref.getInt("id",-1);
                User u=null;
                for(User uu: CRUD.loadUsersFromDatabase(getActivity())){
                    if(uu.getId() == idUser){
                        u=uu;
                    }
                }
                Comment c= new Comment(title,desc,u,getDateTime(),id,0,0);
                CRUD.insertComment(c,getActivity());
                FragmentTransaction ft=getFragmentManager().beginTransaction();
                ft.detach(CommentFragment.this).attach(CommentFragment.this).commit();


            }
        });


        return view;


    }

    @Override
    public void onResume() {
        super.onResume();
        sortComments();
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
    public void sortComments(){
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortPostbyDate =sharedPreferences.getBoolean(getString(R.string.sort_comm_date_key),false);
        sortPostbyPppularity=sharedPreferences.getBoolean(getString(R.string.sort_comm_popu_key),false);
        if(sortPostbyDate == true){
            sortByDate();
        }
        if(sortPostbyPppularity == true){
            sortCommByPopularity();
        }


    }
    public void sortCommByPopularity(){

        Collections.sort(post.getComments(), new Comparator< Comment >() {
            @Override
            public int compare(Comment o1, Comment o2) {
                return o2.getLikes() - o1.getLikes();
            }



        });

    }
    public void sortByDate() {
        Collections.sort(post.getComments(), new Comparator<Comment>() {
            @Override
            public int compare(Comment o1, Comment o2) {
                return o2.getDate().compareTo(o1.getDate());
            }

        });
    }



}
