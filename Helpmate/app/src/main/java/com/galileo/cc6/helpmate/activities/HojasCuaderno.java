package com.galileo.cc6.helpmate.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.galileo.cc6.helpmate.R;
import com.galileo.cc6.helpmate.adapters.NoteAdapter;
import com.galileo.cc6.helpmate.dataModels.Note;
import com.galileo.cc6.helpmate.database.HojasDAO;

import java.util.ArrayList;

/**
 * Main Activity class
 */
public class HojasCuaderno extends AppCompatActivity {

    // Array used to backup data before using search function
    private ArrayList<Note> allNotesSearchArray;
    public static final String TAG = "HojasCuaderno";
    // Database Handler

    private HojasDAO hojas;

    // Alert dialogs for back button and delete all notes button
    private AlertDialog alertDialogDeleteAll;
    private AlertDialog alertDialogDeleteSingleNote;

    // Note selected on menu
    private Note selectedNote;

    // Variables used to handle note list
    public NoteAdapter noteAdapter;
    public ListView listView;
    long course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hojas_cuaderno);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        hojas = new HojasDAO(getApplicationContext());

        Intent intent = getIntent();
        course = intent.getExtras().getLong("Curso");
        Log.d(TAG, "course: "+course);
        //Toast.makeText(HojasCuaderno.this,"Curso Seleccionado id: "+course, Toast.LENGTH_SHORT).show();

        // Add items to ListView
        listView = (ListView) findViewById(R.id.listView);
        populateListView(hojas.getAllNotesByCourse(course));
        Log.d(TAG, "list size : " + hojas.getAllNotesByCourse(course).size());
        // Assign listView to context menu
        registerForContextMenu(listView);

        // Setup AlertDialogs
        alertDialogDeleteAll = initAlertDialogDeleteAllNotes();

        // Floating Action Button listener used to adding new notes
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard();
                Intent intent = new Intent(HojasCuaderno.this, Hojas.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Bundle data = new Bundle();
                data.putLong("Curso",course);
                intent.putExtras(data);
                startActivity(intent);
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedNote = (Note) parent.getAdapter().getItem(position);
                editNote(selectedNote.getId());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Creating menu
        getMenuInflater().inflate(R.menu.menu_cuaderno, menu);

        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint(searchView.getContext().getResources().getString(R.string.search_hint));

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allNotesSearchArray = (ArrayList<Note>) noteAdapter.getData();
            }
        });

        final android.support.v7.widget.SearchView.OnQueryTextListener queryTextListener = new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {

                ArrayList<Note> filteredNotesArrayList = new ArrayList<>();
                for (Note note : allNotesSearchArray) {
                    if (note.getRawText().contains(newText)) {
                        filteredNotesArrayList.add(note);
                    }
                }

                populateListView(filteredNotesArrayList);
                noteAdapter.notifyDataSetChanged();

                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                // Do nothing
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return true;
    }

    /**
     * Method used for first setup of delete all notes button AlertDialog
     */
    private AlertDialog initAlertDialogDeleteAllNotes() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(this.getString(R.string.confirmation)).setTitle(this.getString(R.string.delete_notes_title));
        builder.setPositiveButton(this.getString(R.string.ok_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAllNotes(course);
                Toast.makeText(HojasCuaderno.this, getString(R.string.delete_notes_success),
                        Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(this.getString(R.string.cancel_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

    /**
     * Method used for setup of delete single note button AlertDialog
     */
    private AlertDialog setupAlertDialogDeleteSingleNote() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(this.getString(R.string.confirmation)).setTitle(String.format(this.getString(R.string.delete_note_number), selectedNote.getId()));
        builder.setPositiveButton(this.getString(R.string.ok_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hojas.deleteNote(selectedNote);
                noteAdapter.remove(selectedNote);
                noteAdapter.notifyDataSetChanged();
                Toast.makeText(HojasCuaderno.this, String.format(getString(R.string.note_deleted), selectedNote.getId()),
                        Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(this.getString(R.string.cancel_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

    /**
     * Method used to show AlertDialog when delete all notes button is clicked
     */
    public void showAlertDialogDeleteAllNotes(MenuItem menuItem) {
        alertDialogDeleteAll.show();
    }

    /**
     * Method used to show AlertDialog when delete note button is clicked
     */
    private void showAlertDialogDeleteSingleNote() {
        alertDialogDeleteSingleNote.show();
    }


    public void deleteAllNotes(long courseID) {
        hojas.clearAllNotesByCourse(courseID);
        noteAdapter.clear();
        noteAdapter.notifyDataSetChanged();
    }

    /**
     * Method used to enter note edition mode
     *
     * @param noteId ID number of the Note entry in the SQLite database
     */
    private void editNote(int noteId) {
        hideSoftKeyboard();
        Intent intent = new Intent(HojasCuaderno.this, NoteActivity.class);
        Bundle data = new Bundle();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("id", String.valueOf(noteId));
        intent.putExtra("Curso",course);
        startActivity(intent);
    }

    /**
     * Method used to hide keyboard
     */
    private void hideSoftKeyboard() {
        if (this.getCurrentFocus() != null) {
            try {
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getApplicationWindowToken(), 0);
            } catch (RuntimeException e) {
                //ignore
            }
        }
    }

    /**
     * Method used to fill ListView
     *
     * @param note Array of Notes containing all Notes in Database
     */
    void populateListView(ArrayList<Note> note) {
        noteAdapter = new NoteAdapter(this,
                R.layout.listview_item_row, note,course);
        listView.setAdapter(noteAdapter);
    }

    public void setListViewData(ArrayList<Note> allNotes, Note newNote) {
        if (noteAdapter != null) {
            if (newNote != null){
                noteAdapter.add(newNote);
            }
            noteAdapter.setData(allNotes);
            noteAdapter.notifyDataSetChanged();
        }
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.listView) {
            ListView listViewLocal = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            selectedNote = (Note) listViewLocal.getItemAtPosition(acmi.position);
            menu.setHeaderTitle(String.format(v.getContext().getString(R.string.choose_activity), selectedNote.getId()));
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.context_menu_note_select, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.context_menu_delete:
                alertDialogDeleteSingleNote = setupAlertDialogDeleteSingleNote();
                showAlertDialogDeleteSingleNote();
                break;
            case R.id.context_menu_edit:
                editNote(selectedNote.getId());
                break;
        }
        return super.onContextItemSelected(item);
    }
}
