package com.skeleton.navifragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.skeleton.R;
import com.skeleton.adapter.BooksAdapter;
import com.skeleton.model.BookInfo;
import com.skeleton.util.customview.ProgressDialog;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private BooksAdapter adapter;
    private ArrayList<BookInfo> mBooksArrayList;
    private String mTitle, mAuthor, mCategory, mEdition, mPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                      Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, null);
        init(v);
        getBooks();
        return v;
    }

    private void init(View v) {
        //init
        mBooksArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);


    }

    private List<BookInfo> getDataRecyclerView() {
        BookInfo book = new BookInfo(mTitle, mAuthor, getCategory(mCategory), mEdition, mPrice);
        mBooksArrayList.add(book);
        return mBooksArrayList;

    }

    private void getBooks() {
        ProgressDialog.showProgressDialog(getActivity(), "Getting Books");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("books");
        ref.addChildEventListener(new ChildEventListener() {
            @Override

            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mTitle = String.valueOf(dataSnapshot.child("book_title").getValue());
                mAuthor = String.valueOf(dataSnapshot.child("book_author").getValue());
                mCategory = String.valueOf(dataSnapshot.child("book_category").getValue());
                mEdition = String.valueOf(dataSnapshot.child("book_edition").getValue());
                mPrice = String.valueOf(dataSnapshot.child("book_price").getValue());
                adapter = new BooksAdapter(getActivity(), getDataRecyclerView());
                 /*set adapter to the recyclerView*/
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                ProgressDialog.dismissProgressDialog();


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private String getCategory(String string){
        if (string.matches("1")){
            return "Physics";
        } if (string.matches("2")){
            return "Chemistry";
        } if (string.matches("3")){
            return "Maths";
        }if (string.matches("4")) {
            return "Biology";
        }
        return "null";
    }

}
