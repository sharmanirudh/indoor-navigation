package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private int x = 1;
    private int y = 1;
    private TextView textViewXValue;
    private TextView textViewYValue;
    private RelativeLayout relativeLayoutMap;
    private RelativeLayout relativeLayoutPointer;
    private View viewPointer;

    private Button buttonStart;
    private TextView textViewSSID;
    private TextView textViewBSSID;
    private TextView textViewDBm;
    private Router [] router;
    private Coordinate current_coordinate;
    private int old_dBm;
    private String current_BSSID;
    private int iInRouter;

    private String BSSID;
    private String SSID;
    int dBm;
    boolean start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    0);
        }

        relativeLayoutMap = findViewById(R.id.relativeLayoutMap);
        relativeLayoutPointer = findViewById(R.id.relativeLayoutPointer);
        viewPointer = findViewById(R.id.viewPointer);
        buttonStart = findViewById(R.id.buttonStart);
        textViewSSID = findViewById(R.id.textViewSSID);
        textViewBSSID = findViewById(R.id.textViewBSSID);
        textViewDBm = findViewById(R.id.textViewDBm);
        textViewXValue = findViewById(R.id.textViewXValue);
        textViewYValue = findViewById(R.id.textViewYValue);

        router = new Router[1];
        router[0] = new Router();
        router[0].bssid = "08:10:77:51:3c:26";
        router[0].length = 17;
        router[0].coordinate = new Coordinate[17];
        router[0].coordinate[0] = new Coordinate(-63, 0, 0);
        router[0].coordinate[1] = new Coordinate(-69, 0, 3);
        router[0].coordinate[2] = new Coordinate(-62, 0, 6);
        router[0].coordinate[3] = new Coordinate(-60, 0, 9);
        router[0].coordinate[4] = new Coordinate(-61, 0, 12);
        router[0].coordinate[5] = new Coordinate(-58, 0, 15);
        router[0].coordinate[6] = new Coordinate(-63, 0, 18);
        router[0].coordinate[7] = new Coordinate(-46, 0, 21);
        router[0].coordinate[8] = new Coordinate(-53, 0, 24);
        router[0].coordinate[9] = new Coordinate(-48, 0, 27);
        router[0].coordinate[10] = new Coordinate(-52, 0, 30);
        router[0].coordinate[11] = new Coordinate(-57, 0, 33);
        router[0].coordinate[12] = new Coordinate(-64, 0, 36);
        router[0].coordinate[13] = new Coordinate(-72, 0, 39);
        router[0].coordinate[14] = new Coordinate(-77, 0, 42);
        router[0].coordinate[15] = new Coordinate(-78, 0, 45);
        router[0].coordinate[16] = new Coordinate(-76, 0, 48);
//        router[0].length = 17;
//        router[0].coordinate = new Coordinate[17];
//        router[0].coordinate[0] = new Coordinate(-69, 0, 0);
//        router[0].coordinate[1] = new Coordinate(-70, 0, 3);
//        router[0].coordinate[2] = new Coordinate(-74, 0, 6);
//        router[0].coordinate[3] = new Coordinate(-65, 0, 9);
//        router[0].coordinate[4] = new Coordinate(-63, 0, 12);
//        router[0].coordinate[5] = new Coordinate(-58, 0, 15);
//        router[0].coordinate[6] = new Coordinate(-58, 0, 18);
//        router[0].coordinate[7] = new Coordinate(-53, 0, 21);
//        router[0].coordinate[8] = new Coordinate(-47, 0, 24);
//        router[0].coordinate[9] = new Coordinate(-51, 0, 27);
//        router[0].coordinate[10] = new Coordinate(-52, 0, 30);
//        router[0].coordinate[11] = new Coordinate(-63, 0, 33);
//        router[0].coordinate[12] = new Coordinate(-61, 0, 36);
//        router[0].coordinate[13] = new Coordinate(-68, 0, 39);
//        router[0].coordinate[14] = new Coordinate(-70, 0, 42);
//        router[0].coordinate[15] = new Coordinate(-73, 0, 45);
//        router[0].coordinate[16] = new Coordinate(-66, 0, 48);

        start = false;
        buttonStart = findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (!start) { // 0, 0 location
                    Log.d(TAG, "onClick: start button clicked");
                    start = true;
                    buttonStart.setText("stop");
                    iInRouter = 0;
                    current_coordinate = router[0].coordinate[iInRouter];
                    current_BSSID = router[0].bssid;
                    old_dBm = current_coordinate.dbm;
                    x = current_coordinate.x;
                    y = current_coordinate.y;
                    textViewXValue.setText("" + x);
                    textViewYValue.setText("" + y);
                    movePointer(x, y, 1);
                }
                else {
                    Log.d(TAG, "onClick: stop button clicked");
                    start = false;
                    buttonStart.setText("start");
                    textViewXValue.setText("");
                    textViewYValue.setText("");
                }
            }
        });

        final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        final Handler handler = new Handler();
        int delay = 1000; //milliseconds
        handler.postDelayed(new Runnable(){
            public void run(){
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if (wifiInfo.getRssi() == -127) {
                    SSID = "Not connected to WiFi";
                    BSSID = "Connection required!";
                } else {
                    Log.d(TAG, "run: in else block");
                    SSID = wifiInfo.getSSID();
                    BSSID = wifiInfo.getBSSID();
                    dBm = wifiInfo.getRssi();
                    textViewSSID.setText(SSID);
                    textViewBSSID.setText(BSSID);
                    textViewDBm.setText(String.format("%s", dBm));
                    Log.d(TAG, "run: dBm : " + dBm);
                    if (start) {
                        Log.d(TAG, "run: in start section");
                        int i;
                        for (i = 0; i < 1; i++) {
                            if (BSSID.equals(router[i].bssid))
                                break;
                        }
                        if (/*(dBm == old_dBm && current_BSSID.equals(router[i].bssid)) || (dBm == current_coordinate.dbm && current_BSSID.equals(router[i].bssid)) || */i == 1)
                            return;
                        Coordinate[] neighbours = new Coordinate[8];
                        int j, k, l;
                        for (j = iInRouter + 1, l = iInRouter - 1, k = 0; j < Math.min(router[i].length, iInRouter + 5) && l >= Math.max(0, iInRouter - 4); l--, j++) {
                            neighbours[k++] = router[i].coordinate[j];
                            neighbours[k++] = router[i].coordinate[l];
                        }
                        if (j == Math.min(router[i].length, iInRouter + 5)) {
                            while (l >= Math.max(0, iInRouter - 4)) {
                                neighbours[k++] = new Coordinate(+1, -1, -1);
                                neighbours[k++] = router[i].coordinate[l--];
                            }
                        }
                        if (l == Math.max(0, iInRouter - 4 - 1)) {
                            while (j < Math.min(router[i].length, iInRouter + 5)) {
                                neighbours[k++] = router[i].coordinate[j++];
                                neighbours[k++] = new Coordinate(+1, -1, -1);
                            }
                        }
                        String str = "[";
                        for (j = 0; j < k; j++) {
                            if (j % 2 == 0) {
                                str = str + "(" + neighbours[j].dbm;
                            } else {
                                str = str + ", " + neighbours[j].dbm + "), ";
                            }
                        }
                        str = str + "]";
                        Log.d(TAG, "onClick: neighbours[] : " + str);
                        Log.d(TAG, "run: iInRouter : " + iInRouter);
                        Coordinate closestCoordinate = current_coordinate;
                        int minDifference = 99999;
                        int pos = iInRouter;
                        for (j = 0; j < Math.min(2 * 3, k); j++) {//considering only 3 steps in both the direction
                            if (neighbours[j].dbm == 1)
                                continue;
                            if (minDifference > Math.abs(neighbours[j].dbm - dBm)) {
                                minDifference = Math.abs(neighbours[j].dbm - dBm);
                                closestCoordinate = neighbours[j];
                                pos = j;
                            }
                        }
                        old_dBm = dBm;
                        current_coordinate = closestCoordinate;
                        iInRouter = (pos % 2 == 0) ? iInRouter + pos + 1 : iInRouter - pos;
                        current_BSSID = router[i].bssid;
                        x = current_coordinate.x;
                        y = current_coordinate.y;
                        textViewXValue.setText("" + x);
                        textViewYValue.setText("" + y);
                        movePointer(x, y, 1);
                    }
                }
                handler.postDelayed(this, 1000);
            }
        }, delay);

    }

    void movePointer(int x, int y, int z) {
        int maxX = 48;
        int maxY = 48;
        int minX = 0;
        int minY = 0;

        final float scale = this.getResources().getDisplayMetrics().density;
        float dpWidth = Math.max(10, 243f / (maxX - minX) * x); // 20 is the pointer width
        float dpHeight = Math.max(10, 220f / (maxY - minY) * y);
//        Log.d(TAG, "movePointer: relativeLayoutPointer size in dp (" + dpWidth + ", " + dpHeight + ") for pos (" + x + ", " + y + ")");
        int pxWidth = (int) (dpWidth * scale + 0.5f);
        int pxHeight = (int) (dpHeight * scale + 0.5f);
//        Log.d(TAG, "movePointer: relativeLayoutPointer size in px (" + pxWidth + ", " + pxHeight + ") for pos (" + x + ", " + y + ")");
        int marginTop = (int) (47 * scale + 0.5f);
        int marginRight = (int) (102 * scale + 0.5f);
        int marginBottom = (int) (55 * scale + 0.5f);
        int marginLeft = (int) (45 * scale + 0.5f);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(pxWidth, pxHeight);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        params.topMargin = marginTop;
        params.rightMargin = marginRight;
        params.bottomMargin = marginBottom;
        params.leftMargin = marginLeft;
        relativeLayoutPointer.setLayoutParams(params);
//        viewPointer.getLayoutParams().height = pxHeight;
//        viewPointer.getLayoutParams().width = pxWidth;

    }
}
