package com.skeleton.navifragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.skeleton.R;
import com.skeleton.fragment.BaseFragment;
import com.skeleton.util.customview.MaterialEditText;
import com.skeleton.util.customview.ProgressDialog;
import com.skeleton.util.imagepicker.ImageChooser;

import java.io.File;
import java.util.List;


public class AddBookFragment extends BaseFragment implements View.OnClickListener {
    private MaterialEditText etTitle, etAuthor, etEdition, etPrice;
    private RadioButton rbPhysics, rbChemistry, rbMaths, rbBiology;
    private Button btAddBook;
    private DatabaseReference mDatabase;
    private String mUser;
    private RadioGroup rgCategoryGroup;
    private ImageView ivBookImage;
    private ImageChooser imageChooser;
    private File imagefile;
    private Uri uriImage;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://book-bay-55a28.appspot.com/booksImages");
    private Boolean isImageSet = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_book, null);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mUser = user.getEmail();
        init(v);
        return v;
    }

    private void init(View v) {
        etTitle = (MaterialEditText) v.findViewById(R.id.et_title);
        etAuthor = (MaterialEditText) v.findViewById(R.id.et_author);
        etEdition = (MaterialEditText) v.findViewById(R.id.et_edition);
        etPrice = (MaterialEditText) v.findViewById(R.id.et_price);
        rbPhysics = (RadioButton) v.findViewById(R.id.radio_physics);
        rbChemistry = (RadioButton) v.findViewById(R.id.radio_chemistry);
        rbMaths = (RadioButton) v.findViewById(R.id.radio_maths);
        rbBiology = (RadioButton) v.findViewById(R.id.radio_biology);
        btAddBook = (Button) v.findViewById(R.id.bt_add_book);
        rgCategoryGroup = (RadioGroup) v.findViewById(R.id.category_group);
        ivBookImage = (ImageView) v.findViewById(R.id.book_image);
        ivBookImage.setOnClickListener(this);
        btAddBook.setOnClickListener(this);


    }

    private void addBook() {


        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mRefBooks;
        mRefBooks = mDatabase.child("books").push();
        mRefBooks.child("book_title").setValue(etTitle.getText().toString().trim());
        mRefBooks.child("book_author").setValue(etAuthor.getText().toString().trim());
        mRefBooks.child("book_edition").setValue(etEdition.getText().toString().trim());
        mRefBooks.child("book_price").setValue(etPrice.getText().toString().trim());
        mRefBooks.child("book_category").setValue(getCategory());
        mRefBooks.child("user_id").setValue(mUser);
        uploadImage();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add_book:

                if (validateData()) {
                    addBook();
                }
                break;
            case R.id.book_image:
                chooseImage();
                break;
            default:
                break;
        }
    }

    private boolean validateData() {
        if (etTitle.getText().toString().isEmpty()) {
            etTitle.setError("Field can not beempty");
            etTitle.requestFocus();
            return false;
        } else if (etAuthor.getText().toString().isEmpty()) {
            etAuthor.setError("Field can not be empty");
            etAuthor.requestFocus();
            return false;
        } else if (etEdition.getText().toString().isEmpty()) {
            etEdition.setError("Field can not beempty");
            etEdition.requestFocus();
            return false;
        } else if (etPrice.getText().toString().isEmpty()) {
            etPrice.setError("Field can not beempty");
            etPrice.requestFocus();
            return false;
        } else if (getCategory() == 0) {
            Toast.makeText(getActivity(), "Select a category", Toast.LENGTH_LONG).show();
            return false;
        } else if (!isImageSet) {
            Toast.makeText(getActivity(), "Select an Image", Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private int getCategory() {
        if (rbPhysics.isChecked())
            return 1;
        if (rbChemistry.isChecked())
            return 2;
        if (rbMaths.isChecked())
            return 3;
        if (rbBiology.isChecked())
            return 4;
        else
            return 0;
    }

    /**
     * Image chooser
     */
    private void chooseImage() {

        imageChooser = new ImageChooser.Builder(this).setCropEnabled(false).build();
        imageChooser.selectImage(new ImageChooser.OnImageSelectListener() {
            @Override
            public void loadImage(final List<ChosenImage> list) {

                uriImage = Uri.parse(list.get(0).getQueryUri());
                imagefile = new File(String.valueOf(uriImage));
                if (uriImage != null) {
                    //imageView.setImageURI(uriImage);
                    Glide.with(getActivity())
                            .load(uriImage)
                            .centerCrop()
                            .into(ivBookImage);
                    isImageSet = true;
                }
            }

            @Override
            public void croppedImage(final File mCroppedImage) {

            }
        });

    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        imageChooser.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);


    }

    private void uploadImage() {
        ProgressDialog.showProgressDialog(getActivity());
        StorageReference childRef = storageRef.child(etTitle.getText().toString());

        //uploading the image
        UploadTask uploadTask = childRef.putFile(uriImage);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_SHORT).show();
                clearCredentials(etAuthor, etEdition, etPrice, etTitle);
                rgCategoryGroup.clearCheck();
                ivBookImage.setImageResource(R.drawable.sign_up_screen_image);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getActivity(), "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * clears credentials aftedr success
     *
     * @param editTexts all edit texts to be cleared
     */
    private void clearCredentials(MaterialEditText... editTexts) {
        for (MaterialEditText editText : editTexts) {
            editText.setText("");

        }


    }


}
