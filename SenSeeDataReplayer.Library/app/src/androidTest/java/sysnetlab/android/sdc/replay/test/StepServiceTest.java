package sysnetlab.android.sdc.replay.test;

import java.io.InputStream;
import java.util.List;

import name.bagi.levente.pedometer.StepDetector;
import name.bagi.levente.pedometer.StepListener;
import name.bagi.levente.pedometer.StepService;
import sysnetlab.android.sdc.replay.MockSensingContext;
import sysnetlab.android.sdc.replay.SenSeeSensingReplayer;
import sysnetlab.android.sdc.replay.SensingReplayer;
import sysnetlab.android.sdc.replay.metamorphic.AddAvgNoise2;
import sysnetlab.android.sdc.replay.metamorphic.ConvertToRoundedNoise;
import sysnetlab.android.sdc.replay.metamorphic.BaseLineShift;
import sysnetlab.android.sdc.replay.metamorphic.DownSample;
import sysnetlab.android.sdc.replay.metamorphic.InsertNoise2;
import sysnetlab.android.sdc.replay.metamorphic.Interpolating;
import sysnetlab.android.sdc.replay.metamorphic.MultiplyByTwo;
import sysnetlab.android.sdc.replay.metamorphic.MultiplyZAxisByTwo;
import sysnetlab.android.sdc.replay.metamorphic.NoEffectMetamorphicTransform;
import sysnetlab.android.sdc.replay.metamorphic.Semantic;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.test.ServiceTestCase;

public class StepServiceTest 
		extends ServiceTestCase<StepService> {

    private int stepsCountedByStepDetector = 0;

	public StepServiceTest() 
	{
		super(StepService.class);
	}

	public void setUp() throws Exception {
	}

	public void tearDown() throws Exception {
	}
	
	public void testReplayInStepService() throws Exception 
	{
        //set up mock context to capture the application's sensor data listener
		MockSensingContext mockContext = new MockSensingContext(getContext());
        setContext(mockContext);

        //start the StepService
        startService(new Intent());     
        StepService stepService = (StepService) getService();
        assertNotNull(stepService);      

        //set up the replayer, using files from SenSee and the captured listener
        SensorEventListener capturedListener = mockContext.getCapturedEventListener();
        assertNotNull(capturedListener);
        StepDetector stepDetector = (StepDetector) capturedListener;  //-----------------------------
        assertNotNull(stepDetector);

        Context testContext = (Context)getClass().getMethod("getTestContext").invoke(this);

        //List of commented out Test Data we Tired

        //InputStream dataStream = testContext.getAssets().open("accelerometer.csv");
        //InputStream dataStream = testContext.getAssets().open("50Stepsv2.csv");
        //InputStream dataStream = testContext.getAssets().open("Marcos50Steps.csv");
        //InputStream dataStream = testContext.getAssets().open("Macos50Steps2.csv");

        //InputStream dataStream = testContext.getAssets().open("Marcos50StepsV3.csv");
        //InputStream dataStream = testContext.getAssets().open("50StepsEveryStep.csv");
        //InputStream dataStream = testContext.getAssets().open("50StepsPocket.csv");
        //InputStream dataStream = testContext.getAssets().open("Marcos10Steps.csv");

        InputStream dataStream = testContext.getAssets().open("Marcos10Hip.csv");
        //InputStream dataStream = testContext.getAssets().open("Marcos50Hip.csv");
        //InputStream dataStream = testContext.getAssets().open("Marcos50ForArm.csv");

        //InputStream dataStream = testContext.getAssets().open("CeCe50StepsAncle.csv");
        //InputStream dataStream = testContext.getAssets().open("CeCe100StepsHip.csv");
        //InputStream dataStream = testContext.getAssets().open("Cece50StepsV2.csv");
        //InputStream dataStream = testContext.getAssets().open("Marcos60StepsHip.csv");

        //InputStream dataStream = testContext.getAssets().open("Cece50StepsHipS3.csv");


        int stepsInSenSeeExperiment = 50;

        SensingReplayer replayer = new SenSeeSensingReplayer(dataStream, capturedListener);

        //set up a step listener to detect when the application counts a step
        stepDetector.clearStepListeners();
        stepDetector.addStepListener(new StepListener() {
            @Override
            public void onStep() {
                stepsCountedByStepDetector++;
            }

            @Override
            public void passValue() {
                //do nothing
            }
        });

        int TransformID = 10;


        //replay

        List<float[]> allSensorValues = replayer.getAllSensorValuesForReplay();
        replayInStepDetectorAndAssert(stepDetector, stepsInSenSeeExperiment, allSensorValues);

        //List of Transform Functions Below

        if (TransformID == 1){
            //transform and replay again--------------------
            NoEffectMetamorphicTransform transformOne = new NoEffectMetamorphicTransform();
            List<float[]> morphedSensorValues = transformOne.transform(allSensorValues);
            replayInStepDetectorAndAssert(stepDetector, stepsInSenSeeExperiment, morphedSensorValues);
        }

        if (TransformID == 2) {
            // This Transform Multiples all X,Y,Z coordinate values by a Factor of 2
            MultiplyByTwo transformTwo = new MultiplyByTwo();
            List<float[]> morphedSensorValues = transformTwo.transform(allSensorValues);
            replayInStepDetectorAndAssert(stepDetector, stepsInSenSeeExperiment, morphedSensorValues);
        }

        if (TransformID == 3) {
            // This Transform Multiples all Z coordinate values by a Factor of 2
            MultiplyZAxisByTwo transformThree = new MultiplyZAxisByTwo();
            List<float[]> morphedSensorValues = transformThree.transform(allSensorValues);
            replayInStepDetectorAndAssert(stepDetector, stepsInSenSeeExperiment, morphedSensorValues);
        }

        if (TransformID == 4) {
            // This Transform Adds a noise value at complete random inserted in between every
            // value
            InsertNoise2 transformFour = new InsertNoise2();
            List<float[]> morphedSensorValues = transformFour.transform(allSensorValues);
            replayInStepDetectorAndAssert(stepDetector, stepsInSenSeeExperiment, morphedSensorValues);
        }

        if (TransformID == 5) {
            // This Transform modifies all the existing array values by converting them to some value
            // + or 1 from the original data
            ConvertToRoundedNoise transformFive = new ConvertToRoundedNoise();
            List<float[]> morphedSensorValues = transformFive.transform(allSensorValues);
            replayInStepDetectorAndAssert(stepDetector, stepsInSenSeeExperiment, morphedSensorValues);
        }

        if (TransformID == 6) {
            // This Transform create inputs that are have the same “meaning” as the original
            Semantic transformSix = new Semantic();
            List<float[]> morphedSensorValues = transformSix.transform(allSensorValues);
            replayInStepDetectorAndAssert(stepDetector, stepsInSenSeeExperiment, morphedSensorValues);
        }

        if (TransformID == 7) {
            // Transform that adds average noise of each axis to each data point in the axis family
            AddAvgNoise2 transformSeven = new AddAvgNoise2();
            List<float[]> morphedSensorValues = transformSeven.transform(allSensorValues);
            replayInStepDetectorAndAssert(stepDetector, stepsInSenSeeExperiment, morphedSensorValues);
        }

        if (TransformID == 8){
            // This Transform down sizes the array by deleting a certain percentage of
            // the values
            DownSample transformSeven = new DownSample();
            List<float[]> morphedSensorValues = transformSeven.transform(allSensorValues);
            replayInStepDetectorAndAssert(stepDetector, stepsInSenSeeExperiment, morphedSensorValues);
        }

        if (TransformID == 9){
            //insert a data point in between two adjacent data points
            Interpolating transformEight = new Interpolating();
            List<float[]> morphedSensorValues = transformEight.transform(allSensorValues);
            replayInStepDetectorAndAssert(stepDetector, stepsInSenSeeExperiment, morphedSensorValues);
        }

        else{
            // This Transform moves the base line of the input data based on defined Rise and
            // Run values
            BaseLineShift transformNine = new BaseLineShift();
            List<float[]> morphedSensorValues = transformNine.transform(allSensorValues);
            replayInStepDetectorAndAssert(stepDetector, stepsInSenSeeExperiment, morphedSensorValues);
        }


        // Next transform Goes here


	}

    private void replayInStepDetectorAndAssert(StepDetector stepDetector,
                                               int stepsInSenSeeExperiment,
                                               List<float[]> morphedSensorValues) {

        for(float[] sensorValues : morphedSensorValues)
        {
            stepDetector.onSensorChanged(Sensor.TYPE_ACCELEROMETER, sensorValues);
        }

        //-----Code to Output Morphed Values so they can be graphed--------------------

         float[] morphedArrX = new float[morphedSensorValues.size()];
         for (int i = 0; i < morphedSensorValues.size(); i++) {
                morphedArrX[i] = morphedSensorValues.get(i)[0];
            }

        float[] morphedArrY = new float[morphedSensorValues.size()];
        for (int i = 0; i < morphedSensorValues.size(); i++) {
            morphedArrY[i] = morphedSensorValues.get(i)[1];
        }

        float[] morphedArrZ = new float[morphedSensorValues.size()];
        for (int i = 0; i < morphedSensorValues.size(); i++) {
            morphedArrZ[i] = morphedSensorValues.get(i)[2];
        }

        //----End of Graphing Code-----------------------------------------------------

        //Used for Debug Window testing so you can see what your transfroms are doing
        double errorThreshold = (stepsInSenSeeExperiment * .9); //10% accuracy.

        //Used in actual App testing. We want our accuracy to be at least 80%
        //double errorThreshold = (stepsInSenSeeExperiment * .2); //80% accuracy.
        assertTrue(Math.abs(stepsCountedByStepDetector - stepsInSenSeeExperiment) <= errorThreshold);
        stepsCountedByStepDetector = 0;
    }

}
