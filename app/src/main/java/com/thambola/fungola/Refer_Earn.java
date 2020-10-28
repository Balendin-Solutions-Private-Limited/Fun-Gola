package com.thambola.fungola;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.thambola.fungola.Utils.Constants;
import com.thambola.fungola.Utils.Tools;
import com.thambola.fungola.Utils.Util;

public class Refer_Earn extends AppCompatActivity {

    private TextView tv_ReferalId;
    Button share_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.referenearn);

        Tools.setSystemBarColor(this, R.color.theme_color1);

        tv_ReferalId=findViewById(R.id.tv_ReferalId);
        share_code=findViewById(R.id.sharecode);

        final String ReferralCodeToBeShared= Util.getStringFromSP(this, Constants.ReferralCodeToBeShared);
        final String name= Util.getStringFromSP(this,Constants.FirstName);

        tv_ReferalId.setText("#"+ReferralCodeToBeShared);

        share_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String content=""+name+" Invited you to play FunTime Game with 75 FREE Coins. Use Referal Code '"
                        +ReferralCodeToBeShared+"' while creating your account. On Successfull account creation you will get 75 FREE Coins and your " +
                        "friend also will get 75 FREE Coins. Join now to play with your friends and family.\n\n Happy Gaming. \n See you soon on FunTime Game. \n\n Install App From Below Link";
                String shareBody = content+" \n\n https://play.google.com/store/apps/details?id="+ getPackageName();
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
               //
                // sharingIntent.putExtra(Intent.EXTRA_SUBJECT, content);

                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });


    }

}
