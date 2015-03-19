package sysnetlab.android.sdc.replay.metamorphic;

import java.util.ArrayList;
import java.util.List;


public class BaseLineShift {

    public List<float[]> transform(List<float[]> inputData)
    {

        List<float[]> BaseLineInputData = new ArrayList<float[]>();

        float SlopeRise = 1;
        float SlopeRun = 10;

        float RateOfChange = SlopeRise / SlopeRun;

        float SumOfChange = RateOfChange;


            for(int i=0; i<inputData.size();i++) {
                float[] array = inputData.get(i);

                float NewXpoint = array[0] + SumOfChange;
                float NewYpoint = array[1] + SumOfChange;
                float NewZpoint = array[2] + SumOfChange;


                // Store Moved X,Y,Z vales into new array
                float[] MovedArray = {NewXpoint, NewYpoint, NewZpoint};

                //Increment SumOfChange for the Next X,Y,Z Points
                SumOfChange = SumOfChange + RateOfChange;

                // Moved date points into the return list
                BaseLineInputData.add(MovedArray);

                }
                //inputData.set(i, array);

            return  BaseLineInputData;

    }
}
