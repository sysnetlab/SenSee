package sysnetlab.android.sdc.replay.metamorphic;

import java.util.List;
import java.util.*;


public class NoEffectMetamorphicTransform implements MetamorphicTransform {

    public List<float[]> transform(List<float[]> inputData)
    {
        //just to show how this could look like

        //for(float num : inputData) {
        //    num = num*2;
        //}

        //for(float i = 0; i < inputData.size(); i++){
        //        inputData[i] = inputData[i] * 2;
       // }


        //loop to get the each array of float values of the list

        for(int i=0; i<inputData.size();i++) {
            float[] array = inputData.get(i);
            for(int j =0;j<array.length;j++) {
                array[j] = array[j] *2;
            }
            inputData.set(i, array);
        }
        return inputData;


    }
}
