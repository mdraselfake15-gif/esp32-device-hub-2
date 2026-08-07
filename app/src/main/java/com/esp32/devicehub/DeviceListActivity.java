package com.esp32.devicehub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class DeviceListActivity extends AppCompatActivity implements DeviceAdapter.Listener {

    public static final String EXTRA_DEVICE_ID = "extra_device_id";
    public static final String EXTRA_DEVICE_NAME = "extra_device_name";
    public static final String EXTRA_DEVICE_URL = "extra_device_url";

    private DeviceStore deviceStore;
    private DeviceAdapter adapter;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        deviceStore = new DeviceStore(this);

        RecyclerView recyclerView = findViewById(R.id.recyclerDevices);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DeviceAdapter(this);
        recyclerView.setAdapter(adapter);

        emptyView = findViewById(R.id.textEmpty);

        FloatingActionButton fab = findViewById(R.id.fabAddDevice);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(DeviceListActivity.this, AddEditDeviceActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        List<Device> devices = deviceStore.getAll();
        adapter.setDevices(devices);
        emptyView.setVisibility(devices.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDeviceClick(Device device) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(EXTRA_DEVICE_NAME, device.name);
        intent.putExtra(EXTRA_DEVICE_URL, device.getUrl());
        startActivity(intent);
    }

    @Override
    public void onDeviceEdit(Device device) {
        Intent intent = new Intent(this, AddEditDeviceActivity.class);
        intent.putExtra(EXTRA_DEVICE_ID, device.id);
        startActivity(intent);
    }

    @Override
    public void onDeviceDelete(Device device) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_device_title)
                .setMessage(getString(R.string.delete_device_message, device.name))
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    deviceStore.delete(device.id);
                    refreshList();
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
