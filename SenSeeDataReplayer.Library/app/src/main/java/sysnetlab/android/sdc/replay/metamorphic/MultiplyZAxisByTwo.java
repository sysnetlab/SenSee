package sysnetlab.android.sdc.replay.metamorphic;

import java.util.List;
import java.util.*;


public class MultiplyZAxisByTwo implements MetamorphicTransform {

    public List<float[]> transform(List<float[]> inputData)
    {

        for(int i=0; i<inputData.size();i++) {
            float[] array = inputData.get(i);

            array[2] = array[2]*2;

            //for (int j = 2;j<array.length;j+=3) {

                //array[i] = array[i]*2;
            //}
            //inputData.set(i, array);
        }
        return inputData;


    }
}
