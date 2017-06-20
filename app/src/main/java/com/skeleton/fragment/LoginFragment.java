package com.skeleton.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.skeleton.R;
import com.skeleton.activity.MainActivity;
import com.skeleton.util.customview.ProgressDialog;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * MainActivity simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private EditText etUsernme, etPassword;
    private CheckBox checkRememberMe;
    private Button btSignIn, btSignInFb;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, null);
        init(v);
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticateUser();
            }
        });

        return v;
    }

    private void init(final View v) {
        etUsernme = (EditText) v.findViewById(R.id.et_email_phone);
        etPassword = (EditText) v.findViewById(R.id.et_password);
        checkRememberMe = (CheckBox) v.findViewById(R.id.ch_remember);
        btSignIn = (Button) v.findViewById(R.id.bt_sign_in);
        auth = FirebaseAuth.getInstance();
    }

    //Authenticating user
    private void authenticateUser() {
        String mEmail = etUsernme.getText().toString().trim();
        String mPassword = etPassword.getText().toString().trim();
        //Show Progress Bar
        ProgressDialog.showProgressDialog(getActivity(), getString(R.string.loading));
        //authenticate user
        Log.d(TAG, mEmail + mPassword);
        auth.signInWithEmailAndPassword(mEmail, mPassword)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        ProgressDialog.dismissProgressDialog();

                        if (!task.isSuccessful()) {

                            Toast.makeText(getActivity(), "Wrong Username Password", Toast.LENGTH_LONG).show();

                        } else {
                            startActivity(new Intent(getActivity(), MainActivity.class));

                        }
                    }
                });

    }

}
