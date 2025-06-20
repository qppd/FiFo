package com.qppd.fifov2.ui.note;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.qppd.fifov2.Classes.Note;
import com.qppd.fifov2.Classes.Saving;
import com.qppd.fifov2.R;

import java.util.List;

public class NoteList extends ArrayAdapter<Note> {

    private Activity context;
    private List<Note> noteList;

    public NoteList(Activity context, List<Note> noteList){
        super(context, R.layout.fragment_note, noteList);
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View ListViewItem = inflater.inflate(R.layout.layout_list_note, null, true);



        TextView note_title = (TextView)ListViewItem.findViewById(R.id.note_title);
        TextView note_content = (TextView)ListViewItem.findViewById(R.id.note_content);
        TextView note_datetime = (TextView)ListViewItem.findViewById(R.id.note_datetime);

        Note note = noteList.get(position);

        note_title.setText(note.getTitle());
        note_content.setText(note.getContent());
        note_datetime.setText(note.getDatetime());


        return ListViewItem;

    }

}
