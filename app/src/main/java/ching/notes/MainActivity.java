package ching.notes;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.SpringBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import java.util.ArrayList;
import java.util.List;

import ching.notes.DB.Notes;
import ching.notes.DB.NotesAdapter;
import ching.notes.DB.NotesDao;
import ching.notes.DB.DBhelper;
import ching.notes.PopUpWindows.PopUpMenu;
import ching.notes.PopUpWindows.PopUpInsert;


public class MainActivity extends ActionBarActivity {


    NotesDao mNotesDao;
    private List<Notes> mNotesList;
    private NotesAdapter mNotesAdapter;

    private View notesView;
    private View contentView;
    private ViewPager viewPager;



    private ListView  showDateListView;
    final  static String TAG = "MainActivty";

    DBhelper bounsDBHelper;

    private IndicatorViewPager indicatorViewPager;
    private LayoutInflater inflate;

    private EditText et_showContent;
    private Notes curently_note;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spring);

        bounsDBHelper = new DBhelper(this);

        inflate = LayoutInflater.from(getApplicationContext());

        notesView = inflate.inflate(R.layout.activity_notes, null);
        contentView = inflate.inflate(R.layout.activtiy_content, null);

        initViewpagerIndicator();

        initNotesComponent(notesView);
        initNotesData();
        initContentComponent(contentView);


    }

    /**
     * "ViewpagerIndicator.jar"
     * Indicator is a 3rd party lib, using Indicator instend to use page view
     *
     * */
    private void initViewpagerIndicator(){
         viewPager = (ViewPager) findViewById(R.id.spring_viewPager);

        int selectColorId = Color.parseColor("#f8f8f8");
        int unSelectColorId = Color.parseColor("#010101");
        Indicator indicator = (Indicator) findViewById(R.id.spring_indicator);
        indicator.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColorId, unSelectColorId));
        indicator.setScrollBar(new SpringBar(getApplicationContext(), Color.GRAY));

        indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        indicatorViewPager.setAdapter(adapter);
        indicatorViewPager.setCurrentItem(0, false);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_showContent.getWindowToken(),0);

            }
        });
    }


    /**
    * Init The first Page "NotesData"
    * Init insert Button
     * Init ListVie

     * */

    private void initNotesComponent(View v){
        Log.d(TAG,"init Notes component");
        Button insertButton = (Button)v.findViewById(R.id.insertButton);
        showDateListView = (ListView)v.findViewById(R.id.showDateListView);
        showDateListView.addHeaderView(LayoutInflater.from(this).inflate(R.layout.show_sql_item, null), null, false);
        SQLBtnOnclickListener onclickListener = new SQLBtnOnclickListener();
        insertButton.setOnClickListener(onclickListener);


        showDateListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"Long clikc");
                deleteMenu(position);
                return true;
            }
        });
        showDateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG,"click DataList");

                    Notes notes = mNotesList.get(position-1);
                curently_note = notes;
                    int m_id = notes.id;
                    String text = mNotesDao.getTextByPositon(m_id);
                    Log.d(TAG,"Show Text : " + text);
                et_showContent.setFocusableInTouchMode(false);
                et_showContent.setText(text);
                et_showContent.setTextColor(Color.BLACK);
                    viewPager.setCurrentItem(1);




            }
        });
    }





    /**
     * Using to fetch Data from SQLite, in order to show data in the begin
     *
     * */
    private void initNotesData(){
        Log.d(TAG,"init Data");
        mNotesDao = new NotesDao(this,bounsDBHelper);
        mNotesList = new ArrayList<Notes>() {
        };

        if (mNotesDao.getAllDate() != null) {
            mNotesList = mNotesDao.getAllDate();
            mNotesAdapter = new NotesAdapter(this, mNotesList);


        }else{
            mNotesAdapter = new NotesAdapter(this,mNotesList);

            Log.e(TAG,"orderList = null");

        }

        showDateListView.setAdapter(mNotesAdapter);


    }

    private void initContentComponent(View v){

        et_showContent = (EditText) v.findViewById(R.id.et_content);
        et_showContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isDoubleClicked();
                if(mHasDoubleClicked){
                    et_showContent.setFocusableInTouchMode(true);
                    et_showContent.setTextColor(getResources().getColor(R.color.colorAccent));

                    Log.d(TAG,"double Click is  : " + et_showContent.isFocusable() );
                }

            }
        });
        et_showContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String m_string = et_showContent.getText().toString();
                if(curently_note != null){
                    curently_note.setNotes(m_string);
                    mNotesDao.editData(curently_note);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private static final long DOUBLE_PRESS_INTERVAL = 250; // in millis
    private long lastPressTime;

    private boolean mHasDoubleClicked = false;
    private boolean isDoubleClicked() {

        // Get current time in nano seconds.
        long pressTime = System.currentTimeMillis();


        // If double click...
        if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {
       //     Toast.makeText(getApplicationContext(), "Double Click Event", Toast.LENGTH_SHORT).show();
            mHasDoubleClicked = true;
        } else {     // If not double click....
            mHasDoubleClicked = false;
            Handler myHandler = new Handler() {
                public void handleMessage(Message m) {
                    if (!mHasDoubleClicked) {
               //         Toast.makeText(getApplicationContext(), "Single Click Event", Toast.LENGTH_SHORT).show();
                    }
                }
            };
            Message m = new Message();
            myHandler.sendMessageDelayed(m, DOUBLE_PRESS_INTERVAL);
        }
        // record the last time the menu button was pressed.
        lastPressTime = pressTime;
        return true;
    }

    private void refreshOrderList(){
        if(mNotesList != null) {
            mNotesList.clear();
        }
        if (mNotesDao.getAllDate() != null) {
            mNotesList.addAll(mNotesDao.getAllDate());
        }
//        Log.d(TAG, String.valueOf(mNotesDao.getAllDate().size()));
        mNotesAdapter.notifyDataSetChanged();
    }

    public void insertMenu(){
        PopUpInsert popUpInsert = new PopUpInsert(this,this);


    }
    public void insetData(Notes notes){
        mNotesDao.insertDate(notes);
        refreshOrderList();
    }

    public void deleteMenu(int position){
        PopUpMenu popUpMenu = new PopUpMenu(this,this,position);

    }
    public void deleteData(int position){

        Log.d(TAG,"DeleteData");
        Notes notes = mNotesList.get(position-1);
        mNotesDao.DeletData(notes.id);
        refreshOrderList();
    }
    public void editData(Notes n){
        Log.d(TAG,"ModifyData");
        mNotesDao.editData(n);
        refreshOrderList();



    }



    public class SQLBtnOnclickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.insertButton:
                    insertMenu();
                    break;

            }

        }
    }




    private IndicatorViewPager.IndicatorPagerAdapter adapter = new IndicatorViewPager.IndicatorViewPagerAdapter() {

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {

            if (convertView == null) {
                convertView = inflate.inflate(R.layout.indicator_top, container, false);
            }
            TextView textView = (TextView) convertView;
            if(position == 0) {
                textView.setText("Notes");
            }
            if(position == 1){
                textView.setText("Context");

            }
            return convertView;
        }

        @Override
        public View getViewForPage(int position, View convertView, ViewGroup container) {

            switch (position){
                case 0 :
                    convertView = notesView;
                    break;
                case 1 :
                    convertView = contentView;
                    break;

            }

            return convertView;
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

}
