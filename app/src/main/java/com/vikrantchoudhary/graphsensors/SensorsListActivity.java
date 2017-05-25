package com.vikrantchoudhary.graphsensors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vikrantchoudhary.graphsensors.bean.SensorBean;

import java.util.ArrayList;
import java.util.List;

public class SensorsListActivity extends BaseActivity {

    private static final String LOGTAG = "SENSORS" ;
    private SensorManager sensorManager;
    private Sensor sensor=null;
    private List<Sensor> sensorList = new ArrayList<>();
    private List<SensorBean> mSensorBean = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        SensorBean sensorBeanL = new SensorBean();
        sensorBeanL.setTitle("Sound Sensor");
        sensorBeanL.setSubtitle("Measures the amplitude of sound.");
        sensorBeanL.setType(12345);
        sensorBeanL.setPhoto(R.drawable.ic_sound_sensor);
        mSensorBean.add(sensorBeanL);

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(sensor != null){
            SensorBean sensorBean = new SensorBean();
            sensorBean.setTitle("Accelerometer Sensor");
            sensorBean.setSubtitle("Measures the acceleration force in m/s2 that is applied to a device on all three physical axes (x, y, and z), including the force of gravity.");
            sensorBean.setType(sensor.TYPE_ACCELEROMETER);
            sensorBean.setPhoto(R.drawable.ic_accelerometer_sensor);
            mSensorBean.add(sensorBean);
            sensorList.add(sensor);
            sensor = null;
        }
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(sensor != null){
            SensorBean sensorBean = new SensorBean();
            sensorBean.setTitle("Proximity Sensor");
            sensorBean.setSubtitle("Measures the proximity of an object in cm relative to the view screen of a device. This sensor is typically used to determine whether a handset is being held up to a person's ear.");
            sensorBean.setType(Sensor.TYPE_PROXIMITY);
            sensorBean.setPhoto(R.drawable.ic_settings_cell_black_48dp);
            mSensorBean.add(sensorBean);
            sensorList.add(sensor);
            sensor = null;
        }
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if(sensor != null){
            SensorBean sensorBean = new SensorBean();
            sensorBean.setTitle("Ambient Temperature Sensor");
            sensorBean.setSubtitle("Measures the ambient room temperature in degrees Celsius (°C).");
            sensorBean.setType(Sensor.TYPE_AMBIENT_TEMPERATURE);
            sensorBean.setPhoto(R.drawable.ic_action_temperature);
            mSensorBean.add(sensorBean);
            sensorList.add(sensor);
            sensor = null;
        }
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if(sensor != null){
            SensorBean sensorBean = new SensorBean();
            sensorBean.setTitle("Gravity Sensor");
            sensorBean.setSubtitle("Measures the force of gravity in m/s2 that is applied to a device on all three physical axes (x, y, z).");
            sensorBean.setType(Sensor.TYPE_GRAVITY);
            mSensorBean.add(sensorBean);
            sensorList.add(sensor);
            sensor = null;
        }
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if(sensor != null){
            SensorBean sensorBean = new SensorBean();
            sensorBean.setTitle("Gyroscope Sensor");
            sensorBean.setSubtitle("Measures a device's rate of rotation in rad/s around each of the three physical axes (x, y, and z).");
            sensorBean.setType(Sensor.TYPE_GYROSCOPE);
            mSensorBean.add(sensorBean);
            sensorList.add(sensor);
            sensor = null;
        }
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(sensor != null){
            SensorBean sensorBean = new SensorBean();
            sensorBean.setTitle("Light Sensor");
            sensorBean.setSubtitle("Measures the ambient light level (illumination) in lx.");
            sensorBean.setType(Sensor.TYPE_LIGHT);
            sensorBean.setPhoto(R.drawable.ic_light_sensor);
            mSensorBean.add(sensorBean);
            sensorList.add(sensor);
            sensor = null;
        }
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        if(sensor != null){
            SensorBean sensorBean = new SensorBean();
            sensorBean.setTitle("Linear Acceleration Sensor");
            sensorBean.setSubtitle("Measures the acceleration force in m/s2 that is applied to a device on all three physical axes (x, y, and z), excluding the force of gravity.");
            sensorBean.setType(Sensor.TYPE_LINEAR_ACCELERATION);
            mSensorBean.add(sensorBean);
            sensorList.add(sensor);
            sensor = null;
        }
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(sensor != null){
            SensorBean sensorBean = new SensorBean();
            sensorBean.setTitle("Magnetic Field Sensor");
            sensorBean.setSubtitle("Measures the ambient geomagnetic field for all three physical axes (x, y, z) in μT.");
            sensorBean.setType(Sensor.TYPE_MAGNETIC_FIELD);
            mSensorBean.add(sensorBean);
            sensorList.add(sensor);
            sensor = null;
        }
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if(sensor != null){
            SensorBean sensorBean = new SensorBean();
            sensorBean.setTitle("Pressure Sensor");
            sensorBean.setSubtitle("Measures the ambient air pressure in hPa or mbar.");
            sensorBean.setType(Sensor.TYPE_PRESSURE);
            mSensorBean.add(sensorBean);
            sensorList.add(sensor);
            sensor = null;
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(mSensorBean);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                SensorBean sen = mSensorBean.get(position);
                Toast.makeText(getApplicationContext(), sen.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SensorsListActivity.this,SensorDataActivity.class);
                intent.putExtra("SENSORTYPE",sen.getType());
                intent.putExtra("SENSORTITLE",sen.getTitle());
                intent.putExtra("SENSORSUBTITLE",sen.getSubtitle());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    public Bitmap getCircleCroppedImage(Context context, int imageDrawable) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                imageDrawable);
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 3, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private SensorsListActivity.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final SensorsListActivity.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}