package com.esp32.devicehub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {

    public interface Listener {
        void onDeviceClick(Device device);
        void onDeviceEdit(Device device);
        void onDeviceDelete(Device device);
    }

    private final List<Device> devices = new ArrayList<>();
    private final Listener listener;

    public DeviceAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setDevices(List<Device> newDevices) {
        devices.clear();
        devices.addAll(newDevices);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_device, parent, false);
        return new DeviceViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        Device device = devices.get(position);
        holder.name.setText(device.name);
        holder.url.setText(device.getUrl());

        holder.itemView.setOnClickListener(v -> listener.onDeviceClick(device));
        holder.editButton.setOnClickListener(v -> listener.onDeviceEdit(device));
        holder.deleteButton.setOnClickListener(v -> listener.onDeviceDelete(device));
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    static class DeviceViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView url;
        ImageButton editButton;
        ImageButton deleteButton;

        DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textDeviceName);
            url = itemView.findViewById(R.id.textDeviceUrl);
            editButton = itemView.findViewById(R.id.buttonEdit);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
