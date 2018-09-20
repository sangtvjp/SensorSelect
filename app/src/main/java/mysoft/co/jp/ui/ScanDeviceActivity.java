package mysoft.co.jp.ui;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mysoft.co.jp.R;
import mysoft.co.jp.controller.ConnectBluetoothController;
import mysoft.co.jp.model.DeviceScanModel;
import mysoft.co.jp.util.Const;

public class ScanDeviceActivity extends AppCompatActivity implements ScanDeviceAdapter.ItemClickListener {

    private static final String TAG = "ScanDeviceActivity";

    private int typeSensor;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothAdapter.LeScanCallback leScanCallback = null;
    private ScanCallback scanCallback = null;

    private ConnectBluetoothController mBluetoothLeService;
    private ScanDeviceAdapter scanDeviceAdapter;
    private ArrayList<DeviceScanModel> deviceScanModels;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_device);

        //check let or right sensor
        typeSensor = getIntent().getIntExtra(Const.TYPE_SENSOR_KEY, -1);
        if (typeSensor == -1) {
            setResult(Const.RESULT_CODE_LEFT_SCAN);
        }
        initView();
        initData();
    }

    private void initView() {
        TextView tvTitleScan = findViewById(R.id.tv_title_scan);
        tvTitleScan.setText(typeSensor == Const.TYPE_SENSOR_LEFT ? R.string.scan_title_left : R.string.scan_title_right);

        RecyclerView recyclerView = findViewById(R.id.recycler_view_scan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        deviceScanModels = new ArrayList<>();
        scanDeviceAdapter = new ScanDeviceAdapter(this, deviceScanModels);
        scanDeviceAdapter.setClickListener(this);
        recyclerView.setAdapter(scanDeviceAdapter);
    }

    private void initData() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            scanCallback = new ScanCallback() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    Log.d(TAG, "onScanResult: ");
                    super.onScanResult(callbackType, result);
                    BluetoothDevice bluetoothDevice = result.getDevice();
                    addDataScan(bluetoothDevice);
                }

                @Override
                public void onBatchScanResults(List<ScanResult> results) {
                    Log.d(TAG, "onBatchScanResults: ");
                    super.onBatchScanResults(results);
                }

                @Override
                public void onScanFailed(int errorCode) {
                    super.onScanFailed(errorCode);
                    Log.d(TAG, "Scanning Failed " + errorCode);
                }
            };
        } else {
            leScanCallback = new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                    Log.d(TAG, "onLeScan: ");
                    addDataScan(device);
                }
            };
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else {
            scanDevice();
        }
    }

    private void addDataScan(BluetoothDevice bluetoothDevice) {
        DeviceScanModel deviceScanModel = new DeviceScanModel();
        deviceScanModel.setDeviceName(bluetoothDevice.getName());
        deviceScanModel.setAddress(bluetoothDevice.getAddress());
        scanDeviceAdapter.addItem(deviceScanModel);
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            scanDevice();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    Const.REQUEST_LOCATION_ENABLE_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case Const.REQUEST_LOCATION_ENABLE_CODE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    scanDevice();
                } else {
                    setResult(typeSensor == Const.TYPE_SENSOR_LEFT ? Const.RESULT_CODE_LEFT_SCAN : Const.RESULT_CODE_RIGHT_SCAN);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothAdapter.getBluetoothLeScanner().stopScan(scanCallback);
        } else {
            mBluetoothAdapter.stopLeScan(leScanCallback);
        }
    }

    private void scanDevice() {
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBtIntent);
            return;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothAdapter.getBluetoothLeScanner().startScan(scanCallback);
        } else {
            mBluetoothAdapter.startLeScan(leScanCallback);
        }
    }

    @Override
    public void onItemClick(View view, DeviceScanModel deviceScanModel) {
        Intent intent = new Intent();
        if (deviceScanModel != null) {
            intent.putExtra(Const.DEVICE_NEED_CONNECT_KEY, deviceScanModel);
        }
        setResult(typeSensor == Const.TYPE_SENSOR_LEFT ? Const.RESULT_CODE_LEFT_SCAN : Const.RESULT_CODE_RIGHT_SCAN, intent);
        showAlertDialog(ScanDeviceActivity.this);
    }

    public void showAlertDialog(Context context) {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(R.string.dialog_message_scan)
                .setCancelable(false)
                .setPositiveButton("Connected", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}

