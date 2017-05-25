/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vikrantchoudhary.graphsensors;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;

/**
 * A basic sample which shows how to use {@link }
 * to display a custom {@link ViewPager} title strip which gives continuous feedback to the user
 * when scrolling.
 */
public class SlidingTabsBasicFragment extends Fragment implements SensorEventListener {

    static final String LOG_TAG = "SENSORS";

    private static SensorManager sensorManager;
    private static Sensor sensorAccelerometer, sensorProximity;

    GraphView graphViewAccelerometer, graphViewLight, graphViewSound;
    static LineGraphSeries<DataPoint> seriesAccelerometer, seriesProximity, seriesSound;

    MediaRecorder mRecorder;
    private Handler mHandler = new Handler();
    private static int POLL_INTERVAL = 150;
    private static int cur_tab = 0;

    static float accX, proX, soundX;
    boolean soundRun = false;

    private static int axis =0;

    public static void setAxis(int ax){
        axis=ax;
        seriesAccelerometer.resetData(new DataPoint[]{
                new DataPoint(0,0)
        });
        seriesProximity.resetData(new DataPoint[]{
                new DataPoint(0,0)
        });
        seriesSound.resetData(new DataPoint[]{
                new DataPoint(0,0)
        });
        accX =0;proX=0;soundX=0;
    }

    /**
     * A custom {@link ViewPager} title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    private SlidingTabLayout mSlidingTabLayout;

    /**
     * A {@link ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private ViewPager mViewPager;

    /**
     * Inflates the {@link View} which will be displayed by this {@link Fragment}, from the app's
     * resources.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accX = 0;
        proX = 0;
        soundX = 0;
        soundRun = false;
        seriesProximity = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 0)
        });
        seriesAccelerometer = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 0)
        });
        seriesSound = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 0)
        });

        return inflater.inflate(R.layout.fragment_sample, container, false);
    }

    /**
     * This is called after the {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} has finished.
     * Here we can pick out the {@link View}s we need to configure from the content view.
     * <p/>
     * We set the {@link ViewPager}'s adapter to be an instance of {@link SamplePagerAdapter}. The
     * {@link SlidingTabLayout} is then given the {@link ViewPager} so that it can populate itself.
     *
     * @param view View created in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());

        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setCustomTabView(R.layout.custom_tab, 0);
//        mSlidingTabLayout.setDividerColors(R.color.divider);
//        mSlidingTabLayout.setSelectedIndicatorColors(R.color.icons);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    /* ####################### Sensor Methods ###################### */

    public void unregisterSensorListener() {
        sensorManager.unregisterListener(this);
        seriesAccelerometer.resetData(new DataPoint[]{
                new DataPoint(0, 0)
        });
        accX = 0;
        seriesProximity.resetData(new DataPoint[]{
                new DataPoint(0, 0)
        });
        proX = 0;

    }

    public void reregisterSensorListener() {
        sensorManager.unregisterListener(this);
        seriesAccelerometer.resetData(new DataPoint[]{
                new DataPoint(0, 0)
        });
        seriesProximity.resetData(new DataPoint[]{
                new DataPoint(0, 0)
        });
        accX = 0;
        proX = 0;
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensorProximity, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // Log.i(LOG_TAG,"Value changed : x = "+event.values[0] + " , sensor type = " + event.sensor);

        if (sensorAccelerometer == event.sensor) {
            accX += 0.06;
            seriesAccelerometer.appendData(new DataPoint(accX, event.values[axis]), true, 100000);
            Log.i(LOG_TAG, "Accelerometer values : x = " + event.values[0]);
        } else if (sensorProximity == event.sensor) {
            proX += 0.1;
            seriesProximity.appendData(new DataPoint(proX, event.values[0]), true, 100000);
            Log.i(LOG_TAG, "Proximity sensor values: x = " + event.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        if (soundRun) {
            try {
                soundThread.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensorProximity, SensorManager.SENSOR_DELAY_UI);
    }

    /* --------------------------------------------------------- */

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

            if (soundRun) {
                soundX += 0.15;
                Log.i(LOG_TAG, "******************************");
                int amp = mRecorder.getMaxAmplitude();
                Log.i(LOG_TAG, "New Amp = " + amp + " , x = " + soundX);
                //int decibel = (int) ( 20 * Math.log10(amp / 2700.0));
                seriesSound.appendData(new DataPoint(soundX, amp), true, 100000);
            } else
                POLL_INTERVAL = 5000;

            mHandler.postDelayed(soundThread, POLL_INTERVAL);
        }
    };
    /* ----------------------------------------------------------------- */

    /**
     * The {@link android.support.v4.view.PagerAdapter} used to display pages in this sample.
     * The individual pages are simple and just display two lines of text. The important section of
     * this class is the {@link #getPageTitle(int)} method which controls what is displayed in the
     * {@link SlidingTabLayout}.
     */
    class SamplePagerAdapter extends PagerAdapter {

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 4;
        }

        /**
         * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
         * same object as the {@link View} added to the {@link ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}.
         * <p/>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        private int[] imageResId = {
                R.drawable.ic_accelerometer_sensor,
                R.drawable.ic_light_sensor,
                R.drawable.ic_sound_sensor,
                R.drawable.ic_experiment,
        };

        @Override
        public CharSequence getPageTitle(int position) {
            Drawable image = getResources().getDrawable(imageResId[position]);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            SpannableString sb = new SpannableString(" ");
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        }

        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            Log.i(LOG_TAG, "*******************************");
            Log.i(LOG_TAG, "Value of position = " + position);
            View view = null;
            switch (position) {
                case 0:
                    view = getActivity().getLayoutInflater().inflate(R.layout.fragment_accelerometer, container, false);
                    container.addView(view);

                    graphViewAccelerometer = (GraphView) view.findViewById(R.id.graph_view_accelerometer);
                    graphViewAccelerometer.getViewport().setXAxisBoundsManual(true);
                    graphViewAccelerometer.getViewport().setMinX(0);
                    graphViewAccelerometer.getViewport().setMaxX(10);
                    graphViewAccelerometer.getViewport().setScrollable(true);
                    graphViewAccelerometer.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
                    if (seriesAccelerometer != null) {
                        Log.i(LOG_TAG, "********Series is reseting...*******");
                        accX = 0;
                        seriesAccelerometer.resetData(new DataPoint[]{
                                new DataPoint(0, 0)
                        });
                    } else {
                        seriesAccelerometer = new LineGraphSeries<>(new DataPoint[]{
                                new DataPoint(0, 0)
                        });
                    }
                    graphViewAccelerometer.addSeries(seriesAccelerometer);
                    sensorLookup(position);
                    break;
                case 1:
                    view = getActivity().getLayoutInflater().inflate(R.layout.fragment_proximity, container, false);
                    container.addView(view);

                    Log.i(LOG_TAG, "*******************************");

                    graphViewLight = (GraphView) view.findViewById(R.id.graph_view_proximity);
                    graphViewLight.getViewport().setXAxisBoundsManual(true);
                    graphViewLight.getViewport().setMinX(0);
                    graphViewLight.getViewport().setMaxX(2);
                    graphViewLight.getViewport().setScrollable(true);
                    graphViewLight.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);

                    if (seriesProximity != null) {
                        proX = 0;
                        Log.i(LOG_TAG, "********Series is reseting...*******");
                        seriesProximity.resetData(new DataPoint[]{
                                new DataPoint(0, 0)
                        });
                    } else {
                        seriesProximity = new LineGraphSeries<>(new DataPoint[]{
                                new DataPoint(0, 0)
                        });
                    }
                    graphViewLight.addSeries(seriesProximity);
                    sensorLookup(position);
                    break;

                case 2:
                    view = getActivity().getLayoutInflater().inflate(R.layout.fragment_sound, container, false);
                    container.addView(view);

                    Log.i(LOG_TAG, "*******************************");
                    Log.i(LOG_TAG, "position = " + position + ", Sound Sensor.");

                    graphViewSound = (GraphView) view.findViewById(R.id.graph_view_sound);
                    graphViewSound.getViewport().setXAxisBoundsManual(true);
                    graphViewSound.getViewport().setMinX(0);
                    graphViewSound.getViewport().setMaxX(3);
                    graphViewSound.getViewport().setScrollable(true);
                    graphViewSound.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);

                    if (seriesSound != null) {
                        soundX = 0;
                        Log.i(LOG_TAG, "********Series is reseting...*******");
                        seriesSound.resetData(new DataPoint[]{
                                new DataPoint(0, 0)
                        });
                    } else {
                        seriesSound = new LineGraphSeries<>(new DataPoint[]{
                                new DataPoint(0, 0)
                        });
                    }
                    graphViewSound.addSeries(seriesSound);
                    sensorLookup(position);
                    break;

                case 3:
                    view = getActivity().getLayoutInflater().inflate(R.layout.fragment_experiments,
                            container, false);
                    // Add the newly created View to the ViewPager
                    container.addView(view);

                    // Retrieve a TextView from the inflated View, and update it's text
                    /*TextView title = (TextView) view.findViewById(R.id.item_title);
                    title.setText(String.valueOf(position + 1));*/
                    sensorLookup(position);
            }

            return view;
        }

        public void sensorLookup(int position){
            Log.i(LOG_TAG, "*******************************");
            Log.i(LOG_TAG, "outside, Position = " + position);
            // Inflate a new layout from our resources

            if (cur_tab == 0 && position == 0) {

                sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                sensorProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
                reregisterSensorListener();
                Log.i(LOG_TAG, "*******************************");

                if (sensorAccelerometer == null) {
                    Toast.makeText(getActivity().getApplicationContext(), "Accelerometer Sensor is not present in your phone", Toast.LENGTH_SHORT).show();
                    //Snackbar.make(view,"Accelerometer Sensor is not present in your phone.",Snackbar.LENGTH_SHORT).show();
                    Log.i(LOG_TAG, "Accelerometer Sensor is not present in your phone, Sorry.");
                }
                if (sensorProximity == null) {
                    Toast.makeText(getActivity().getApplicationContext(), "Proximity Sensor is not present in your phone", Toast.LENGTH_SHORT).show();
                    //Snackbar.make(view,"Accelerometer Sensor is not present in your phone, Sorry.",Snackbar.LENGTH_SHORT).show();
                    Log.i(LOG_TAG, "Proximity Sensor is not present in your phone, Sorry.");
                }

                cur_tab = 0;
            } else if (cur_tab == 0 && position == 1) {

                cur_tab = 0;
            } else if (cur_tab == 0 && position == 2) {

                seriesAccelerometer.resetData(new DataPoint[]{
                        new DataPoint(0, 0)
                });
                accX = 0;
                cur_tab = 1;
            } else if (cur_tab == 1 && position == 3) {

                unregisterSensorListener();
                seriesProximity.resetData(new DataPoint[]{
                        new DataPoint(0, 0)
                });
                proX = 0;
                seriesAccelerometer.resetData(new DataPoint[]{
                        new DataPoint(0, 0)
                });
                accX = 0;

                seriesSound.appendData(new DataPoint(0.001, 3), true, 100000);
                if (!soundRun) {
                    soundSensorSetup();
                    POLL_INTERVAL = 150;
                    soundRun = true;
                    soundThread.run();
                }
                cur_tab = 2;
            } else if (cur_tab == 2 && position == 1) {

                cur_tab = 2;
            } else if (cur_tab == 2 && position == 0) {

                if (soundRun) {
                    soundRun = false;
                    mRecorder.stop();

                    seriesSound.resetData(new DataPoint[]{
                            new DataPoint(0, 0)
                    });
                    soundX = 0;
                }

                sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                sensorProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
                reregisterSensorListener();
                Log.i(LOG_TAG, "*******************************");
                if (sensorAccelerometer == null) {
                    Toast.makeText(getActivity().getApplicationContext(), "Accelerometer Sensor is not present in your phone", Toast.LENGTH_SHORT).show();
                    //Snackbar.make(view,"Accelerometer Sensor is not present in your phone.",Snackbar.LENGTH_SHORT).show();
                    Log.i(LOG_TAG, "Accelerometer Sensor is not present in your phone, Sorry.");
                }
                if (sensorProximity == null) {
                    Toast.makeText(getActivity().getApplicationContext(), "Proximity Sensor is not present in your phone", Toast.LENGTH_SHORT).show();
                    //Snackbar.make(view,"Accelerometer Sensor is not present in your phone, Sorry.",Snackbar.LENGTH_SHORT).show();
                    Log.i(LOG_TAG, "Proximity Sensor is not present in your phone, Sorry.");
                }
            }
        }

        /**
         * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
         * {@link View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}