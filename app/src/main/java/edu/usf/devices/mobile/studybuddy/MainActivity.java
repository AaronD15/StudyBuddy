package edu.usf.devices.mobile.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity  {

    FirebaseAuth.AuthStateListener mAuthListener;

    final String TAG = "MainActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        //tabLayout.addTab(tabLayout.newTab().setText("Search"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.group));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.mipmap.search));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.scheduler));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.profile));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PageAdapter (getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.options_menu, menu);


        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.settings:
                settings();
                break;
            case R.id.sign_out:
                signOut();
                break;
            case R.id.update_acc:
                //UpdateProfile();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void startIntent(Intent intentToStart){
        intentToStart.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intentToStart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentToStart);
    }
    public void settings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startIntent(intent);
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        LogInScreen();
    }

    private void LogInScreen() {
        Intent intent = new Intent(this, EmailPasswordActivity.class);
        startIntent(intent);
    }

    public void UpdateProfile() {
        Intent intent = new Intent(this, UpdateProfile.class);
        startIntent(intent);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getApplicationContext(),"Start1",Toast.LENGTH_SHORT).show();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("FragmentProfile");
        if (fragment!=null){
            fragment.onActivityResult(requestCode, resultCode, data);
            Toast.makeText(getApplicationContext(),"FRAGMENT IS NOT NULL",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(),"FRAGMENT IS NULL",Toast.LENGTH_SHORT).show();
        }
    }


}
