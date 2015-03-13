package sysnetlab.android.sdc.replay.metamorphic;

import org.mockito.internal.util.collections.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class DownSample {

    public List<float[]> transform(List<float[]> inputData) {

        //int ZnumOfDataPoints = 0;
        List<float[]> downSampledInputData = new ArrayList<float[]>();

        int downSample = 50; //Note: 50 means 50% downSample

        int downSampleRate = 100 / downSample;

        for (int i = 0; i < inputData.size(); i++) {
            float[] array = inputData.get(i);

            //for (int j = 2;j<array.length;j+=3) {

                if (((i + 1) % downSampleRate) != 0) {

                    downSampledInputData.add(inputData.get(i));

                    //array =(float[]) ArrayUtils.removeElement(array, j);
                    // array.splice(j, 1);

                    //int numElts = array.length - ( j + 1 ) ;
                    //System.arraycopy( array, j + 1, array, j, numElts ) ;

                    //float TempArray[] = new float[array.length - 1];
                    //TempArray[0] = array[0];
                    //TempArray[1] = array[1];

                    //array[0] = 0;
                    //array[1] = 0;
                    //array[2] = 0;

                    //array = TempArray;
                }


            //inputData.set(i, array);
        }
        return downSampledInputData;


    }

}
