package com.qppd.fifov2.Libs.Keyboardz;

import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyboardClass {

    private Context context;
    private Activity activity;
    private InputMethodManager inputMethodManager;

    //public KeyboardClass(Activity activity){
        //this.activity = activity;
    //}

    public KeyboardClass(Context context){
        this.context = context;
    }

    public void hideKeyboard(EditText editText){
        inputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public void showKeyboard(EditText editText){
        inputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }

    public void setKeyboardType(EditText editText, int inputTypedotType, int inputTypedotType2){
        editText.setRawInputType(inputTypedotType|inputTypedotType2);
    }

    public void hideWindowKeyboard(){
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
    // keyboard show or hide
    // another way
    // a.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    // a.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//    public void KEYBOARD(boolean show, ArrayList<EditText> edittexts){
//        InputMethodManager imm = null;
//        if(show){
//            for (EditText editText: edittexts ) {
//                imm = (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
//                }
//            }
//        }
//        else{
//            for (EditText editText: edittexts ) {
//                imm = (InputMethodManager)editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
//                }
//            }
//        }
//    }

}
