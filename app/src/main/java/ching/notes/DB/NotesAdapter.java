package ching.notes.DB;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.List;

import ching.notes.R;


public class NotesAdapter extends BaseAdapter {

    private Context context;

    private List<Notes> mNotesList;

    public NotesAdapter(Context context, List<Notes> mNotesList){
        this.context = context;
        this.mNotesList = mNotesList;

    }


    @Override
    public int getCount() {
        if (mNotesList != null){
            return mNotesList.size();
        }else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mNotesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Notes notes = mNotesList.get(position);
        if (notes == null){
            return null;
        }
        ViewHolder holder ;
        if (convertView != null){
            holder = (ViewHolder) convertView.getTag();
        }else {
            convertView = LayoutInflater.from(context).inflate(R.layout.show_sql_item, null);

            holder = new ViewHolder();

            holder.tv_notes = (TextView) convertView.findViewById(R.id.tv_notes);

            convertView.setTag(holder);
        }

        String mNotes  ;
        if (notes.notes.length() < 20){
            mNotes = notes.notes;
        }else {
            StringBuilder sb = new StringBuilder();
            sb.append(notes.notes.substring(0,20));
            sb.append("...");
            mNotes = sb.toString();
        }
        holder.tv_notes.setText(mNotes );

        return convertView;
    }
    public static class ViewHolder{
        public TextView tv_notes;
    }
}
