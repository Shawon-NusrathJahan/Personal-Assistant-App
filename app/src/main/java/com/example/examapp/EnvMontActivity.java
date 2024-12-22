package com.example.examapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.HashMap;

public class EnvMontActivity extends AppCompatActivity implements SensorEventListener {
    MaterialTextView textView, light, temperature, humidity, btnRefreshData;
    Sensor temperatureSensor, lightSensor, humiditySensor;

    // For the hourly recording
    private float tempValue = 0.0f;
    private float lightValue = 0.0f;
    private float humidityValue = 0.0f;

    private final ArrayList<HashMap<String, Float>> dataRecords = new ArrayList<>();
    private final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        temperature = findViewById(R.id.textViewTemperature);
        light = findViewById(R.id.textViewLight);
        humidity = findViewById(R.id.textViewHumidity);
        btnRefreshData = findViewById(R.id.btnRefreshData);

        // Initialize SensorManager
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager == null) {
            Log.e("EnvMontActivity", "SensorManager is not available");
            return;
        }

        /* Initializing the sensors! */

        // Initialize temperature sensor
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (temperatureSensor != null) {
            sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            temperature.setText("Temperature sensor not available.");
        }

        // Initialize light sensor
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
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

        /* Start recording data every hour */
//        scheduleHourlyDataRecording();

        // Record data every hour
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                HashMap<String, Float> record = new HashMap<>();
                record.put("Temperature", tempValue);
                record.put("Light", lightValue);
                record.put("Humidity", humidityValue);

                dataRecords.add(record);

                // Log data for debugging
                Log.d("EnvMontActivity", "Data Recorded: " + record.toString());

                // Schedule the next recording
                handler.postDelayed(this, 3600 * 1000); // 1 hour in milliseconds
            }
        }, 3600 * 1000); // Initial delay

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            tempValue = event.values[0];
            temperature.setText("Temperature: " + tempValue + "°C");
        } else if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            lightValue = event.values[0];
            if (lightValue < 1) {
                light.setText("The Room is Dark: " + lightValue);
            } else {
                light.setText("The Room is Light: " + lightValue);
            }
        } else if (event.sensor.getType() == Sensor.TYPE_RELATIVE_HUMIDITY) {
            humidityValue = event.values[0];
            humidity.setText("Humidity: " + humidityValue + "%");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (sensorManager != null) {
//            sensorManager.unregisterListener(this);
//        }
//        handler.removeCallbacksAndMessages(null); // Stop the hourly recording when the activity is paused
//    }

}