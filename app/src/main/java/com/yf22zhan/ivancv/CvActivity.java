package com.yf22zhan.ivancv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by yzhang on 2/8/2016.
 */
public class CvActivity extends Activity {

    private TextView textViewName;
    private ImageView imageViewForwardWaterloo;
    private ImageView imageViewForwardVaughan;
    private ImageView imageViewForwardOakville;
    private Button buttonWaterloo;
    private Button buttonVaughan;
    private Button buttonOakville;


    public static void actionStart(Context context, String name) {
        Intent intent = new Intent(context, CvActivity.class);
        intent.putExtra("cv_name", name);
        context.startActivity(intent);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cv);

        String cvName = getIntent().getStringExtra("cv_name");
        textViewName = (TextView) findViewById(R.id.textview_name);
        textViewName.setText(cvName + "'s Footsteps");

        buttonWaterloo = (Button) findViewById(R.id.button_1);
        buttonWaterloo.setOnClickListener(mOnClickListener);
        buttonVaughan = (Button) findViewById(R.id.button_2);
        buttonVaughan.setOnClickListener(mOnClickListener);
        buttonOakville = (Button) findViewById(R.id.button_3);
        buttonOakville.setOnClickListener(mOnClickListener);

        imageViewForwardWaterloo = (ImageView) findViewById(R.id.imagebutton_forward_1);
        imageViewForwardWaterloo.setOnClickListener(mOnClickListener);
        imageViewForwardVaughan = (ImageView) findViewById(R.id.imagebutton_forward_2);
        imageViewForwardVaughan.setOnClickListener(mOnClickListener);
        imageViewForwardOakville = (ImageView) findViewById(R.id.imagebutton_forward_3);
        imageViewForwardOakville.setOnClickListener(mOnClickListener);


    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_1:
                    CvMapActivity.actionStart(CvActivity.this, 1);
                    break;
                case R.id.button_2:
                    CvMapActivity.actionStart(CvActivity.this, 2);
                    break;
                case R.id.button_3:
                    CvMapActivity.actionStart(CvActivity.this, 3);
                    break;


                case R.id.imagebutton_forward_1:
                    CvMapActivity.actionStart(CvActivity.this, 1);
                    break;
                case R.id.imagebutton_forward_2:
                    CvMapActivity.actionStart(CvActivity.this, 2);
                    break;
                case R.id.imagebutton_forward_3:
                    CvMapActivity.actionStart(CvActivity.this, 3);
                    break;

                default:
                    break;
            }
        }
    };

}

