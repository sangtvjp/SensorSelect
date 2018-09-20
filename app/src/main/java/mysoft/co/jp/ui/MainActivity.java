package mysoft.co.jp.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

import mysoft.co.jp.R;

import mysoft.co.jp.controller.ConnectBluetoothController;
import mysoft.co.jp.model.DeviceScanModel;
import mysoft.co.jp.util.Const;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private HashMap<Integer, ConnectBluetoothController> bluetoothControllerHashMap;

    @SuppressLint("UseSparseArrays")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        bluetoothControllerHashMap = new HashMap<>();
    }

    private void initView() {
        findViewById(R.id.btn_left_sensor).setOnClickListener(this);
        findViewById(R.id.btn_right_sensor).setOnClickListener(this);
        findViewById(R.id.btn_setting).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left_sensor:
                showAlertDialog(MainActivity.this, Const.TYPE_SENSOR_LEFT);
                break;
            case R.id.btn_right_sensor:
                showAlertDialog(MainActivity.this, Const.TYPE_SENSOR_RIGHT);
                break;
            case R.id.btn_setting:
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void startScanDevice(int typeSensor) {
        Intent intent = new Intent(this, ScanDeviceActivity.class);
        intent.putExtra(Const.TYPE_SENSOR_KEY, typeSensor);
        startActivityForResult(intent, Const.REQUEST_CODE_SCAN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.REQUEST_CODE_SCAN && resultCode == Activity.RESULT_CANCELED) {
         /*   DeviceScanModel deviceScanModel = ((DeviceScanModel) data.getSerializableExtra(Const.DEVICE_NEED_CONNECT_KEY));
            ConnectBluetoothController bluetoothController = new ConnectBluetoothController(this, deviceScanModel);
            if (resultCode == Const.RESULT_CODE_LEFT_SCAN) {
                bluetoothControllerHashMap.put(Const.TYPE_SENSOR_LEFT, bluetoothController);
            } else if (resultCode == Const.RESULT_CODE_RIGHT_SCAN) {
                bluetoothControllerHashMap.put(Const.TYPE_SENSOR_RIGHT, bluetoothController);
            }
            bluetoothController.connect(toString());
        */
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (bluetoothControllerHashMap.get(Const.TYPE_SENSOR_LEFT) != null) {
            bluetoothControllerHashMap.get(Const.TYPE_SENSOR_LEFT).connect(toString());
        }

        if (bluetoothControllerHashMap.get(Const.TYPE_SENSOR_RIGHT) != null) {
            bluetoothControllerHashMap.get(Const.TYPE_SENSOR_RIGHT).connect(toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothControllerHashMap.get(Const.TYPE_SENSOR_LEFT) != null) {
            bluetoothControllerHashMap.get(Const.TYPE_SENSOR_LEFT).connect(toString());
        }

        if (bluetoothControllerHashMap.get(Const.TYPE_SENSOR_RIGHT) != null) {
            bluetoothControllerHashMap.get(Const.TYPE_SENSOR_RIGHT).connect(toString());
        }
        bluetoothControllerHashMap.clear();
    }

    public void showAlertDialog(Context context, final int TYPE_SENSOR) {
        if (bluetoothControllerHashMap.get(TYPE_SENSOR) != null
            &&bluetoothControllerHashMap.get(TYPE_SENSOR).isConnected()) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setMessage(R.string.dialog_message_disconnect_device)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (bluetoothControllerHashMap.get(TYPE_SENSOR) != null) {
                                if (bluetoothControllerHashMap.get(TYPE_SENSOR).isConnected()) {
                                    bluetoothControllerHashMap.get(TYPE_SENSOR).close();
                                }
                                bluetoothControllerHashMap.remove(TYPE_SENSOR);
                            }
                            // mở màn hình right sensor
                           // startScanDevice(TYPE_SENSOR);
                        }
                    })
                    .setPositiveButton("Disconnected", new DialogInterface.OnClickListener() {
                        @Override
                         public void onClick(DialogInterface dialog, int which) {
                            if (bluetoothControllerHashMap.get(TYPE_SENSOR) != null) {
                                if (bluetoothControllerHashMap.get(TYPE_SENSOR).isConnected()) {
                                    bluetoothControllerHashMap.get(TYPE_SENSOR).close();
                                }
                                bluetoothControllerHashMap.remove(TYPE_SENSOR);
                            }
                            dialog.dismiss();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }else{
            startScanDevice(TYPE_SENSOR);

        }
    }
}
