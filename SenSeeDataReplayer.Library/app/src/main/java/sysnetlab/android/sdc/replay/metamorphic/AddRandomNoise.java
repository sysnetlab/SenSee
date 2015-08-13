package sysnetlab.android.sdc.replay.metamorphic;

import java.util.List;
import java.util.Random;

public class AddRandomNoise {

    public List transform(List<float[]> inputData)
    {
        Random r=new Random();
        //float NoiseValue = 0;

        for(int DataIndex=0; DataIndex<inputData.size();DataIndex++)
        {
            float[] array = inputData.get(DataIndex);
            for(int j =0;j<array.length;j++)
            {
                array[j]+=Math.round(r.nextDouble()*100)/100.0;
            }
            inputData.set(DataIndex, array);
        }
        return inputData;
    }
}
