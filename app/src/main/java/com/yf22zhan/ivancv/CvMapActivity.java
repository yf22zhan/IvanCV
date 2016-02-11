package com.yf22zhan.ivancv;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by yzhang on 2/8/2016.
 */

// Note: Any (int index) parameter to ViewPager functions starts with 0
// Whereas the variables 'selectPage' and 'RowID' to SQLite start with 1

public class CvMapActivity extends Activity implements OnMapReadyCallback {

    private GoogleMap googleMap;

    private ImageButton imageButtonLeft;
    private ImageButton imageButtonRight;
    private ViewPager viewPager;
    private PageAdapter pageAdapter;
    private Marker marker = null;
    private Circle circleBig = null;
    private Circle circleSmall = null;

    private int selectPage = 0;
    private static String location = "";

    private static MyDatabaseHelper dbHelper;

    public static void actionStart(Context context, int position) {
        Intent intent = new Intent(context, CvMapActivity.class);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // selectPage == 1 or in other words index == 0 is checked
        // because ViewPager.onPageSelected(int index) does not get triggered
        // the first  time calling setCurrentItem(0)
        if (selectPage == 1) {
            setUpMap();
        } else {
            viewPager.setCurrentItem((selectPage - 1));
        }
    }

    private void setUpMap() {

        if (selectPage == 1) {
            imageButtonLeft.setVisibility(View.INVISIBLE);
        } else if (selectPage == 3) {
            imageButtonRight.setVisibility(View.INVISIBLE);
        } else {
            imageButtonLeft.setVisibility(View.VISIBLE);
            imageButtonRight.setVisibility(View.VISIBLE);
        }

        double lat = 0f;
        double lng = 0f;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursorCurrent = db.query("footstep", null, "id=" + selectPage, null, null, null, null);
        if (cursorCurrent.moveToFirst()) {
            lat = cursorCurrent.getDouble(cursorCurrent.getColumnIndex("lat"));
            lng = cursorCurrent.getDouble(cursorCurrent.getColumnIndex("lng"));
        }
        cursorCurrent.close();

        if (marker != null) {
            marker.remove();
            circleBig.remove();
            circleSmall.remove();
        }

        LatLng currentLocation = new LatLng(lat, lng);
        marker = googleMap.addMarker(new MarkerOptions().position(currentLocation)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker)));
        circleBig = googleMap.addCircle(new CircleOptions().center(currentLocation)
                .fillColor(Color.parseColor("#7f448aff"))
                .radius(1000).strokeWidth(0f));
        circleSmall = googleMap.addCircle(new CircleOptions().center(currentLocation)
                .fillColor(Color.parseColor("#448aff"))
                .radius(150).strokeWidth(2f).strokeColor(Color.parseColor("#ffffff")));
        googleMap.animateCamera(CameraUpdateFactory
                .newLatLngZoom(currentLocation, 12f), 1000, null);
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // TODO Auto-generated method stub
                CvDetailedContentActivity.actionStart(CvMapActivity.this, selectPage);
                return false;
            }
        });

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.avtivity_cvmap);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dbHelper = new MyDatabaseHelper(this, "FootStep.db", null, 1);

        selectPage = getIntent().getIntExtra("position", 0);

        /**
         * viewPager settings,the most important part is setOnPageChangeListener to
         * record the page change state in order to update googleMap camera LatLng position
         **/

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                selectPage = position + 1;
                setUpMap();
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // TODO Auto-generated method stub
            }
        });

        pageAdapter = new PageAdapter(getFragmentManager());
        viewPager.setAdapter(pageAdapter);
        imageButtonLeft = (ImageButton) findViewById(R.id.imagebutton_left);
        imageButtonLeft.setOnClickListener(mOnClickListener);
        imageButtonRight = (ImageButton) findViewById(R.id.imagebutton_right);
        imageButtonRight.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imagebutton_left:
                    viewPager.setCurrentItem((selectPage - 2), true);
                    break;
                case R.id.imagebutton_right:
                    viewPager.setCurrentItem(selectPage, true);
                    break;
            }
        }
    };

    public static class PlaceHolderFragment extends Fragment {
        private static final String EXTRA_POSITION = "EXTRA_POSITION";

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final int position = getArguments().getInt(EXTRA_POSITION);

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query("footstep", null, "id=" + position, null, null, null, null);
            if (cursor.moveToFirst()) {
                location = cursor.getString(cursor.getColumnIndex("name"));
            }
            cursor.close();

            final TextView textViewPosition = (TextView) inflater.inflate(R.layout.fragment_cvmap, container, false);
            textViewPosition.setText(location);
            textViewPosition.setBackgroundColor(Color.parseColor("#df673ab7"));

            return textViewPosition;
        }
    }

    private static final class PageAdapter extends FragmentStatePagerAdapter {
        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            final Bundle bundle = new Bundle();
            bundle.putInt(PlaceHolderFragment.EXTRA_POSITION, position + 1);

            final PlaceHolderFragment fragment = new PlaceHolderFragment();
            fragment.setArguments(bundle);

            return fragment;
        }

        public int getCount() {
            return 3;
        }
    }

}
