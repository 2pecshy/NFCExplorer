package com.lolo.nfcexplorer.nfc_service;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nxp.nfclib.CardType;
import com.nxp.nfclib.KeyType;
import com.nxp.nfclib.NxpNfcLib;
import com.nxp.nfclib.classic.ClassicFactory;
import com.nxp.nfclib.classic.IMFClassic;
import com.nxp.nfclib.classic.IMFClassicEV1;
import com.nxp.nfclib.desfire.DESFireFactory;
import com.nxp.nfclib.desfire.DESFireFile;
import com.nxp.nfclib.desfire.EV1ApplicationKeySettings;
import com.nxp.nfclib.desfire.IDESFireEV1;
import com.nxp.nfclib.desfire.IDESFireEV2;
import com.nxp.nfclib.exceptions.NxpNfcLibException;
import com.nxp.nfclib.icode.ICodeFactory;
import com.nxp.nfclib.icode.IICodeDNA;
import com.nxp.nfclib.icode.IICodeSLI;
import com.nxp.nfclib.icode.IICodeSLIL;
import com.nxp.nfclib.icode.IICodeSLIS;
import com.nxp.nfclib.icode.IICodeSLIX;
import com.nxp.nfclib.icode.IICodeSLIX2;
import com.nxp.nfclib.icode.IICodeSLIXL;
import com.nxp.nfclib.icode.IICodeSLIXS;
import com.nxp.nfclib.interfaces.IKeyData;
import com.nxp.nfclib.ntag.INTAGI2Cplus;
import com.nxp.nfclib.ntag.INTag203x;
import com.nxp.nfclib.ntag.INTag210;
import com.nxp.nfclib.ntag.INTag210u;
import com.nxp.nfclib.ntag.INTag213215216;
import com.nxp.nfclib.ntag.INTag213F216F;
import com.nxp.nfclib.ntag.INTagI2C;
import com.nxp.nfclib.ntag.NTagFactory;
import com.nxp.nfclib.plus.IPlus;
import com.nxp.nfclib.plus.IPlusEV1SL0;
import com.nxp.nfclib.plus.IPlusEV1SL1;
import com.nxp.nfclib.plus.IPlusEV1SL3;
import com.nxp.nfclib.plus.IPlusSL0;
import com.nxp.nfclib.plus.IPlusSL1;
import com.nxp.nfclib.plus.IPlusSL3;
import com.nxp.nfclib.plus.PlusFactory;
import com.nxp.nfclib.plus.PlusSL1Factory;
import com.nxp.nfclib.ultralight.IUltralight;
import com.nxp.nfclib.ultralight.IUltralightC;
import com.nxp.nfclib.ultralight.IUltralightEV1;
import com.nxp.nfclib.ultralight.IUltralightNano;
import com.nxp.nfclib.ultralight.UltralightFactory;
import com.nxp.nfclib.utils.NxpLogUtils;
import com.nxp.nfclib.utils.Utilities;

import java.io.File;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

import static android.content.ContentValues.TAG;

/**
 * Created by lolo on 06/10/17.
 */

public class NFC_manager {
    private static NFC_manager instance = new NFC_manager();
    private NfcAdapter Nfc_adapter;
    private Tag lastTag;
    private NfcA reader;
    private NdefMessage ndef_file[];
    private PendingIntent mPendingIntent;


    private IKeyData objKEY_2KTDES_ULC = null;
    private IKeyData objKEY_2KTDES = null;
    private IKeyData objKEY_AES128 = null;
    private byte[] default_ff_key = null;
    private IKeyData default_zeroes_key = null;


    private static final String ALIAS_KEY_AES128 = "key_aes_128";

    private static final String ALIAS_KEY_2KTDES = "key_2ktdes";

    private static final String ALIAS_KEY_2KTDES_ULC = "key_2ktdes_ulc";

    private static final String ALIAS_DEFAULT_FF = "alias_default_ff";

    private static final String ALIAS_KEY_AES128_ZEROES = "alias_default_00";

    private static final String EXTRA_KEYS_STORED_FLAG = "keys_stored_flag";

    /**
     * NDEF MESSAGE DATA !!
     */

    static String ndefData = "Mifare";

    static String ndefDataslix2 = "MifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeamMifareSDKTeam" +
            "MifareSDKTeamMifar";


    /**
     * Package Key.
     */
    static String packageKey = "Put your package key obtained from mifare.net";

    private NxpNfcLib libInstance = null;
    /**
     * text view instance.
     */
    private TextView tv = null;
    /**
     * Image view inastance.
     */
    private ImageView mImageView = null;
    /**
     * byte array.
     */
    byte[] data;

    /**
     * Desfire card object.
     */

    private IDESFireEV2 desFireEV2;

    /**
     * Checkbox for write select
     */
    CheckBox mCheckToWrite;
    /**
     * Constant for permission
     */
    private static final int STORAGE_PERMISSION_WRITE = 113;

    /**
     * Android Handler for handling messages from the threads.
     */
    private static Handler mHandler;

    private boolean bWriteAllowed = false;

    private boolean mIsPerformingCardOperations = false;


    private CardType mCardType = CardType.UnknownCard;
    /**
     * Desfire card object.
     */
    private IDESFireEV1 desFireEV1;

    public static NFC_manager getInstance(Activity activity)
    {
        instance.initializeLibrary(activity);
        return instance;
    }

    private NFC_manager() {
        Nfc_adapter = null;
        lastTag = null;
        reader = null;
        ndef_file = null;
        mPendingIntent = null;
        Log.d("NFCExplorer_debug: ","INIT: NFC manager");
    }

    /**
     * Initialize the library and register to this activity.
     */
    @TargetApi(19)
    private void initializeLibrary(Activity activity) {
        libInstance = NxpNfcLib.getInstance();
        try {
            libInstance.registerActivity(activity, packageKey);
        } catch (NxpNfcLibException ex) {
            Toast.makeText(activity, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public TextView getTv() {
        return tv;
    }

    public void setTv(TextView tv) {
        this.tv = tv;
    }

    /*==============================================================================================
    ================================================================================================
    ==============================================================================================*/

    public void onCreate(Context context){
        Nfc_adapter = NfcAdapter.getDefaultAdapter(context);

    }

    public void onPause(Activity activity){
        if (Nfc_adapter != null) {
            Nfc_adapter.disableForegroundDispatch(activity);
        }
    }

    public void onResume(Activity activity, PendingIntent mPendingIntent){
        if (Nfc_adapter != null) {
            Nfc_adapter.enableForegroundDispatch(activity, mPendingIntent, null, null);
        }
    }

    public boolean resolveIntent(Intent data,Activity activity){

        CardType type = CardType.UnknownCard;
        try {
            type = libInstance.getCardType(data);
        } catch (NxpNfcLibException ex) {
            Toast.makeText(activity, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        switch (type) {

            case DESFireEV1:

                mCardType = CardType.DESFireEV1;
                showMessage(activity,"DESFireEV1 Card detected.", 't');
                showMessage(activity,"Card Detected : DESFireEV1", 'n');
                desFireEV1 = DESFireFactory.getInstance().getDESFire(libInstance.getCustomModules());
                try {

                    desFireEV1.getReader().connect();
                    desFireEV1.getReader().setTimeout(2000);
                    desfireEV1CardLogic(activity);

                } catch (Throwable t) {
                    t.printStackTrace();
                    showMessage(activity,"Unknown Error Tap Again!", 't');
                }
                break;

            case DESFireEV2:
                mCardType = CardType.DESFireEV2;
                showMessage(activity,"DESFireEV2 Card detected.", 't');
                tv.setText(" ");
                showMessage(activity,"Card Detected : DESFireEV2", 'n');
                desFireEV2 = DESFireFactory.getInstance().getDESFireEV2(libInstance.getCustomModules());
                try {
                    desFireEV2.getReader().connect();
                    desfireEV2CardLogic(activity);

                } catch (Throwable t) {
                    t.printStackTrace();
                    showMessage(activity,"Unknown Error Tap Again!", 't');
                }
                break;

                default:
                    Parcelable[] rawMsgs;
                    String action = data.getAction();
                    Log.d("NFCExplorer_debug: ","===========resolveIntent===========");

                    // Intent is a tag technology (we are sensitive to Ndef, NdefFormatable) or
                    // an NDEF message (we are sensitive to URI records with the URI http://www.mroland.at/)

                    if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                            || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                            || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

                        Log.d("NFCExplorer_debug: ","===========getTagInfo===========");
                        // The reference to the tag that invoked us is passed as a parameter (intent extra EXTRA_TAG)
                        lastTag = data.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                        rawMsgs = data.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

                        if(lastTag != null)
                            reader = NfcA.get(lastTag);
                        else
                            reader = null;
                        if(rawMsgs != null){
                            ndef_file = new NdefMessage[rawMsgs.length];
                            for (int i = 0; i < rawMsgs.length; i++) {
                                ndef_file[i] = (NdefMessage) rawMsgs[i];
                            }
                        }
                        return true;
                    }
                    break;
        }
        return false;
    }

    /*==============================================================================================
    ================================================================================================
    ==============================================================================================*/

    public Tag getLastTag() {
        return lastTag;
    }

    public NfcA getReader() {
        return reader;
    }

    public NdefMessage[] getNdef_file() {
        return ndef_file;
    }

    /**/
    /**
     * DESFire Pre Conditions.
     * <p/>
     * PICC Master key should be factory default settings, (ie 16 byte All zero
     * Key ).
     * <p/>
     */
    private void desfireEV1CardLogic(Activity activity) {

        byte[] appId = new byte[]{0x12, 0x00, 0x00};
        int fileSize = 100;
        byte[] data = new byte[]{0x11, 0x11, 0x11, 0x11,
                0x11};
        int timeOut = 2000;
        int fileNo = 0;

        tv.setText(" ");
        showMessage(activity,"Card Detected : " + desFireEV1.getType().getTagName(), 'n');

        try {
            desFireEV1.getReader().setTimeout(timeOut);
            showMessage(activity,
                    "Version of the Card : "
                            + Utilities.dumpBytes(desFireEV1.getVersion()),
                    'd');
            showMessage(activity,
                    "Existing Applications Ids : " + Arrays.toString(desFireEV1.getApplicationIDs()),
                    'd');

            desFireEV1.selectApplication(0);

            desFireEV1.authenticate(0, IDESFireEV1.AuthType.Native, KeyType.THREEDES, objKEY_2KTDES);

            desFireEV1.getReader().close();

            // Set the custom path where logs will get stored, here we are setting the log folder DESFireLogs under
            // external storage.
            String spath = Environment.getExternalStorageDirectory().getPath() + File.separator + "DESFireLogs";
            NxpLogUtils.setLogFilePath(spath);
            // if you don't call save as below , logs will not be saved.
            NxpLogUtils.save();

        } catch (Exception e) {
            showMessage(activity,"IOException occurred... check LogCat", 't');
            e.printStackTrace();
        }

    }

    private void desfireEV2CardLogic(Activity activity) {
        byte[] appId = new byte[]{0x12, 0x00, 0x00};
        int fileSize = 100;
        byte[] data = new byte[]{0x11, 0x11, 0x11, 0x11,
                0x11};
        int timeOut = 2000;
        int fileNo = 0;

        tv.setText(" ");
        showMessage(activity,"Card Detected : " + desFireEV2.getType().getTagName(), 'n');

        try {
            desFireEV2.getReader().setTimeout(timeOut);
            showMessage(activity,
                    "Version of the Card : "
                            + Utilities.dumpBytes(desFireEV2.getVersion()),
                    'd');
            showMessage(activity,
                    "Existing Applications Ids : " + Arrays.toString(desFireEV2.getApplicationIDs()),
                    'd');


            desFireEV2.selectApplication(0);

            desFireEV2.authenticate(0, IDESFireEV1.AuthType.Native, KeyType.THREEDES, objKEY_2KTDES);

            // Set the custom path where logs will get stored, here we are setting the log folder DESFireLogs under
            // external storage.
            String spath = Environment.getExternalStorageDirectory().getPath() + File.separator + "DESFireLogs";
            NxpLogUtils.setLogFilePath(spath);
            // if you don't call save as below , logs will not be saved.
            NxpLogUtils.save();

        } catch (Exception e) {
            showMessage(activity,"IOException occurred... check LogCat", 't');
            e.printStackTrace();
        }
    }

    /**
     * This will display message in toast or logcat or on screen or all three.
     *
     * @param str   String to be logged or displayed
     * @param where 't' for Toast; 'l' for Logcat; 'd' for Display in UI; 'n' for
     *              logcat and textview 'a' for All
     */
    protected void showMessage(Activity activity,final String str, final char where) {

        switch (where) {

            case 't':
                Toast.makeText(activity, "\n" + str, Toast.LENGTH_SHORT)
                        .show();
                break;
            case 'l':
                NxpLogUtils.i(TAG, "\n" + str);
                break;
            case 'd':
                tv.setText(tv.getText() + "\n-----------------------------------\n"
                        + str);
                break;
            case 'a':
                Toast.makeText(activity, "\n" + str, Toast.LENGTH_SHORT)
                        .show();
                NxpLogUtils.i(TAG, "\n" + str);
                tv.setText(tv.getText() + "\n-----------------------------------\n"
                        + str);
                break;
            case 'n':
                NxpLogUtils.i(TAG, "Dump Data: " + str);
                tv.setText(tv.getText() + "\n-----------------------------------\n"
                        + str);
                break;
            default:
                break;
        }
        return;
    }
}
