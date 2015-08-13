package sysnetlab.android.sdc.replay.metamorphic;

import java.util.List;


public class Semantic {

    public List<float[]> transform(List<float[]> inputData)
    {

        for(int i=0; i<inputData.size();i++) {
            float[] array = inputData.get(i);

            // Test X Value
            array[0] = (float) (array[0]* (Math.cos(0)) );

            // Test Y Value
            array[1] = (float) (array[1]* (Math.cos(0)) );

            // Test Z Value
            array[2] = (float) (array[2]* (Math.cos(0)) );

            inputData.set(i, array);
        }
        return inputData;


    }

}
