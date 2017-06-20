package com.skeleton.fragment;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.skeleton.R;
import com.skeleton.activity.LoginSignupActivity;
import com.skeleton.activity.MainActivity;
import com.skeleton.util.customview.MaterialEditText;
import com.skeleton.util.customview.ProgressDialog;
import com.skeleton.util.imagepicker.ImageChooser;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupFragment extends BaseFragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String TAG = SignupFragment.class.getName();
    private MaterialEditText etName, etPhone, etEmail, etDob, etPassword, etCpassword;
    private Spinner spOrientation;
    private CircleImageView ciProfileImage;
    private RadioGroup rgSexRadioGroup;
    private RadioButton rbMale, rbFemale;
    private CheckBox checkTerms;
    private Button btSignup;
    private boolean isImageSet = false;
    private File imagefile;
    private int mGender;
    private String mDateOfBirth;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private ImageChooser imageChooser;
    private Uri uriImage;
    private Uri newUri;
    private static int PICK_IMAGE_REQUEST = 1;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://book-bay-55a28.appspot.com/booksImages");


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_up, null);
        init(v);
        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();


            }
        });
        return v;
    }

    private void init(final View v) {

        etName = (MaterialEditText) v.findViewById(R.id.et_name);
        etPhone = (MaterialEditText) v.findViewById(R.id.et_phone);
        etEmail = (MaterialEditText) v.findViewById(R.id.et_email);
        etDob = (MaterialEditText) v.findViewById(R.id.et_dob);
        etPassword = (MaterialEditText) v.findViewById(R.id.et_password);
        etCpassword = (MaterialEditText) v.findViewById(R.id.et_c_password);
        spOrientation = (Spinner) v.findViewById(R.id.sp_orientation);
        ciProfileImage = (CircleImageView) v.findViewById(R.id.profile_image);
        rgSexRadioGroup = (RadioGroup) v.findViewById(R.id.sex_group);
        rbMale = (RadioButton) v.findViewById(R.id.radio_male);
        rbFemale = (RadioButton) v.findViewById(R.id.radio_female);
        checkTerms = (CheckBox) v.findViewById(R.id.cb_terms);
        btSignup = (Button) v.findViewById(R.id.bt_sign_up);
        rgSexRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final RadioGroup group, @IdRes final int checkedId) {
                if (checkedId == R.id.radio_male) {
                    mGender = 0;
                } else {
                    mGender = 1;
                }
            }
        });
        ciProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        auth = FirebaseAuth.getInstance();
        Log.d("gender", Integer.toString(mGender));
    }


    /**
     * create user
     */
    public void createUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        //Progress Bar Visibility on
        ProgressDialog.showProgressDialog(getActivity(), getString(R.string.loading));
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Progress Bar Visibility GONE
                        ProgressDialog.dismissProgressDialog();
                        //Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Authentication failed." + task.getException(),
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "Signup Successful!", Toast.LENGTH_LONG).show();
                            pushUserCredentials();
                            Activity activity = getActivity();
                            if (activity instanceof LoginSignupActivity) {
                                LoginSignupActivity profileActivity = (LoginSignupActivity) activity;
                                ViewPager viewPager = profileActivity.getViewPager();
                                if (viewPager != null) {
                                    viewPager.setCurrentItem(1);
                                }
                            }

                        }
                    }
                });

    }

    private void pushUserCredentials() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("User").child("user_credentials").child("name").setValue(etName.getText().toString());
        mDatabase.child("User").child("user_credentials").child("phone").setValue(etPhone.getText().toString());
        mDatabase.child("User").child("user_credentials").child("sex").setValue(mGender);
        mDatabase.child("User").child("user_credentials").child("dob").setValue(etDob.getText().toString());
        uploadImage();
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
                            .into(ciProfileImage);
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

    /**
     * upload image method
     */
    private void uploadImage() {
        StorageReference childRef = storageRef.child(etPhone.toString());

        //uploading the image
        UploadTask uploadTask = childRef.putFile(uriImage);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getActivity(), "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
            }
        });
    }

}

