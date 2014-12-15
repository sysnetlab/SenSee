package sysnetlab.android.sdc.replay.metamorphic;

import java.util.List;

/**
 * Created by kosta on 12/14/2014.
 */
public interface MetamorphicTransform {
    public List<float[]> transform(List<float[]> inputSensorData);
}
