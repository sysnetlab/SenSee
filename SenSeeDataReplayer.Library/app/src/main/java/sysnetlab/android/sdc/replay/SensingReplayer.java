package sysnetlab.android.sdc.replay;

import android.hardware.SensorEventListener;

import java.util.List;

/**
 * Created by kosta on 12/13/2014.
 */
public interface SensingReplayer {

    public void replayOne();
    public void replayAll();
    public boolean replayCompleted();
    public float[] getNextSensorValuesForReplay();
    public List<float[]> getAllSensorValuesForReplay();

}
