package sysnetlab.android.sdc.replay.metamorphic;

import org.mockito.internal.util.collections.ArrayUtils;

import java.util.List;

public class DownSample {

    public List<float[]> transform(List<float[]> inputData) {

        int ZnumOfDataPoints = 0;
        double downSample = 5; //Note: 5 = 5% downSample

        double downSampleRate = 100 - downSample;

        for (int i = 0; i < inputData.size(); i++) {
            float[] array = inputData.get(i);

            // Down Sample Z Values
            for (int j = 2;j<array.length;j+=3){
                ZnumOfDataPoints++;

                if(ZnumOfDataPoints % downSampleRate == 0){

                    array =(float[]) ArrayUtils.removeElement(array, j);
                }
            }


            inputData.set(i, array);
        }
        return inputData;

    }

}
