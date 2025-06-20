package com.qppd.fifov2.ui.note;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.qppd.fifov2.Classes.Note;
import com.qppd.fifov2.Database.DBHandler;
import com.qppd.fifov2.Globals.NoteGlobal;
import com.qppd.fifov2.Libs.DateTimez.DateTimeClass;
import com.qppd.fifov2.Libs.Functionz.UserFunctions;
import com.qppd.fifov2.Libs.IntentManager.IntentManagerClass;
import com.qppd.fifov2.R;
import com.qppd.fifov2.databinding.FragmentNoteBinding;

import java.util.ArrayList;

public class NoteFragment extends Fragment implements View.OnClickListener {

    private FragmentNoteBinding binding;
    private LayoutInflater layoutInflater;
    private View root;

    private UserFunctions functions;
    private DateTimeClass dateTimeClass = new DateTimeClass("MM/dd/YYYY");
    private DBHandler dbHandler;

    private ListView listNotes;

    private int note_key;
    private ArrayList<String> note_keys;
    private ArrayList<Note> note_list;

    private NoteList noteAdapter;
    private Note note;

    private Button btnAddNote;

    private AlertDialog.Builder dialogBuilder;
    private View dialogView;
    private AlertDialog alertDialog;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_note, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menuView:
                NoteGlobal.setIsView(true);
                NoteGlobal.setNote(note);
                IntentManagerClass.intentsify(getActivity(), NoteActivity.class);
                break;
            case R.id.menuRemove:
                dbHandler.deleteNote(note);
                loadNotes();
                break;

        }
        return false;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        layoutInflater = inflater;
        binding = FragmentNoteBinding.inflate(layoutInflater, container, false);
        root = binding.getRoot();

        functions = new UserFunctions(getContext());
        dbHandler = new DBHandler(getContext());

        initComponents();

        loadNotes();

        return root;
    }

    private void loadNotes() {
        note_list = new ArrayList<>();
        note_keys = new ArrayList<>();

        note_list = dbHandler.getAllNotes();

        noteAdapter = new NoteList((Activity)root.getContext(), note_list);
        listNotes.setAdapter(noteAdapter);

        listNotes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                note_key = note_list.get(i).getId();
                note = note_list.get(i);

                return false;
            }
        });
    }

    private void initComponents() {

        functions = new UserFunctions(root.getContext());

        listNotes = root.findViewById(R.id.listNotes);
        registerForContextMenu(listNotes);

        btnAddNote = root.findViewById(R.id.btnAddNote);
        btnAddNote.setOnClickListener(this);

        buildAddNoteDialog();
    }

    private void buildAddNoteDialog() {
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAddNote:
                NoteGlobal.setIsView(false);
                IntentManagerClass.intentsify(getActivity(), NoteActivity.class);
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        loadNotes();
    }
}