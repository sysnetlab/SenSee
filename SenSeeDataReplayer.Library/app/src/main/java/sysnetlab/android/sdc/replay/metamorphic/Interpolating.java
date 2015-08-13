package sysnetlab.android.sdc.replay.metamorphic;


import java.util.ArrayList;
import java.util.List;

public class Interpolating {

    public List<float[]> transform(List<float[]> inputData)
    {

        List<float[]> InterpolatedInputData = new ArrayList<float[]>();

        float NextXpoint = 0;
        float NextYpoint = 0;
        float NextZpoint = 0;


        // Get the 1st values for X,Y,Z coordinates
        for(int i=0; i<inputData.size();i++) {
            float[] array = inputData.get(i);

            float Xpoint = array[0];
            float Ypoint = array[1];
            float Zpoint = array[2];


            // Get the 2nd values for X,Y,Z coordinates
                float[] Nextarray = inputData.get(i+1);

                NextXpoint = Nextarray[0];
                NextYpoint = Nextarray[1];
                NextZpoint = Nextarray[2];

            //}


            // Avg The 1st and 2nd Values together
            float XAvg = (Xpoint + NextXpoint)/2;
            float YAvg = (Ypoint + NextYpoint)/2;
            float ZAvg = (Zpoint + NextZpoint)/2;


            // Store averaged values in new array
            float[] AvgArray = {XAvg, YAvg, ZAvg};


            // add original data to return values
            InterpolatedInputData.add(inputData.get(i));

            // add Averaged data to return values
            InterpolatedInputData.add(AvgArray);

            }
            //inputData.set(i, array);

        //return inputData;
        return InterpolatedInputData;

    }
}
