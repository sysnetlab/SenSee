package sysnetlab.android.sdc.replay;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SenSeeSensingReplayer implements SensingReplayer {

    private Scanner scanner;
    private SensorEventListener listener;

    public SenSeeSensingReplayer(InputStream dataStream,
                                 SensorEventListener listener) {
        try {
            this.scanner = new Scanner(new InputStreamReader(dataStream, "UTF-8"));
            this.listener = listener;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public boolean replayCompleted() {
        return !scanner.hasNext();
    }


    public void replayOne() {
        //TODO: incomplete

        float[] values = getNextSensorValuesForReplay();
        if(values != null) {
            SensorEvent se = mock(SensorEvent.class);
            //Sensor sensor = mock(Sensor.class);
            //when(sensor.getType()).thenReturn(Sensor.TYPE_ACCELEROMETER);

            //when(se.values).thenReturn(values);
            //when(se.sensor).thenReturn(sensor);

            listener.onSensorChanged(se);
        }
    }

    public void replayAll() {
        while (!replayCompleted()) replayOne();
    }

    public float[] getNextSensorValuesForReplay()
    {
        String line = scanner.nextLine();
        if (line != null) {
            String[] strValues = line.split("[\\s,]+");
            if (strValues.length > 4) {
                long timestamp = Long.parseLong(strValues[0]);

                float[] values = new float[3];
                for (int i = 0; i < 3; i++) {
                    values[i] = Float.parseFloat(strValues[i + 2]);
                }
                return values;
            }
        }
        return null;
    }

    public List<float[]> getAllSensorValuesForReplay()
    {
        List<float[]> listOfValues = new ArrayList<float[]>();
        while(!replayCompleted())
        {
            listOfValues.add(getNextSensorValuesForReplay());
        }
        return listOfValues;
    }

}
