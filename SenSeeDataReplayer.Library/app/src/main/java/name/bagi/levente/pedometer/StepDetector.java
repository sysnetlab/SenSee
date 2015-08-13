/*
 *  Pedometer - Android App
 *  Copyright (C) 2009 Levente Bagi
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

//StepDetection-Explained

package name.bagi.levente.pedometer;

import java.util.ArrayList;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Detects steps and notifies all listeners (that implement StepListener).
 * @author Levente Bagi
 * @todo REFACTOR: SensorListener is deprecated
 */
public class StepDetector implements SensorEventListener
{
    private final static String TAG = "StepDetector";
    private float   mLimit = 10;
    private float   mLastValues[] = new float[3*2];
    private float   mScale[] = new float[2];
    private float   mYOffset;

    private float   mLastDirections[] = new float[3*2];
    private float   mLastExtremes[][] = { new float[3*2], new float[3*2] };
    private float   mLastDiff[] = new float[3*2];
    private int     mLastMatch = -1;

    private ArrayList<StepListener> mStepListeners = new ArrayList<StepListener>();

    public StepDetector() {
        int h = 480; //value used to calculate Earth's Gravity and Magnetic Field
        mYOffset = h * 0.5f;
        mScale[0] = - (h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
        mScale[1] = - (h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
    }

    public void setSensitivity(float sensitivity) {
        //mLimit = sensitivity; // 1.97  2.96  4.44  6.66  10.00  15.00  22.50  33.75  50.62
        mLimit = 17.0f;
    }

    public void addStepListener(StepListener sl) {
        mStepListeners.add(sl);
    }

    public void clearStepListeners() {
        mStepListeners.clear();
    }

    //---------------------------------------------------
    public void onSensorChanged(int sensor, float[] values) {
        synchronized (this) {
            //if the Sensor Type pass in is = to the orientation Sensor -> Do Nothing
            if (sensor == Sensor.TYPE_ORIENTATION) {
            }

            //if the Sensor Type pass in is = to the accelerometer Sensor -> Continue
            else {
                //j = 1 if (sensor == Sensor.TYPE_ACCELEROMETER)
                //j = 0
                int j = (sensor == Sensor.TYPE_ACCELEROMETER) ? 1 : 0;

                if (j == 1) {

                    //Reset vSum to 0
                    float vSum = 0;

                    //Loop to grab the values from the X,Y, and Z accelerometer Data
                    // Values[0] = X Value
                    // Values[1] = Y Value
                    // Values[2] = Z Value
                    for (int i=0 ; i<3 ; i++) {
                        final float v = mYOffset + values[i] * mScale[j];

                        //add X,Y,Z "v" values together
                        vSum += v;
                    }
                    int k = 0;
                    //When Testing insert break point below

                    float v = vSum / 3;

                    //Used to determine if v is on the up slope or down slope
                    float direction = (v > mLastValues[k] ? 1 : (v < mLastValues[k] ? -1 : 0));
                    if (direction == - mLastDirections[k]) {
                        // Direction changed
                        int extType = (direction > 0 ? 0 : 1); // minumum or maximum?
                        mLastExtremes[extType][k] = mLastValues[k];
                        float diff = Math.abs(mLastExtremes[extType][k] - mLastExtremes[1 - extType][k]);

                        if (diff > mLimit) {

                            // Check to see if this Peak is 2 thirds as large as the last one.
                            boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k]*2/3);
                            // Check to see if last peak was large enough to use as a step.
                            boolean isPreviousLargeEnough = mLastDiff[k] > (diff/3);
                            // Check to see that Max and Min direction values did not go past 1 or -1
                            boolean isNotContra = (mLastMatch != 1 - extType);

                            // If all previous conditions are met, count 1 step.
                            if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
                                Log.i(TAG, "step");
                                for (StepListener stepListener : mStepListeners) {
                                    stepListener.onStep();
                                }

                                //reset "extType" to -1
                                mLastMatch = extType;
                            }
                            else {
                                mLastMatch = -1;
                            }
                        }
                        mLastDiff[k] = diff;
                    }
                    mLastDirections[k] = direction;
                    mLastValues[k] = v;
                }
            }
        }
    }

    public void onSensorChanged(SensorEvent event) {
        onSensorChanged(event.sensor.getType(), event.values);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

}