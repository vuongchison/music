package com.example.music;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity{


    private ViewPager mViewPager;
    private Context context;

    public static Player player;

    public static Tab curentTab;
    public static final int RUNTIME_PERMISSION_CODE = 7;

    private ImageView nextBtn, playBtn, prevBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        context = getApplication().getApplicationContext();
        player = new Player(context);

        AndroidRuntimePermission();

        nextBtn = findViewById(R.id.next);
        prevBtn = findViewById(R.id.prev);
        playBtn = findViewById(R.id.play);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("VCS", "onClick: click next");
                player.next();

            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("VCS", "onClick: click prev");
                player.prev();
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("VCS", "onClick: click pause");
                if (player.isPlaying()){
                    player.pause();
                }else {
                    player.play();
                }
            }
        });

    }

    private void createLayout(){
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Dsách phát"));
        tabLayout.addTab(tabLayout.newTab().setText("Đang phát"));
        tabLayout.addTab(tabLayout.newTab().setText("Lời bài hát"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(0);
        curentTab = PagerAdapter.tab1;
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Log.d("VCS", "onTabSelected: " + tab.getPosition());

                switch (tab.getPosition()){
                    case 0:
                        curentTab = PagerAdapter.tab1;
                        break;
                    case 1:
                        //Log.d("VCS", "onTabSelected: "+ PagerAdapter.tab2.data);
                        curentTab = PagerAdapter.tab2;
                        break;
                    case 2:
                        //Log.d("VCS", "onTabSelected: "+ PagerAdapter.tab3.data);
                        curentTab = PagerAdapter.tab3;
                        break;
                }

                viewPager.setCurrentItem(tab.getPosition());
                curentTab.update();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //Log.d("VCS", "onTabUnselected: " + tab.getPosition() + tab.view);
//                switch (tab.getPosition()){
//                    case 0:
//                        Log.d("VCS", "onTabSelected: " + PagerAdapter.tab1.data);
//                        playing = PagerAdapter.tab1.playing;
//                        break;
//                    case 1:
//                        Log.d("VCS", "onTabSelected: "+ PagerAdapter.tab2.data);
//                        break;
//                    case 2:
//                        Log.d("VCS", "onTabSelected: "+ PagerAdapter.tab3.data);
//
//
//                        break;
//                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d("VCS", "onRequestPermissionsResult: ");

                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d("VCS", "onRequestPermissionsResult: OK");
                    createLayout();
                }

                else {
                    Log.d("VCS", "onRequestPermissionsResult: else");
                }


    }
    public void AndroidRuntimePermission(){

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){

            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

                if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){

                    AlertDialog.Builder alert_builder = new AlertDialog.Builder(MainActivity.this);
                    alert_builder.setMessage("External Storage Permission is Required.");
                    alert_builder.setTitle("Please Grant Permission.");
                    alert_builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            ActivityCompat.requestPermissions(
                                    MainActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    RUNTIME_PERMISSION_CODE

                            );
                        }
                    });

                    alert_builder.setNeutralButton("Cancel",null);

                    AlertDialog dialog = alert_builder.create();

                    dialog.show();

                }
                else {

                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            RUNTIME_PERMISSION_CODE
                    );
                }
            }else {
                createLayout();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_main, container, false);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
