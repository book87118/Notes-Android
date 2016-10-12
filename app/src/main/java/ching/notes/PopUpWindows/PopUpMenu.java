package ching.notes.PopUpWindows;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import ching.notes.DB.Notes;
import ching.notes.MainActivity;
import ching.notes.R;

/**
 * Created by book871181 on 16/6/11.
 */
public class PopUpMenu {
    final static String TAG = "PopUpMenu";
    Button bt_delete;
    Button bt_fix;


    Context mContext;
    MainActivity mMainActivity;
    PopupWindow window;
    int position;

    public PopUpMenu(Context mContext, MainActivity mMainActivity, int position){
        this.mContext = mContext;
        this.mMainActivity = mMainActivity;
        this.position = position;
        initPopUp();
    }

    public void initPopUp(){

        View view = View.inflate(mContext, R.layout.item_popup, null);
        window = new PopupWindow(mContext);
        window.setContentView(view);
        window.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        window.setAnimationStyle(R.style.AnimationPreview);
        window.showAtLocation(view, Gravity.CENTER_HORIZONTAL, 0, 0);

        bt_delete = (Button)view.findViewById(R.id.bt_delete);


        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
                window.dismiss();
            }
        });

    }
    private void editData(){
        int id = position;
        String title  = "Test";
        String notes = "Notes";
        Notes n = new Notes(id,title,notes);
        mMainActivity.editData(n);
    }

    private void deleteData(){
        mMainActivity.deleteData(position);
    }



}
