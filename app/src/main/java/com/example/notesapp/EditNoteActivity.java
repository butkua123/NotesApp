package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditNoteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        EditText titleEditText = findViewById(R.id.editNoteTitle);
        EditText contentEditText = findViewById(R.id.editNoteContent);
        Button saveButton = findViewById(R.id.saveEditNoteButton);

        // Get data passed from NoteDetailActivity
        String noteTitle = getIntent().getStringExtra("NOTE_TITLE");
        String noteContent = getIntent().getStringExtra("NOTE_CONTENT");
        titleEditText.setText(noteTitle);
        contentEditText.setText(noteContent);

        int noteIndex = getIntent().getIntExtra("NOTE_INDEX", -1); // Add this line at the beginning of onCreate

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedTitle = titleEditText.getText().toString();
                String updatedContent = contentEditText.getText().toString();

                if (updatedTitle.isEmpty() || updatedContent.isEmpty()) {
                    Toast.makeText(EditNoteActivity.this, "Title and content cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                if (noteIndex != -1) {
                    editor.putString("note_title_" + noteIndex, updatedTitle);
                    editor.putString("note_content_" + noteIndex, updatedContent);
                }
                editor.apply();

                finish();
            }
        });


    }
}
