package sysnetlab.android.sdc.replay.metamorphic;

import java.util.List;
import java.util.Random;

/**
 * Created by Marco on 2/12/2015.
 */
public class AddNoise {

    public List transform(List<float[]> inputData)
    {
        Random r=new Random();

        for(int i=0; i<inputData.size();i++)
        {
            float[] array = inputData.get(i);
            for(int j =0;j<array.length;j++)
            {
                // array[j] = (float)((r.nextDouble(20.0)-10.0)/10.0);
            }
            inputData.set(i, array);
        }
        return inputData;
    }
}
