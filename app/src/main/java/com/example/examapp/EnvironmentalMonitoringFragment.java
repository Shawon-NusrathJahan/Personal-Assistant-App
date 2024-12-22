package com.example.examapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Handler;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class EnvironmentalMonitoringFragment extends Fragment implements SensorEventListener {
    MaterialTextView textView, light, temperature, humidity, btnRefreshData;
    Sensor temperatureSensor, lightSensor, humiditySensor;

    // For the hourly recording
    private float tempValue = 0.0f;
    private float lightValue = 0.0f;
    private float humidityValue = 0.0f;

    private final ArrayList<HashMap<String, Float>> dataRecords = new ArrayList<>();
    private final Handler handler = new Handler();




    public EnvironmentalMonitoringFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_environmental_monitoring, container, false);


        temperature = view.findViewById(R.id.textViewTemperature);
        light = view.findViewById(R.id.textViewLight);
        humidity = view.findViewById(R.id.textViewHumidity);
        btnRefreshData = view.findViewById(R.id.btnRefreshData);


        /**
         * it must be here as the fragment needs to be attached/inflated first!!
         * */
        //    SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //typecasting as it returns an Object type.
        SensorManager sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        /*
         * SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
         * Returns: The Activity the fragment is currently associated with or null if the fragment is not attached to an activity.
         * Risk: If the fragment is not attached to an activity at the time this is called, it can lead to a NullPointerException.
         *
         * SensorManager sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
         * Returns: The Activity the fragment is attached to.
         * Risk: Throws an IllegalStateException if the fragment is not currently attached to an activity.
         */
        if (sensorManager == null) {
            Log.e("EnvironmentalMonitoring", "SensorManager is not initialized");
        }

        /* Initializing the sensors! */

        // Initialize temperature sensor
        if (sensorManager != null) {
            temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            if (temperatureSensor != null) {
                sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_UI);
            } else {
                temperature.setText("Temperature sensor not available.");
            }

            // Initialize light sensor
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            if (lightSensor != null) {
                sensorManager.registerListener(this, lightSensor, sensorManager.SENSOR_DELAY_NORMAL);
            } else {
                light.setText("Light sensor not available.");
            }

            // Initialize humidity sensor
            humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
            if (humiditySensor != null) {
                sensorManager.registerListener(this, humiditySensor, SensorManager.SENSOR_DELAY_UI);
            } else {
                humidity.setText("Humidity sensor not available.");
            }
        }

        /* Start recording data every hour */
//        scheduleHourlyDataRecording();

        // Record data every hour
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                HashMap<String, Float> record = new HashMap<>();
                record.put("Temperature ", tempValue);
                record.put("Light ", lightValue);
                record.put("Humidity ", humidityValue);

                dataRecords.add(record);

                // Log data for debugging
                System.out.println("Data Recorded: " + record.toString());
                handler.postDelayed(this, 3600 * 1000); // 1 hour in milliseconds
            }
        }, 3600 * 1000); // Initial delay


        return view;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            float tempValue = event.values[0];
            temperature.setText("Temperature: " + tempValue + "°C");
        }
        if(event.sensor.getType()==Sensor.TYPE_LIGHT){
            if(event.values[0]<1){
                light.setText("The Room is Dark: " + event.values[0]);
            }
            else {
                light.setText("The Room is Light: " + event.values[0]);
            }
        }
        if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            humidityValue = event.values[0];
            humidity.setText("Humidity: " + humidityValue + "%");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // You can handle changes in sensor accuracy here if needed
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        if (sensorManager != null) {
//            sensorManager.unregisterListener(this);
//        }
//        handler.removeCallbacksAndMessages(null); // Stop the hourly recording when fragment is paused
//    }
}

