
package com.kth.baasio.startup;

import com.kth.baasio.callback.BaasioCallback;
import com.kth.baasio.entity.entity.BaasioEntity;
import com.kth.baasio.exception.BaasioException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends Activity {

    private TextView tvBaasio;

    private UUID savedUuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvBaasio = (TextView)findViewById(R.id.hello);

        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                BaasioEntity entity = new BaasioEntity("greeting");
                entity.setProperty("greeting", getString(R.string.success_baasio) + "\n\n생성시간:"
                        + System.currentTimeMillis());

                entity.saveInBackground(new BaasioCallback<BaasioEntity>() {

                    @Override
                    public void onResponse(BaasioEntity response) {
                        if (response != null) {
                            tvBaasio.setText(R.string.success_baasio);
                            savedUuid = response.getUuid();

                            Toast.makeText(MainActivity.this, "성공하였습니다.", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onException(BaasioException e) {
                        Log.e("baas.io", e.toString());

                        Toast.makeText(MainActivity.this, "실패하였습니다.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        Button get = (Button)findViewById(R.id.get);
        get.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (savedUuid != null) {
                    BaasioEntity entity = new BaasioEntity("greeting");
                    entity.setUuid(savedUuid);

                    entity.getInBackground(new BaasioCallback<BaasioEntity>() {

                        @Override
                        public void onResponse(BaasioEntity response) {
                            if (response != null) {
                                String result = response.getProperty("greeting").getTextValue();
                                tvBaasio.setText(result);

                                Toast.makeText(MainActivity.this, "성공하였습니다.", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }

                        @Override
                        public void onException(BaasioException e) {
                            Log.e("baas.io", e.toString());

                            Toast.makeText(MainActivity.this, "실패하였습니다.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}
