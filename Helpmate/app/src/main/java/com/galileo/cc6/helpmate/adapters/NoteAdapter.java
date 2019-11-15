package com.galileo.cc6.helpmate.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.galileo.cc6.helpmate.R;
import com.galileo.cc6.helpmate.dataModels.Note;

import java.util.List;

public class NoteAdapter extends ArrayAdapter<Note> {

    private Context context;
    private int layoutResourceId;
    private List<Note> data = null;
    private long courseID;

    public NoteAdapter(Context context, int layoutResourceId, List<Note> data, long course) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.courseID=course;
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        NoteHolder holder;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new NoteHolder();
            holder.noteTitle = (TextView)row.findViewById(R.id.noteTitle);
            holder.noteContent = (TextView)row.findViewById(R.id.noteContent);
            holder.noteDate = (TextView)row.findViewById(R.id.noteDate);


            row.setTag(holder);
        }
        else
        {
            holder = (NoteHolder)row.getTag();
        }
        Note note = data.get(position);
        String noteTitle = note.getTitle();
        if (noteTitle == null || noteTitle.length() == 0)
            noteTitle = String.format(context.getString(R.string.note_number), note.getId());
        holder.noteTitle.setText(noteTitle);

        String title = note.getRawText();
        if (title.length() != 0) {
            holder.noteContent.setText(note.getRawText());
        } else {
            //TODO Finding out if there is picture on note
            holder.noteContent.setText("INFO: La hoja no tiene texto.");
        }

        holder.noteDate.setText(context.getString(R.string.last_updated) + ": " + note.getFormattedDateUpdatted());
        return row;
    }

    public void setData(List<Note> data) {
        this.data = data;
    }

    public List<Note> getData() {
        return data;
    }

    private static class NoteHolder
    {
        private TextView noteTitle;
        private TextView noteContent;
        private TextView noteDate;
    }
}