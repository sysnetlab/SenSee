package sysnetlab.android.sdc.replay.metamorphic;

import java.util.List;
import java.util.Random;

public class AddAvgNoise2 {

    public List transform(List<float[]> inputData) {
        Random r = new Random();
        //float NoiseValue = 0;

        float XTotal = 0;
        int XnumOfDataPoints = 0;
        float XMax = 0;
        float XMin = 100;


        float YTotal = 0;
        int YnumOfDataPoints = 0;
        float YMax = 0;
        float YMin = 100;


        float ZTotal = 0;
        int ZnumOfDataPoints = 0;
        float ZMax = 0;
        float ZMin = 100;



        for (int DataIndex = 0; DataIndex < inputData.size(); DataIndex++) {


            float[] array = inputData.get(DataIndex);

            //-----Start Of X Axis Transfroms-----------------------------------------------------
            //For Loop to total up X Axis Values and number of points in X axis
            for (int j = 0; j < array.length; j += 3) {
                XTotal = XTotal + array[j];
                XnumOfDataPoints++;


                float Xavg = XTotal / XnumOfDataPoints;


                //If Statement to find Max Value of Y values after Yavg is Subtracted
                if (array[j] > XMax) {
                    XMax = array[j];
                }

                //If Statement to find Min Value of Y values after Yavg is Subtracted
                if (array[j] < XMin) {
                    XMin = array[j];
                }


                array[j] = array[j] - Xavg;


                float xd = XMax - XMin;
                double percentage = 0.10;

                //Loop to reassign Averaged Random Values back into the Xaxis values
                //for (int j = 2; j < array.length; j += 3) {
                array[j] += xd * percentage * Math.round(r.nextDouble() * 100) / 100.0;
            }




            //-----Start Of Y Axis Transfroms-----------------------------------------------------
            //For Loop to total up Y Axis Values and number of points in Y axis
            for (int j = 1; j < array.length; j += 3) {
                YTotal = YTotal + array[j];
                YnumOfDataPoints++;


                float Yavg = YTotal / YnumOfDataPoints;


                //If Statement to find Max Value of Y values after Yavg is Subtracted
                if (array[j] > YMax) {
                    YMax = array[j];
                }

                //If Statement to find Min Value of Y values after Yavg is Subtracted
                if (array[j] < YMin) {
                    YMin = array[j];
                }


                array[j] = array[j] - Yavg;


                float yd = YMax - YMin;
                double percentage = 0.10;

                //Loop to reassign Averaged Random Values back into the Yaxis values
                //for (int j = 2; j < array.length; j += 3) {
                array[j] += yd * percentage * Math.round(r.nextDouble() * 100) / 100.0;
            }
            //-----End Of Y Axis Transfroms-----------------------------------------------------







            //-----Start Of Z Axis Transfroms-----------------------------------------------------
            //For Loop to total up Z Axis Values and number of points in Z axis
            for (int j = 2; j < array.length; j += 3) {
                ZTotal = ZTotal + array[j];
                ZnumOfDataPoints++;


                float Zavg = ZTotal / ZnumOfDataPoints;


                //If Statement to find Max Value of Z values after Zavg is Subtracted
                if (array[j] > ZMax) {
                    ZMax = array[j];
                }

                //If Statement to find Min Value of Z values after Zavg is Subtracted
                if (array[j] < ZMin) {
                    ZMin = array[j];
                }


                array[j] = array[j] - Zavg;


                float zd = ZMax - ZMin;
                double percentage = 0.10;

            //Loop to reassign Averaged Random Values back into the Zaxis values
            //for (int j = 2; j < array.length; j += 3) {
                array[j] += zd * percentage * Math.round(r.nextDouble() * 100) / 100.0;
            }
            //-----End Of Z Axis Transfroms-----------------------------------------------------

            inputData.set(DataIndex, array);
        }
        return inputData;
    }
}