package com.example.advvibration;

import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.os.VibratorManager;
import android.content.Context;
import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

public class AdvVibration extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext cb) throws JSONException {
        if (!"vibratePattern".equals(action)) return false;

        JSONArray arr = args.getJSONArray(0);
        final long[] timings = new long[arr.length()];
        for (int i=0;i<arr.length();i++) timings[i] = Math.max(0, arr.getInt(i));

        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Context ctx = cordova.getContext();
                    Vibrator vibrator;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        VibratorManager vm = (VibratorManager) ctx.getSystemService(Context.VIBRATOR_MANAGER_SERVICE);
                        vibrator = vm.getDefaultVibrator();
                    } else {
                        vibrator = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);
                    }
                    if (vibrator == null || !vibrator.hasVibrator()) {
                        cb.error("No vibrator");
                        return;
                    }

                    if (timings.length == 1) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(timings[0], VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            vibrator.vibrate(timings[0]);
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            // Indique l'amplitude: vibre sur indices pairs, silence sur impairs
                            int[] amps = new int[timings.length];
                            for (int i=0;i<amps.length;i++) amps[i] = (i % 2 == 0) ? 255 : 0;
                            VibrationEffect effect = VibrationEffect.createWaveform(timings, amps, -1);
                            vibrator.vibrate(effect);
                        } else {
                            vibrator.vibrate(timings, -1);
                        }
                    }
                    cb.success();
                } catch (Exception e) {
                    cb.error(e.getMessage());
                }
            }
        });
        return true;
    }
}
