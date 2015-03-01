package sysnetlab.android.sdc.replay.metamorphic;

import java.util.List;
import java.util.Random;

public class AddAvgNoise {

    public List transform(List<float[]> inputData)
    {
        Random r=new Random();
        //float NoiseValue = 0;

        for(int DataIndex=0; DataIndex<inputData.size();DataIndex++)
        {
            float ZTotal = 0;
            int ZnumOfDataPoints = 0;

            float[] array = inputData.get(DataIndex);

            //For Loop to total up Z Axis Values and number of points in Z axis
            for (int j = 2;j<array.length;j+=3)
            {
                ZTotal = ZTotal + array[j];
                ZnumOfDataPoints ++;
            }

            float Zavg = ZTotal/ZnumOfDataPoints;
            float ZMax = 0;
            float ZMin = 0;

            //For Loop to subtract Avg Noise from Z Values
            for (int j = 2;j<array.length;j+=3)
            {
                array[j]= array[j] - Zavg;

                // If statement to insure array does not go out of bounds
                // Note: Index 5 is the location of the 2nd Z Axis Value
                if(array[j] >= 5){

                    //If Statement to find Max Value of Z values after Zavg is Subtracted
                    if(array[j] > array[j-3])
                    {
                        array[j] = ZMax;
                    }

                    //If Statement to find Min Value of Z values after Zavg is Subtracted
                    if(array[j] < array[j-3])
                    {
                        array[j] = ZMin;
                    }

                }
            }

            float d = ZMax - ZMin;
            double percentage = 0.10;

            //Loop to reassign Averaged Random Values back into the Zaxis values
            for (int j = 2;j<array.length;j+=3)
            {
                array[j]+= d * percentage * Math.round(r.nextDouble()*100)/100.0;
            }

            inputData.set(DataIndex, array);
        }
        return inputData;
    }
}
