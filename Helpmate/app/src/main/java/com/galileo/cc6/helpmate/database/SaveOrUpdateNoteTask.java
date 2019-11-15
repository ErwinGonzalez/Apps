package com.galileo.cc6.helpmate.database;

import android.app.Activity;
import android.app.Application;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.galileo.cc6.helpmate.R;
import com.galileo.cc6.helpmate.activities.HojasCuaderno;
import com.galileo.cc6.helpmate.dataModels.Note;

import java.util.ArrayList;

public class SaveOrUpdateNoteTask extends AsyncTask<Note, Void, Void> implements Application.ActivityLifecycleCallbacks {

    //SaveOrUpdateNoteTask is an auxiliary class to HojasDAO,  SaveOrUpdateNoteTask manages real time updating
    private static final String TAG = "SAVEorUPDATE";
    private final Activity mCallingActivity;

    private final boolean mIsUpdating;
    private final HojasDAO hojas;
    private Activity currentActivity = null;
    private long course;


    /**
     * Custom constructor
     * @param callingActivity used to make Toasts/Dialogs on it
     * @param hojas is DAO responsible for managing creation and update
     * @param isUpdating boolean that is used to see if note is new or updated
     * @param course is used to validate note associated course
     */
    public SaveOrUpdateNoteTask(Activity callingActivity, HojasDAO hojas, boolean isUpdating,long course) {
        mCallingActivity = callingActivity;
        this.hojas=hojas;
        mIsUpdating = isUpdating;
        callingActivity.getApplication().registerActivityLifecycleCallbacks(this);
        this.course=course;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Note... params) {

        if (mIsUpdating) {
            Note note = params[0];
            note.setCourseID(course);
            Log.d(TAG, note.getTitle()+": "+note.getCourseID()+":"+this.course);
            hojas.updateNote(note);

        }
        else {
            hojas.createNote(params[0]);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

        if (mIsUpdating) {
            Toast.makeText(mCallingActivity, mCallingActivity.getString(R.string.toast_note_updated), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mCallingActivity, mCallingActivity.getString(R.string.toast_note_created), Toast.LENGTH_SHORT).show();
        }

        updateNoteListView();

    }

    private void updateNoteListView(){
        if (currentActivity != null && currentActivity instanceof HojasCuaderno){
            ArrayList<Note> allNotes = hojas.getAllNotesByCourse(course);
            Note newNote = null;
            if (!mIsUpdating){
                newNote = allNotes.get(allNotes.size()-1);
            }
            ((HojasCuaderno) currentActivity).setListViewData(allNotes, newNote);
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {}

    @Override
    public void onActivityStarted(Activity activity) {}

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity) {}

    @Override
    public void onActivityStopped(Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}

    @Override
    public void onActivityDestroyed(Activity activity) {}
}
