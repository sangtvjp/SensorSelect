package mysoft.co.jp.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v4.widget.SwipeRefreshLayout;


import java.util.ArrayList;

import mysoft.co.jp.R;
import mysoft.co.jp.model.DeviceScanModel;

public class ScanDeviceAdapter extends RecyclerView.Adapter<ScanDeviceAdapter.ViewHolder> {

    private static final String TAG = "ScanDeviceAdapter";
    private ArrayList<DeviceScanModel> deviceScanModels;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    ScanDeviceAdapter(Context context, ArrayList<DeviceScanModel> deviceScanModels) {
        this.mInflater = LayoutInflater.from(context);
        this.deviceScanModels = deviceScanModels;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_scan_device, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DeviceScanModel deviceScanModel = deviceScanModels.get(position);
        if(TextUtils.isEmpty(deviceScanModel.getDeviceName())){
                holder.tvDeviceName.setText("N/A");
            }else{
                holder.tvDeviceName.setText(deviceScanModel.getDeviceName());
            }
        holder.tvDeviceAddress.setText(deviceScanModels.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return deviceScanModels.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDeviceName;
        TextView tvDeviceAddress;
        ImageView imvCheckbox;
        ImageView imvDevice;

        ViewHolder(View itemView) {
            super(itemView);
            tvDeviceAddress = itemView.findViewById(R.id.tv_device_addres);
            tvDeviceName = itemView.findViewById(R.id.tv_device_name);
            imvDevice = itemView.findViewById(R.id.imv_icon_device);
            imvCheckbox = itemView.findViewById(R.id.imv_checkbox);
            itemView.setOnClickListener(this);
            imvCheckbox.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imv_checkbox:
                    imvCheckbox.setImageResource(R.drawable.ic_check);
                    if (mClickListener != null)
                        mClickListener.onItemClick(view, deviceScanModels.get(getAdapterPosition()));
                    break;
                default:
                    showAlertDialog(view.getContext());
                    break;
            }
        }
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, DeviceScanModel deviceScanModel);
    }


    public void addItem(DeviceScanModel deviceScanModel) {
        if (!isExit(deviceScanModel)) {
            deviceScanModels.add(deviceScanModel);
            notifyDataSetChanged();

        } else {
            Log.d(TAG, "addItem Address: "+ deviceScanModel.getAddress());
        }
    }

    private boolean isExit(DeviceScanModel deviceScanModel) {
        for (int i = 0; i < deviceScanModels.size(); i++) {
            if (deviceScanModels.get(i).getAddress().equals(deviceScanModel.getAddress())) {
                return true;
            }
        }
        return false;
    }

    public void showAlertDialog(Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(R.string.dialog_message_click_fail)
                .setCancelable(false)
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}