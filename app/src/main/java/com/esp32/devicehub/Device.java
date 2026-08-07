package com.esp32.devicehub;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class Device {
    public String id;
    public String name;
    public String ipAddress;
    public boolean useHttps;

    public Device(String id, String name, String ipAddress, boolean useHttps) {
        this.id = id;
        this.name = name;
        this.ipAddress = ipAddress;
        this.useHttps = useHttps;
    }

    public static Device create(String name, String ipAddress, boolean useHttps) {
        return new Device(UUID.randomUUID().toString(), name, ipAddress, useHttps);
    }

    public String getUrl() {
        String scheme = useHttps ? "https://" : "http://";
        String ip = ipAddress.trim();
        ip = ip.replaceFirst("^https?://", "");
        if (ip.endsWith("/")) {
            ip = ip.substring(0, ip.length() - 1);
        }
        return scheme + ip;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject o = new JSONObject();
        o.put("id", id);
        o.put("name", name);
        o.put("ipAddress", ipAddress);
        o.put("useHttps", useHttps);
        return o;
    }

    public static Device fromJson(JSONObject o) throws JSONException {
        return new Device(
                o.getString("id"),
                o.getString("name"),
                o.getString("ipAddress"),
                o.optBoolean("useHttps", false)
        );
    }
                              }
