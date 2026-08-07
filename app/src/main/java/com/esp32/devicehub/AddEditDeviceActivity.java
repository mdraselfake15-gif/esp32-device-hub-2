package com.esp32.devicehub;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;

public class AddEditDeviceActivity extends AppCompatActivity {

    private EditText inputName;
    private EditText inputIp;
    private Switch switchHttps;

    private DeviceStore deviceStore;
    private String editingDeviceId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_device);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        deviceStore = new DeviceStore(this);

        inputName = findViewById(R.id.inputDeviceName);
        inputIp = findViewById(R.id.inputDeviceIp);
        switchHttps = findViewById(R.id.switchHttps);
        MaterialButton saveButton = findViewById(R.id.buttonSave);

        editingDeviceId = getIntent().getStringExtra(DeviceListActivity.EXTRA_DEVICE_ID);
        if (editingDeviceId != null) {
            setTitle(R.string.edit_device);
            loadExistingDevice(editingDeviceId);
        } else {
            setTitle(R.string.add_device);
        }

        saveButton.setOnClickListener(v -> saveDevice());
    }

    private void loadExistingDevice(String id) {
        for (Device d : deviceStore.getAll()) {
            if (d.id.equals(id)) {
                inputName.setText(d.name);
                inputIp.setText(d.ipAddress);
                switchHttps.setChecked(d.useHttps);
                break;
            }
        }
    }

    private void saveDevice() {
        String name = inputName.getText().toString().trim();
        String ip = inputIp.getText().toString().trim();
        boolean https = switchHttps.isChecked();

        if (TextUtils.isEmpty(name)) {
            inputName.setError(getString(R.string.error_required));
            return;
        }
        if (TextUtils.isEmpty(ip)) {
            inputIp.setError(getString(R.string.error_required));
            return;
        }

        if (editingDeviceId != null) {
            Device updated = new Device(editingDeviceId, name, ip, https);
            deviceStore.update(updated);
            Toast.makeText(this, R.string.device_updated, Toast.LENGTH_SHORT).show();
        } else {
            Device newDevice = Device.create(name, ip, https);
            deviceStore.add(newDevice);
            Toast.makeText(this, R.string.device_added, Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
