

package com.example.wirelesscrs.screen_record.Recorder;

//Interface for permission result callback
interface PermissionResultListener {
    void onPermissionResult(int requestCode,
                            String permissions[], int[] grantResults);
}
