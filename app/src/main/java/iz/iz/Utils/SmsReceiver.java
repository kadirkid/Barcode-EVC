package iz.iz.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import iz.iz.R;

/**
 * Created by abdulahiosoble on 2/13/18.
 */

public class SmsReceiver extends BroadcastReceiver {
//    private MessageDatabase messageDatabase;
//    private MessageAdapter mAdapter;
    private static SmsListener mListener;
    final SmsManager sms = SmsManager.getDefault();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SMS LISTENER",
                "in Receiver. intent.getAction():" + intent.getAction());

//        messageDatabase = new MessageDatabase(context);
//        mAdapter = new MessageAdapter(context);
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            // Get the SMS message received
//            messageDatabase.open();
            Bundle bundle = intent.getExtras();
            try {
                if (bundle != null) {
                    // A PDU is a "protocol data unit". This is the industrial standard for SMS message
                    final Object[] pdusObj = (Object[]) bundle.get("pdus");
                    for (int i = 0; i < pdusObj.length; i++) {
                        // This will create an SmsMessage object from the received pdu
                        SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        // Get sender phone number
                        String phoneNumber = sms.getDisplayOriginatingAddress();
                        String sender = phoneNumber;
                        String mes = sms.getDisplayMessageBody();
                        String formattedText = String.format(context.getResources().getString(R.string.sms_message), sender, mes);
                        // Display the SMS message in a Toast
//                        Toast.makeText(context, mes, Toast.LENGTH_LONG).show();

//                        addToDatabase(sender, mes, messageDatabase, context);

                        mListener.messageReceived(mes);
                        Log.i(sender, mes);
                    }
//                    messageDatabase.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
