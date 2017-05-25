package com.vikrantchoudhary.graphsensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;

public class SensorDataActivity extends BaseActivity implements SensorEventListener {

    static final String LOG_TAG = "SENSORS";

    GraphView graphView;
    SensorManager sensorManager;
    Sensor sensor;
    LineGraphSeries<DataPoint> series;
    float i = 0;
    boolean soundSensor = true;
    private int SENSORTYPE;
    private float INC;

    MediaRecorder mRecorder;
    private Handler mHandler = new Handler();
    private static int POLL_INTERVAL = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SENSORTYPE = getIntent().getIntExtra("SENSORTYPE", 12345);
        String activityTitle = getIntent().getStringExtra("SENSORTITLE");
        toolbar.setTitle(activityTitle);

        if (SENSORTYPE != 12345) {
            soundSensor = false;
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            sensor = sensorManager.getDefaultSensor(SENSORTYPE);
        }

        if (SENSORTYPE == Sensor.TYPE_PROXIMITY || SENSORTYPE == Sensor.TYPE_LIGHT)
            INC = 0.1f;
        else
            INC = 0.06f;

        graphView = (GraphView) findViewById(R.id.sensorGraphView);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(10);
        graphView.getViewport().setScrollable(true);
        graphView.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 0)
        });
        graphView.addSeries(series);

        if (soundSensor) {
            soundSensorSetup();
            soundThread.run();
        }
    }

    @Override
    protected void onPause() {
        if (soundSensor)
            mRecorder.stop();
        else
            sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (soundSensor)
            soundSensorSetup();
        else
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);

        super.onResume();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        i += INC;
        series.appendData(new DataPoint(i, event.values[0]), true, 100000);
        Log.i(LOG_TAG,"Sensor Values "+ event.values[0]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /*  ##################### Sound Detection Code ##################### */

    public void soundSensorSetup() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile("/dev/null");
        try {
            mRecorder.prepare();
        } catch (IllegalStateException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        mRecorder.start();
    }

    private Runnable soundThread = new Runnable() {
        public void run() {

            i += 0.15;
            Log.i(LOG_TAG, "******************************");
            int amp = mRecorder.getMaxAmplitude();
            Log.i(LOG_TAG, "New Amp = " + amp + " , x = " + i);
            //int decibel = (int) ( 20 * Math.log10(amp / 2700.0));
            series.appendData(new DataPoint(i, amp), true, 100000);

            mHandler.postDelayed(soundThread, POLL_INTERVAL);
        }
    };
    /* ----------------------------------------------------------------- */
}