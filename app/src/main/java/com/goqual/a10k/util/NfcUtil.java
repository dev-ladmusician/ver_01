package com.goqual.a10k.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;

/**
 * Created by hanwool on 2017. 2. 27..
 */

public class NfcUtil {

    private static final String[][] techList = new String[][]{
            new String[]{
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(),
                    Ndef.class.getName()
            }
    };


    public static void enableForegroundDispatch(Activity ctx) {
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, new Intent(ctx, ctx.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        // creating intent receiver for NFC events:
        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
        // enabling foreground dispatch for getting intent from NFC event:
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(ctx);
        nfcAdapter.enableForegroundDispatch(ctx, pendingIntent, new IntentFilter[]{filter}, NfcUtil.techList);
    }

    public static void disableForegroundDispatch(Activity ctx) {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(ctx);
        nfcAdapter.disableForegroundDispatch(ctx);
    }
}
