package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
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
    private Button buttonIncreaseX;
    private Button buttonDecreaseX;
    private Button buttonIncreaseY;
    private Button buttonDecreaseY;
    private RelativeLayout relativeLayoutMap;
    private RelativeLayout relativeLayoutPointer;
    private View viewPointer;

    private EditText editTextBSSID;
    private EditText editTextDBm;
    private Button buttonLocate;
    private Router [] router;
    private Coordinate current_coordinate;
    private int old_dBm;
    private String current_BSSID;
    private int iInRouter;

    private String BSSID;
    private String SSID;
    int dBm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        relativeLayoutMap = findViewById(R.id.relativeLayoutMap);
        relativeLayoutPointer = findViewById(R.id.relativeLayoutPointer);
        viewPointer = findViewById(R.id.viewPointer);
        textViewXValue = findViewById(R.id.textViewXValue);
        textViewYValue = findViewById(R.id.textViewYValue);
        buttonIncreaseX = findViewById(R.id.buttonIncreaseX);
        buttonIncreaseX.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (x < 40) {
                    x++;
                    textViewXValue.setText("" + x);
                    movePointer(x, y, 1);
                }
            }
        });
        buttonDecreaseX = findViewById(R.id.buttonDecreaseX);
        buttonDecreaseX.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (x > 1) {
                    x--;
                    textViewXValue.setText("" + x);
                    movePointer(x, y, 1);
                }
            }
        });
        buttonDecreaseY = findViewById(R.id.buttonDecreaseY);
        buttonDecreaseY.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (y > 1) {
                    y--;
                    textViewYValue.setText("" + y);
                    movePointer(x, y, 1);
                }
            }
        });
        buttonIncreaseY = findViewById(R.id.buttonIncreaseY);
        buttonIncreaseY.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (y < 40) {
                    y++;
                    textViewYValue.setText("" + y);
                    movePointer(x, y, 1);
                }
            }
        });

        router = new Router[7];
        router[0] = new Router();
        router[0].bssid = "ac:67:06:dd:94:b8";
        router[0].length = 13;
        router[0].coordinate = new Coordinate[13];
        router[0].coordinate[0] = new Coordinate(-30, 0, 0);
        router[0].coordinate[1] = new Coordinate(-44, 0, 3);
        router[0].coordinate[2] = new Coordinate(-44, 0, 6);
        router[0].coordinate[3] = new Coordinate(-46, 0, 9);
        router[0].coordinate[4] = new Coordinate(-48, 0, 12);
        router[0].coordinate[5] = new Coordinate(-51, 0, 15);
        router[0].coordinate[6] = new Coordinate(-49, 0, 18);
        router[0].coordinate[7] = new Coordinate(-55, 0, 21);
        router[0].coordinate[8] = new Coordinate(-57, 0, 24);
        router[0].coordinate[9] = new Coordinate(-60, 0, 27);
        router[0].coordinate[10] = new Coordinate(-56, 0, 30);
        router[0].coordinate[11] = new Coordinate(-57, 0, 33);
        router[0].coordinate[12] = new Coordinate(-63, 0, 36);

        router[1] = new Router();
        router[1].bssid = "84:18:3a:dc:68:88";
        router[1].length = 13;
        router[1].coordinate = new Coordinate[13];
        router[1].coordinate[0] = new Coordinate(-49, 0, 39);
        router[1].coordinate[1] = new Coordinate(-45, 0, 42);
        router[1].coordinate[2] = new Coordinate(-52, 0, 45);
        router[1].coordinate[3] = new Coordinate(-40, 0, 49);
        router[1].coordinate[4] = new Coordinate(-67, 3, 49);
        router[1].coordinate[5] = new Coordinate(-41, 6, 49);
        router[1].coordinate[6] = new Coordinate(-39, 9, 49);
        router[1].coordinate[7] = new Coordinate(-39, 12, 49);
        router[1].coordinate[8] = new Coordinate(-30, 15, 49);
        router[1].coordinate[9] = new Coordinate(-31, 18, 49);
        router[1].coordinate[10] = new Coordinate(-41, 21, 49);
        router[1].coordinate[11] = new Coordinate(-38, 24, 49);
        router[1].coordinate[12] = new Coordinate(-44, 27, 49);

        router[6] = new Router();
        router[6].bssid = "84:18:3a:dc:68:88";
        router[6].length = 8;
        router[6].coordinate = new Coordinate[8];
        router[6].coordinate[0] = new Coordinate(-28, 21, 0);
        router[6].coordinate[1] = new Coordinate(-39, 18, 0);
        router[6].coordinate[2] = new Coordinate(-45, 15, 0);
        router[6].coordinate[3] = new Coordinate(-47, 12, 0);
        router[6].coordinate[4] = new Coordinate(-46, 9, 0);
        router[6].coordinate[5] = new Coordinate(-49, 6, 0);
        router[6].coordinate[6] = new Coordinate(-52, 3, 0);
        router[6].coordinate[7] = new Coordinate(-53, 0, 0);


        current_coordinate = new Coordinate(-55, 0, 21);
        old_dBm = current_coordinate.dbm;
        current_BSSID = router[0].bssid;
        x = current_coordinate.x;
        y = current_coordinate.y;
        textViewXValue.setText("" + x);
        textViewYValue.setText("" + y);
        movePointer(x, y, 1);
        iInRouter = 7;

        editTextBSSID = findViewById(R.id.editTextBSSID);
        editTextDBm = findViewById(R.id.editTextDBm);
        buttonLocate = findViewById(R.id.buttonLocate);
        buttonLocate.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String BSSID = editTextBSSID.getText().toString();
                int dBm = - Math.abs(Integer.parseInt(editTextDBm.getText().toString()));
                int i;
                for (i = 0; i < 3; i++) {
                    if (BSSID.equals(router[i].bssid))
                        break;
                }
                if ((dBm == old_dBm && current_BSSID.equals(router[i].bssid)) || (dBm == current_coordinate.dbm && current_BSSID.equals(router[i].bssid)) || i == 4)
                    return;
                Coordinate [] neighbours = new Coordinate[8];
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
                    }
                    else {
                        str = str + ", " + neighbours[j].dbm + "), ";
                    }
                }
                str = str + "]";
                Log.d(TAG, "onClick: neighbours[] : " + str);
                Coordinate closestCoordinate = current_coordinate;
                int minDifference = 99999;
                int pos = iInRouter;
                for (j = 0; j < Math.min(2 * 3, k) ; j++) {//considering only 3 steps
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
        });


    }

    void movePointer(int x, int y, int z) {
        int maxX = 48;
        int maxY = 49;
        int minX = 0;
        int minY = 0;

        final float scale = this.getResources().getDisplayMetrics().density;
        float dpWidth = Math.max(20, 296f / (maxX - minX) * x); // 20 is the pointer width
        float dpHeight = Math.max(20, 316f / (maxY - minY) * y);
//        Log.d(TAG, "movePointer: relativeLayoutPointer size in dp (" + dpWidth + ", " + dpHeight + ") for pos (" + x + ", " + y + ")");
        int pxWidth = (int) (dpWidth * scale + 0.5f);
        int pxHeight = (int) (dpHeight * scale + 0.5f);
//        Log.d(TAG, "movePointer: relativeLayoutPointer size in px (" + pxWidth + ", " + pxHeight + ") for pos (" + x + ", " + y + ")");
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(pxWidth, pxHeight);
        int margin = (int) (42 * scale + 0.5f);
        params.topMargin = margin;
        params.rightMargin = margin;
        params.bottomMargin = margin;
        params.leftMargin = margin;
        relativeLayoutPointer.setLayoutParams(params);
//        viewPointer.getLayoutParams().height = pxHeight;
//        viewPointer.getLayoutParams().width = pxWidth;

    }
}
