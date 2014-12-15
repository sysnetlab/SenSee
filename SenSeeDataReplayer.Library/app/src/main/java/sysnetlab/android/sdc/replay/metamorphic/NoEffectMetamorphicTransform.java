package sysnetlab.android.sdc.replay.metamorphic;

import java.util.List;

/**
 * Created by kosta on 12/14/2014.
 */
public class NoEffectMetamorphicTransform implements MetamorphicTransform {

    public List<float[]> transform(List<float[]> inputData)
    {
        //just to show how this could look like
        return inputData;
    }
}
