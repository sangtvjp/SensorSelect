package mysoft.co.jp.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import mysoft.co.jp.R;
import mysoft.co.jp.util.Const;
import mysoft.co.jp.util.MyPreferences;

public class SettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private Switch switchAccel;
    private Switch switchGyro;
    private Switch switchPressure;

    private TextView tvCH1;
    private TextView tvCH2;
    private TextView tvCH3;
    private TextView tvCH4;

    private String[] ledSettingValueArray;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ledSettingValueArray = getResources().getStringArray(R.array.arr_led_setting);

        initView();
        initEvent();
        setData();
    }

    private void initView() {
        switchAccel = findViewById(R.id.switch_sensor_setting_accel);
        switchGyro = findViewById(R.id.switch_sensor_setting_gyro);
        switchPressure = findViewById(R.id.switch_sensor_setting_pressure);

        tvCH1 = findViewById(R.id.tv_led_setting_ch1_value);
        tvCH2 = findViewById(R.id.tv_led_setting_ch2_value);
        tvCH3 = findViewById(R.id.tv_led_setting_ch3_value);
        tvCH4 = findViewById(R.id.tv_led_setting_ch4_value);
    }

    private void initEvent() {
        switchAccel.setOnCheckedChangeListener(this);
        switchGyro.setOnCheckedChangeListener(this);
        switchPressure.setOnCheckedChangeListener(this);

        tvCH1.setOnClickListener(this);
        tvCH2.setOnClickListener(this);
        tvCH3.setOnClickListener(this);
        tvCH4.setOnClickListener(this);
    }

    private void setData() {
        switchAccel.setChecked(MyPreferences.getSensorSettingAccel(this));
        switchGyro.setChecked(MyPreferences.getSensorSettingGryo(this));
        switchPressure.setChecked(MyPreferences.getSensorSettingPressure(this));

        tvCH1.setText(getCHValueByType(MyPreferences.getLedSettingCh1(this)));
        tvCH2.setText(getCHValueByType(MyPreferences.getLedSettingCh2(this)));
        tvCH3.setText(getCHValueByType(MyPreferences.getLedSettingCh3(this)));
        tvCH4.setText(getCHValueByType(MyPreferences.getLedSettingCh4(this)));
    }

    private String getCHValueByType(int type) {
        switch (type) {
            case Const.LED_SETTING_OFF:
                return getString(R.string.led_setting_off);
            case Const.LED_SETTING_ON:
                return getString(R.string.led_setting_on);
            case Const.LED_SETTING_PULSE_A:
                return getString(R.string.led_setting_pulse_a);
            case Const.LED_SETTING_PULSE_B:
                return getString(R.string.led_setting_pulse_b);
        }
        return getString(R.string.led_setting_off);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch_sensor_setting_accel:
                MyPreferences.setSensorSettingAccel(SettingActivity.this, isChecked);
                break;
            case R.id.switch_sensor_setting_gyro:
                MyPreferences.setSensorSettingGryo(SettingActivity.this, isChecked);
                break;
            case R.id.switch_sensor_setting_pressure:
                MyPreferences.setSensorSettingPressure(SettingActivity.this, isChecked);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_led_setting_ch1_value:
                showChooseDialogByType(v.getId(), MyPreferences.getLedSettingCh1(SettingActivity.this));
                break;
            case R.id.tv_led_setting_ch2_value:
                showChooseDialogByType(v.getId(), MyPreferences.getLedSettingCh2(SettingActivity.this));
                break;
            case R.id.tv_led_setting_ch3_value:
                showChooseDialogByType(v.getId(), MyPreferences.getLedSettingCh3(SettingActivity.this));
                break;
            case R.id.tv_led_setting_ch4_value:
                showChooseDialogByType(v.getId(), MyPreferences.getLedSettingCh4(SettingActivity.this));
                break;
        }
    }

    private void showChooseDialogByType(final int id, final int checkedItem) {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setSingleChoiceItems(ledSettingValueArray, checkedItem, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if ((item == checkedItem)) {
                    dialog.dismiss();
                    showAlertDialog(SettingActivity.this);
                } else {
                    new ChooseLedSettingAsyncTask(SettingActivity.this, id, item).execute();
                    dialog.dismiss();// dismiss the alertbox after chose option
                }

            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    private void setViewClickItemLed(int id, int item) {
        switch (id) {
            case R.id.tv_led_setting_ch1_value:
                tvCH1.setText(ledSettingValueArray[item]);
                MyPreferences.setLedSettingCh1(this, item);
                break;
            case R.id.tv_led_setting_ch2_value:
                tvCH2.setText(ledSettingValueArray[item]);
                MyPreferences.setLedSettingCh2(this, item);
                break;
            case R.id.tv_led_setting_ch3_value:
                tvCH3.setText(ledSettingValueArray[item]);
                MyPreferences.setLedSettingCh3(this, item);
                break;
            case R.id.tv_led_setting_ch4_value:
                tvCH4.setText(ledSettingValueArray[item]);
                MyPreferences.setLedSettingCh4(this, item);
                break;
        }
    }

    //Thực hiện lệnh khi chọn item giống item cũ
    public void showAlertDialog(Context context) {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(R.string.dialog_message_setting_same)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //Thực hiện lệnh khi chọn item khác item cũ
    // Show progress
    @SuppressLint("StaticFieldLeak")
    class ChooseLedSettingAsyncTask extends AsyncTask<Void, Integer, String> {

        private ProgressDialog mDialog;
        private Context mContext;
        private int id, itemSelected;

        ChooseLedSettingAsyncTask(Context context, int id, int itemSelected) {
            mContext = context;
            this.id = id;
            this.itemSelected = itemSelected;

        }

        protected void onPreExecute() {
            // bắt đầu thực hiện lệnh
            mDialog = new ProgressDialog(mContext);
            mDialog.show();
            super.onPreExecute();
        }

        protected String doInBackground(Void... arg0) {
            // Thực hiện lệnh cần làm trong này
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // thực hiện lệnh xong(cập nhật lại view)
            setViewClickItemLed(id, itemSelected);
            mDialog.dismiss();
        }
    }
}
