package com.qppd.fifov2.ui.note;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qppd.fifov2.Classes.Expense;
import com.qppd.fifov2.Classes.Note;
import com.qppd.fifov2.Database.DBHandler;
import com.qppd.fifov2.Globals.ExpenseGlobal;
import com.qppd.fifov2.Globals.NoteGlobal;
import com.qppd.fifov2.Libs.DateTimez.DateTimeClass;
import com.qppd.fifov2.Libs.Functionz.UserFunctions;
import com.qppd.fifov2.R;

public class NoteActivity extends AppCompatActivity implements View.OnClickListener {

    private DBHandler dbHandler = new DBHandler(this);
    private UserFunctions functions = new UserFunctions(this);
    private DateTimeClass dateTimeClass = new DateTimeClass("MM/dd/YYYY");

    private EditText edtNoteTitle;
    private EditText edtNoteContent;

    private Button btnNoteSave;
    private Button btnNoteUpdate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        functions.setActionbar(getSupportActionBar(), 1, "", 0);
        //getSupportActionBar().setTitle(NoteGlobal.getNote().getTitle());

        initComponents();

        if(NoteGlobal.IsView()){
            getSupportActionBar().setTitle("View Note");
            edtNoteTitle.setText(NoteGlobal.getNote().getTitle());
            edtNoteContent.setText(NoteGlobal.getNote().getContent());

            btnNoteSave.setVisibility(View.GONE);
            btnNoteUpdate.setVisibility(View.VISIBLE);
        }
        else{
            getSupportActionBar().setTitle("Add Note");
            btnNoteSave.setVisibility(View.VISIBLE);
            btnNoteUpdate.setVisibility(View.GONE);
        }
    }

    private void initComponents() {

        edtNoteTitle = findViewById(R.id.edtNoteTitle);
        edtNoteContent = findViewById(R.id.edtNoteContent);

        btnNoteSave = findViewById(R.id.btnNoteSave);
        btnNoteSave.setOnClickListener(this);
        btnNoteUpdate = findViewById(R.id.btnNoteUpdate);
        btnNoteUpdate.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnNoteSave:
                attemptSave();
                break;
            case R.id.btnNoteUpdate:
                attemptUpdate();
                break;
        }

    }

    private void attemptSave() {
        boolean cancel = false;
        View focusView = null;

        String title = edtNoteTitle.getText().toString();
        String content = edtNoteContent.getText().toString();

        if (TextUtils.isEmpty(title)) {
            functions.showMessage("Title is required!");
            cancel = true;
        }

        if (TextUtils.isEmpty(content)) {
            functions.showMessage("Note content/message is required!");
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt signup and focus the
            // field with an error.
            focusView.requestFocus();
        } else {

            if (dbHandler.addNote(new Note(title, content, dateTimeClass.getFormattedTime()))) {
                functions.showMessage("Note saved successfully!");
                this.finish();

            } else {
                functions.showMessage("Note failed saving! Try again!");
            }

        }
    }

    private void attemptUpdate() {
        boolean cancel = false;
        View focusView = null;

        String title = edtNoteTitle.getText().toString();
        String content = edtNoteContent.getText().toString();

        if (TextUtils.isEmpty(title)) {
            functions.showMessage("Title is required!");
            cancel = true;
        }

        if (TextUtils.isEmpty(content)) {
            functions.showMessage("Note content/message is required!");
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt signup and focus the
            // field with an error.
            focusView.requestFocus();
        } else {

            if (dbHandler.updateNote(new Note(NoteGlobal.getNote().getId(), title, content,
                    dateTimeClass.getFormattedTime()))) {
                functions.showMessage("Note updated successfully!");
                this.finish();

            } else {
                functions.showMessage("Note failed updating! Try again!");
            }

        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}