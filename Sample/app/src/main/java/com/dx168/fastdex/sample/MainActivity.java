package com.dx168.fastdex.sample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by tong on 17/10/3.
 */
public class MainActivity extends Activity {
    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String s1 = getString(R.string.s1);
        String s3 = getString(R.string.s3);
        //Toast.makeText(this,"haha " + " | " + s1 +  " | " + s3,Toast.LENGTH_LONG).show();


        String s2 = getString(R.string.s2);
        Toast.makeText(this,"haha123" + " | " + s1 + " | " + s2 + " | " + s3,Toast.LENGTH_LONG).show();

        new Runnable(){
            @Override
            public void run() {

            }
        };

        new Runnable(){
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),"哈哈哈1",Toast.LENGTH_LONG).show();
            }
        }.run();
    }
}
