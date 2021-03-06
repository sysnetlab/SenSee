/*
 * Copyright (c) 2014, the SenSee authors.  Please see the AUTHORS file
 * for details. 
 * 
 * Licensed under the GNU Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 			http://www.gnu.org/copyleft/gpl.html
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package sysnetlab.android.sdc.ui.fragments;

import java.util.List;

import sysnetlab.android.sdc.sensor.AndroidSensor;
import sysnetlab.android.sdc.sensor.AbstractSensor;
import sysnetlab.android.sdc.sensor.SensorProperty;
import sysnetlab.android.sdc.sensor.audio.AudioRecordParameter;
import sysnetlab.android.sdc.sensor.audio.AudioRecordSettingDataSource;
import sysnetlab.android.sdc.sensor.audio.AudioSensor;
import sysnetlab.android.sdc.ui.UserInterfaceUtils;
import sysnetlab.android.sdc.R;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

public class ExperimentSensorSetupFragment extends Fragment {
    private OnFragmentClickListener mCallback;
    private View mView;
    private AbstractSensor mSensor;
    private List<AudioRecordParameter> mAudioRecordParameters;
    private ListView mListView;

    public interface OnFragmentClickListener {
        public void onBtnSetParameterClicked_SensorSetupFragment(View v, AbstractSensor sensor);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // TODO: handle configuration changes
        AudioRecordSettingDataSource.initializeInstance(getActivity());
        AudioRecordSettingDataSource dbSource = AudioRecordSettingDataSource.getInstance();

        dbSource.open();
        if (!dbSource.isDataSourceReady()) {
            // add progress wheel
            // if false, disable audio recording
            dbSource.prepareDataSource();
        }
        mAudioRecordParameters = dbSource.getAllAudioRecordParameters();
        dbSource.close();

        switch (mSensor.getMajorType()) {
            case AbstractSensor.ANDROID_SENSOR:
                mView = inflater.inflate(R.layout.fragment_sensor_setup, container, false);

                if (mSensor != null) {
                    updateAndroidSensorSetupView(mSensor);
                }
                break;

            case AbstractSensor.AUDIO_SENSOR:
                mView = inflater.inflate(R.layout.fragment_audio_sensor_setup, container, false);

                if (mSensor != null) {
                    AudioRecordParameter param = ((AudioSensor) mSensor).getAudioRecordParameter();
                    Log.d("SensorDataCollector", "AUDIO_SENSOR: (id, resid) = " +
                            param.getSource().getSourceId() + "," +
                            param.getSource().getSourceNameResId());
                    updateAudioSensorSetupView();
                }

                break;

            case AbstractSensor.CAMERA_SENSOR:
                // TODO: camera sensor
                Log.w("SensorDataCollector", "ToDo: Camera sensor.");
                mView = inflater.inflate(R.layout.fragment_sensor_setup, container, false);
                break;
            case AbstractSensor.WIFI_SENSOR:
                // TODO: WiFi sensor
                Log.w("SensorDataCollector", "Todo: WiFi RSSI sensor.");
                mView = inflater.inflate(R.layout.fragment_sensor_setup, container, false);
                break;
            case AbstractSensor.BLUETOOTH_SENSOR:
                // TODO: Bluetooth sensor
                Log.w("SensorDataCollector", "Todo: Bluetooth RSSI sensor.");
                mView = inflater.inflate(R.layout.fragment_sensor_setup, container, false);
                break;
            default:
                Log.e("SensorDataCollector",
                        "Invalid major sensor type = " + mSensor.getMajorType());

                mView = inflater.inflate(R.layout.fragment_sensor_setup, container, false);

                String strWarnMsgFormatter = mView.getResources().getString(
                        R.string.text_sensor_type_x_not_handled);
                String strWargMsg = String.format(strWarnMsgFormatter, mSensor.getMajorType());

                Toast.makeText(getActivity(), strWargMsg, Toast.LENGTH_SHORT).show();
                break;
        }

        return mView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO: handle configuration changes
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnFragmentClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnClickedListener");
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((Button) mView.findViewById(R.id.button_sensor_setup_set_parameter))
                .setOnClickListener(new Button.OnClickListener() {
                    public void onClick(View v) {
                        mCallback.onBtnSetParameterClicked_SensorSetupFragment(v, mSensor);
                    }
                });
    }

    public void setSensor(AbstractSensor sensor) {
        mSensor = sensor;
    }

    private void updateAndroidSensorSetupView(AbstractSensor sensor) {
        TextView tv = (TextView) mView.findViewById(R.id.textview_sensor_setup_sensor_name);
        tv.setText(sensor.getName());

        Log.d("SensorDataCollector", "Android sensor.");

        UserInterfaceUtils
                .fillSensorProperties(getActivity(), (ListView) mView
                        .findViewById(R.id.listview_sensor_setup_sensor_properties), sensor);

        EditText etSamplingRate = (EditText) mView
                .findViewById(R.id.edittext_sensor_steup_sampling_rate);
        Button btnSetSamplingRate = (Button) mView
                .findViewById(R.id.button_sensor_setup_set_parameter);
        btnSetSamplingRate.setVisibility(View.VISIBLE);
        TextView tvSamplingRate = (TextView) mView
                .findViewById(R.id.textview_sensor_setup_sampling_rate);

        if (((AndroidSensor) sensor).isStreamingSensor()) {
            etSamplingRate.setText(String.valueOf(1000000. / ((AndroidSensor) sensor)
                    .getSamplingInterval()));
            etSamplingRate.setEnabled(true);
            tvSamplingRate.setEnabled(true);
            etSamplingRate.setVisibility(View.VISIBLE);
            tvSamplingRate.setVisibility(View.VISIBLE);
            btnSetSamplingRate.setVisibility(View.VISIBLE);
            Log.d("SensorDataCollector", "Streaming sensor.");
        } else {
            etSamplingRate.setText(getActivity().getResources().getString(
                    R.string.text_not_applicable));
            etSamplingRate.setEnabled(false);
            tvSamplingRate.setEnabled(false);
            etSamplingRate.setVisibility(View.GONE);
            tvSamplingRate.setVisibility(View.GONE);
            btnSetSamplingRate.setVisibility(View.GONE);
            Log.d("SensorDataCollector", "Non-streaming (onchange) sensor.");
        }
    }

    private void updateAudioSensorSetupView() {
        Button btnSetSamplingRate = (Button) mView
                .findViewById(R.id.button_sensor_setup_set_parameter);
        btnSetSamplingRate.setVisibility(View.GONE);

        TextView tv = (TextView) mView.findViewById(R.id.textview_sensor_setup_sensor_name);
        tv.setText(mSensor.getName());

        mListView = (ListView) mView.findViewById(R.id.listview_audio_sensor_setup);

        UserInterfaceUtils.fillSensorProperties(getActivity(), mListView, mSensor);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                SensorProperty property = (SensorProperty) listView.getItemAtPosition(position);

                if (property.getName().equals(
                        getActivity().getResources().getString(R.string.text_audio_source))) {
                    showAudioSensorDialog(mAudioRecordParameters,
                            ((AudioSensor) mSensor).getAudioRecordParameter(),
                            AudioSensorSetupDialogFragment.SELECT_SOURCE);
                } else if (property.getName().equals(
                        getActivity().getResources().getString(R.string.text_audio_channel_in))) {
                    showAudioSensorDialog(mAudioRecordParameters,
                            ((AudioSensor) mSensor).getAudioRecordParameter(),
                            AudioSensorSetupDialogFragment.SELECT_CHANNEL_IN);
                } else if (property.getName().equals(
                        getActivity().getResources().getString(R.string.text_audio_encoding))) {
                    showAudioSensorDialog(mAudioRecordParameters,
                            ((AudioSensor) mSensor).getAudioRecordParameter(),
                            AudioSensorSetupDialogFragment.SELECT_ENCODING);
                } else if (property.getName().equals(
                        getActivity().getResources().getString(R.string.text_audio_sampling_rate))) {
                    showAudioSensorDialog(mAudioRecordParameters,
                            ((AudioSensor) mSensor).getAudioRecordParameter(),
                            AudioSensorSetupDialogFragment.SELECT_SAMPLING_RATE);
                } else if (property.getName()
                        .equals(getActivity().getResources().getString(
                                R.string.text_audio_min_buffer_size))) {
                    showAudioSensorDialog(mAudioRecordParameters,
                            ((AudioSensor) mSensor).getAudioRecordParameter(),
                            AudioSensorSetupDialogFragment.SELECT_MIN_BUFFER_SIZE);
                }
            }
        });
    }

    private void showAudioSensorDialog(List<AudioRecordParameter> allParams,
            AudioRecordParameter param, int operation) {
        DialogFragment newFragment = AudioSensorSetupDialogFragment.newInstance(getActivity(),
                allParams, param, mListView, operation);

        final FragmentManager fm = getActivity().getSupportFragmentManager();
        newFragment.show(fm, "dialog");
    }

}
