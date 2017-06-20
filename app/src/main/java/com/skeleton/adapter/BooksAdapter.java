package com.skeleton.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.skeleton.R;
import com.skeleton.model.BookInfo;

import java.util.Collections;
import java.util.List;

/**
 * Created by rishucuber on 7/4/17.
 */

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.MyViewHolder> {
    List<BookInfo> data = Collections.emptyList();
    Context context;
    ImageView ivBookImage;

    public BooksAdapter(Context context, List<BookInfo> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final BookInfo current = data.get(position);
        holder.tvTitle.setText(current.getTitle());
        holder.tvAuthor.setText(current.getAuthor());
        holder.tvCatagory.setText(current.getCatagory());
        holder.tvEdition.append(String.valueOf(current.getEdition()));
        holder.tvPrice.append(String.valueOf(current.getPrice()));
        setBookImage(current.getTitle());
        holder.setIsRecyclable(false);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Book Enquired Please check My Enquiries", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvAuthor, tvCatagory, tvEdition, tvPrice;


        public MyViewHolder(final View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.book_title);
            tvAuthor = (TextView) itemView.findViewById(R.id.book_author);
            tvCatagory = (TextView) itemView.findViewById(R.id.book_catagory);
            tvEdition = (TextView) itemView.findViewById(R.id.book_edition);
            tvPrice = (TextView) itemView.findViewById(R.id.book_price);
            ivBookImage = (ImageView) itemView.findViewById(R.id.iv_cover_image);


        }
    }


    public void addStudent(BookInfo student) {
        data.add(student);
        notifyItemInserted(data.size());
    }


    //getting image from fire base storage
    private void setBookImage(String mBookTitle) {
        // Reference to an image file in Firebase Storage
        StorageReference storageReference;
        storageReference = FirebaseStorage.getInstance().getReference("booksImages/" + mBookTitle);

// Load the image using Glide
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(ivBookImage);


    }


}
