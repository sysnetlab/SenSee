package sysnetlab.android.sdc.replay.metamorphic;

import java.util.List;
import java.util.Random;


public class InsertNoise {

    public List transform(List<float[]> inputData)
    {
        Random r=new Random();
        float NoiseValue = 0;

        for(int DataIndex=0; DataIndex<inputData.size();DataIndex++)
        {
            float[] array = inputData.get(DataIndex);
            for(int j =0;j<array.length;j++)
            {
                //array[j] = (float)((r.nextDouble(20.0)-10.0)/10.0);
                NoiseValue = (r.nextFloat() * 2 - 1.0f) * 20.f;

                // initialize new array with size of current array plus room for new element
                float[] newArray = new float[inputData.size() + 1];

                // loop until we reach point of insertion of new element
                // copy the value from the same position in old array over to
                // same position in new array
                for(int i = 0; i < j; i++)
                {
                    newArray[i] = array[i];
                }
                j = j + 1; // move to position to insert new value

                newArray[j] = NoiseValue; // insert noise value

                // loop until you reach the length of the old array
                while(j < array.length)
                {
                    newArray[j] = array[j-1];
                }

                // finally copy last value over
                newArray[j + 1] = array[j];
            }
            inputData.set(DataIndex, array);
        }
        return inputData;
    }
}
