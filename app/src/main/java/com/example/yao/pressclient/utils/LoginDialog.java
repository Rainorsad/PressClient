package com.example.yao.pressclient.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.yao.pressclient.R;

/**
 * Created by Yao on 2016/9/22.
 */
public class LoginDialog {
    private static Dialog dialog;
    private static ImageView imageView;
    public static void show(Context context){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v =layoutInflater.inflate(R.layout.dialog_item,null);
        imageView = (ImageView) v.findViewById(R.id.item_dialog_img);
        dialog = new Dialog(context, R.style.MyDialog);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.CENTER;
        window.setAttributes(layoutParams);
        dialog.setContentView(v,layoutParams);
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.roat_style);
        LinearInterpolator lir = new LinearInterpolator();
        animation.setInterpolator(lir);
        animation.setFillAfter(true);
        imageView.setAnimation(animation);
        animation.start();
        dialog.show();
    }

    public static void dismiss(){
        if (dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }
}
