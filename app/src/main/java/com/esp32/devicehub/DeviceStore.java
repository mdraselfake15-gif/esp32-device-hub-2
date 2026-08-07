package com.esp32.devicehub;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class DeviceStore {
    private static final String PREFS_NAME = "esp32_devices_prefs";
    private static final String KEY_DEVICES = "devices_json";

    private final SharedPreferences prefs;

    public DeviceStore(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public List<Device> getAll() {
        List<Device> result = new ArrayList<>();
        String json = prefs.getString(KEY_DEVICES, "[]");
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                result.add(Device.fromJson(arr.getJSONObject(i)));
            }
        } catch (JSONException e) {
        }
        return result;
    }

    public void saveAll(List<Device> devices) {
        JSONArray arr = new JSONArray();
        try {
            for (Device d : devices) {
                arr.put(d.toJson());
            }
        } catch (JSONException e) {
        }
        prefs.edit().putString(KEY_DEVICES, arr.toString()).apply();
    }

    public void add(Device device) {
        List<Device> all = getAll();
        all.add(device);
        saveAll(all);
    }

    public void update(Device device) {
        List<Device> all = getAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).id.equals(device.id)) {
                all.set(i, device);
                break;
            }
        }
        saveAll(all);
    }

    public void delete(String deviceId) {
        List<Device> all = getAll();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).id.equals(deviceId)) {
                all.remove(i);
                break;
            }
        }
        saveAll(all);
    }
}
