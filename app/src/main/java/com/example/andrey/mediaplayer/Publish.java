 package com.example.andrey.mediaplayer;

 import android.app.Activity;
 import android.content.Intent;
 import android.os.Bundle;
 import android.widget.Toast;

 import com.vk.sdk.VKAccessToken;
 import com.vk.sdk.VKCallback;
 import com.vk.sdk.VKScope;
 import com.vk.sdk.VKSdk;
 import com.vk.sdk.api.VKError;
 import com.vk.sdk.util.VKUtil;

 import java.util.Arrays;

 /**
 * Created by Romanosov on 25.02.2016.
 */



public class Publish extends Activity {

     private String[] scope = new String[]{VKScope.WALL};

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         VKSdk.initialize(this);
         super.onCreate(savedInstanceState);
         setContentView(R.layout.vk_publish);
         String[] finpr = VKUtil.getCertificateFingerprint(this, this.getPackageName());
         System.out.println(Arrays.asList(finpr));
         VKSdk.login(this, scope);
     }

     @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
             @Override
             public void onResult(VKAccessToken res) {
                 Toast.makeText(getApplicationContext(), "Успешная авторизация", Toast.LENGTH_SHORT).show();
             }
             @Override
             public void onError(VKError error) {
                 Toast.makeText(getApplicationContext(), "Ошибка авторизации", Toast.LENGTH_SHORT).show();
             }
         })) {
             super.onActivityResult(requestCode, resultCode, data);
         }
     }
 }