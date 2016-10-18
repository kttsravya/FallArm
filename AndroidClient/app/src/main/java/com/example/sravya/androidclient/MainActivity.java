package com.example.sravya.androidclient;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.StrictMode;



public class MainActivity extends Activity implements SensorEventListener{

    private SensorManager sensorManager;
    EditText textOut;
    TextView textIn;
    TextView xCoor;
    TextView yCoor;
    TextView zCoor;
    TextView xAxis;
    TextView yAxis;
    TextView zAxis;

    float x,y,z,x1,y1,z1;
    String status=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //textOut = (EditText)findViewById(R.id.textout);



        Button buttonSend = (Button)findViewById(R.id.send);

        textIn = (TextView)findViewById(R.id.textin);
        xCoor=(TextView)findViewById(R.id.xcoor); // create X axis object
        yCoor=(TextView)findViewById(R.id.ycoor); // create Y axis object
        zCoor=(TextView)findViewById(R.id.zcoor); // create Z axis object

        xAxis=(TextView)findViewById(R.id.xaxis);
        yAxis=(TextView)findViewById(R.id.yaxis);
        zAxis=(TextView)findViewById(R.id.zaxis);


        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        // add listener. The listener will be HelloAndroid (this) class

        // Register to listen to Accelorometer data
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_NORMAL);

        buttonSend.setOnClickListener(buttonSendOnClickListener);

        // this allows you to run on mainthread

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    Button.OnClickListener buttonSendOnClickListener
            = new Button.OnClickListener(){

        public void onClick(View arg0) {

            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;

            try {
                // tried with 10.0.2.2 instead of 127.0.0.1 because  when we are accessing local host from emulator 127.0.0.1 refers to emulator itself not
                // local host , if using geny motion use 10.0.3.2
                socket = new Socket("10.0.2.2", 8888);
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                //dataOutputStream.writeUTF(textOut.getText().toString());
                dataOutputStream.writeUTF(String.valueOf(x));
                dataOutputStream.writeUTF(String.valueOf(y));
                dataOutputStream.writeUTF(String.valueOf(z));
                dataOutputStream.writeUTF(String.valueOf(x1));
                dataOutputStream.writeUTF(String.valueOf(y1));
                dataOutputStream.writeUTF(String.valueOf(z1));
                dataOutputStream.writeUTF(status);
                textIn.setText(dataInputStream.readUTF());
            } catch (UnknownHostException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
            finally{
                if (socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }

                if (dataOutputStream != null){
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }

                if (dataInputStream != null){
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }
        }};

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
            // assign directions
             x=event.values[0];
             y=event.values[1];
             z=event.values[2];

            xCoor.setText("X: "+x);
            yCoor.setText("Y: "+y);
            zCoor.setText("Z: "+z);



        }else if(event.sensor.getType() == Sensor.TYPE_ORIENTATION){
             x1=event.values[0];
             y1=event.values[1];
             z1=event.values[2];

            xAxis.setText("X: "+x1);
            yAxis.setText("Y: "+y1);
            zAxis.setText("Z: "+z1);

        }

        double calibration = SensorManager.STANDARD_GRAVITY;
        double a = Math.round(Math.sqrt(Math.pow(x, 2) +
                Math.pow(y, 2) +
                Math.pow(z, 2)));
        float currentAcceleration = Math.abs((float)(a - calibration));
        int vecSum = Math.round(currentAcceleration);

        if(vecSum>=8 && vecSum<=10){
            status = "falls on the floor";
        }else if(vecSum>=0 && vecSum<=6){
            status ="doesnt fall on the floor";
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}




