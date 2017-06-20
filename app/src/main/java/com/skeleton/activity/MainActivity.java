package com.skeleton.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.skeleton.R;
import com.skeleton.navifragments.AddBookFragment;
import com.skeleton.navifragments.BidFraagment;

import com.skeleton.navifragments.HomeFragment;
import com.skeleton.navifragments.MyBooksFragment;
import com.skeleton.navifragments.MyEnquiriesFragment;

/**
 * MainActivity
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout dlDrawer;
    private TextView tvHome, tvAddBook, tvMyBooks, tvMyEnquiries, tvBid, tvLogout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar topToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        topToolBar.setTitle("");
        topToolBar.setNavigationIcon(R.drawable.icon_side_menu);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        dlDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        topToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dlDrawer.openDrawer(Gravity.LEFT);
            }
        });
        init();


    }

    private void init() {
        tvHome = (TextView) findViewById(R.id.item_home);
        tvAddBook = (TextView) findViewById(R.id.item_add_book);
        tvMyBooks = (TextView) findViewById(R.id.item_my_book);
        tvMyEnquiries = (TextView) findViewById(R.id.item_my_enquiries);
        tvBid = (TextView) findViewById(R.id.item_bid);
        tvLogout = (TextView) findViewById(R.id.item_logout);
        tvHome.setOnClickListener(this);
        tvAddBook.setOnClickListener(this);
        tvMyBooks.setOnClickListener(this);
        tvMyEnquiries.setOnClickListener(this);
        tvBid.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
        setInitialFragment();
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_home:

                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, homeFragment).commit();
                dlDrawer.closeDrawer(Gravity.LEFT);
                break;
            case R.id.item_add_book:

                AddBookFragment addBookFragment = new AddBookFragment();
                addBookFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, addBookFragment).commit();
                dlDrawer.closeDrawer(Gravity.LEFT);
                break;
            case R.id.item_my_book:
                MyBooksFragment myBooksFragment = new MyBooksFragment();
                myBooksFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, myBooksFragment).commit();
                dlDrawer.closeDrawer(Gravity.LEFT);
                break;
            case R.id.item_my_enquiries:
                MyEnquiriesFragment myEnquiriesFragment = new MyEnquiriesFragment();
                myEnquiriesFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, myEnquiriesFragment).commit();
                dlDrawer.closeDrawer(Gravity.LEFT);
                break;
            case R.id.item_bid:
                BidFraagment bidFraagment = new BidFraagment();
                bidFraagment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, bidFraagment).commit();
                dlDrawer.closeDrawer(Gravity.LEFT);
                break;
            case R.id.item_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginSignupActivity.class));
                dlDrawer.openDrawer(Gravity.LEFT);
                break;


            default:
                break;


        }

    }


    /**
     * initiated initial fragment for activity
     */
    private void setInitialFragment() {
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, homeFragment).commit();
    }
}
