package ching.notes.PopUpWindows;

import android.content.Context;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import ching.notes.DB.Notes;
import ching.notes.MainActivity;
import ching.notes.R;


/**
 * Created by book871181 on 16/6/11.
 */
public class PopUpInsert {
    final static String TAG = "PopUp";
    Context mContext;
    EditText et_notes;

    MainActivity mMainActivity;

    PopupWindow window;
    public PopUpInsert(Context mContext, MainActivity mMainActivity){
        this.mContext = mContext;
        this.mMainActivity = mMainActivity;


        initPopUp();
    }

    public void initPopUp(){

        Log.d(TAG,"initPopUp");
        View view = View.inflate(mContext, R.layout.insert_popup, null);
        et_notes = (EditText)view.findViewById(R.id.et_notes);

        et_notes.setSelectAllOnFocus(true);
        window = new PopupWindow(mContext);

        window.setContentView(view);
        window.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setAnimationStyle(R.style.AnimationPreview);
        window.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 0);


//        when popupwindow show up, the background fade.

        WindowManager.LayoutParams lp = mMainActivity.getWindow().getAttributes();
        lp.alpha = 0.5f; //0.0-1.0
        mMainActivity.getWindow().setAttributes(lp);

//
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = mMainActivity.getWindow().getAttributes();
                lp.alpha = 1.0f; //0.0-1.0
                mMainActivity.getWindow().setAttributes(lp);

            }
        });


        Button button_add = (Button)view.findViewById(R.id.button_add);

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click();

            }
        });

    }


    private void Click(){
        if(isEmpty(et_notes) ){

            Toast.makeText(mContext.getApplicationContext(),"Please fill your content",Toast.LENGTH_SHORT).show();
        }else {
            setNotes();
            window.dismiss();

        }
    }
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    public void setNotes(){

        String s_notes = String.valueOf(et_notes.getText());
        String shorterNotes  = "";
        String s_title ;
        if (s_notes.length() < 10){
            shorterNotes = s_notes;


        }else{
            shorterNotes = s_notes.substring(0,9);

        }
        if(shorterNotes.contains(" ") ){

             s_title = s_notes.substring(0,s_notes.indexOf(" "));

        }else if (shorterNotes.contains("\n")){
            s_title = s_notes.substring(0,s_notes.indexOf("\n"));

        }else {
            s_title = shorterNotes;
        }


        try {
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        Notes notes = new Notes(1,s_title,s_notes);
        mMainActivity.insetData(notes);

    }



}
