package com.example.notesapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Button; // Import the Button class
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "NotePrefs";
    private static final String KEY_NOTE_COUNT = "NoteCount";

    private LinearLayout notesContainer;
    private List<Note> noteList; // Change to 'noteList' to match the case
    //Aktivitenin durumunu kaydeder.
    // Uygulama yeniden başlatıldığında veya durum değişikliği olduğunda bu durumu geri yükler.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current text in EditText fields
        EditText titleEditText = findViewById(R.id.titleEditText);
        EditText contentEditText = findViewById(R.id.contentEditText);
        outState.putString("titleText", titleEditText.getText().toString());
        outState.putString("contentText", contentEditText.getText().toString());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText titleEditText = findViewById(R.id.titleEditText);
        EditText contentEditText = findViewById(R.id.contentEditText);

        // Check if savedInstanceState is not null and restore saved state
        if (savedInstanceState != null) {
            String titleText = savedInstanceState.getString("titleText");
            String contentText = savedInstanceState.getString("contentText");
            titleEditText.setText(titleText);
            contentEditText.setText(contentText);
        }

        notesContainer = findViewById(R.id.notesContainer);
        Button saveButton = findViewById(R.id.saveButton);

        noteList = new ArrayList<>();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        Button aboutButton = findViewById(R.id.aboutButton);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        loadNotesFromPreferences();
        displayNotes();
    }

    private void displayNotes() {
        for(Note note : noteList){
            createNoteView(note);
        }
    }

    private void loadNotesFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int noteCount = sharedPreferences.getInt(KEY_NOTE_COUNT, 0);

        for (int i = 0; i < noteCount; i++){
            String title = sharedPreferences.getString("note_title_" + i, "");
            String content = sharedPreferences.getString("note_content_" + i, "");

            Note note = new Note();
            note.setTitle(title);
            note.setContent(content);

            noteList.add(note);
        }
    }

    private void saveNote() {
        EditText titleEditText = findViewById(R.id.titleEditText);
        EditText contentEditText = findViewById(R.id.contentEditText);

        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();

        if(!title.isEmpty() && !content.isEmpty()){
            Note note = new Note();
            note.setTitle(title);
            note.setContent(content);

            noteList.add(note);
            saveNotesToPreferences();

            createNoteView(note);
            clearInputFields();
        }

    }

    private void clearInputFields() {
        EditText titleEditText = findViewById(R.id.titleEditText);
        EditText contentEditText = findViewById(R.id.contentEditText);

        titleEditText.getText().clear();
        contentEditText.getText().clear();
    }

    private void createNoteView(final Note note) {
        View noteView = getLayoutInflater().inflate(R.layout.note_item, null);
        TextView titleTextView = noteView.findViewById(R.id.titleTextView);
        TextView contentTextView = noteView.findViewById(R.id.contentTextView);

        int textSize = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE).getInt("textSize", 16);
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        contentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        String textColorName = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE).getString("textColor", "Black");
        int textColor = getColorFromString(textColorName);
        titleTextView.setTextColor(textColor);
        contentTextView.setTextColor(textColor);

        titleTextView.setText(note.getTitle());
        contentTextView.setText(note.getContent());

        // Set the click listener to open the note detail when the noteView is clicked
        noteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNoteDetail(note);
            }
        });

        noteView.setOnLongClickListener(new View.OnLongClickListener() {
            //Uzun tıklama olaylarını işler.
            // Genellikle notların uzun süreli tıklanması gibi durumlarda kullanılır.
            @Override
            public boolean onLongClick(View v) {
                showDeleteDialog(note);
                return true;
            }
        });

        notesContainer.addView(noteView);

        ImageButton shareButton = noteView.findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareNote(note.getTitle(), note.getContent());
            }
        });
    }


    private void showDeleteDialog(final Note note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete this note");
        builder.setMessage("Are you sure you want to delete this note?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteNoteAndRefresh(note);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void deleteNoteAndRefresh(Note note){
        noteList.remove(note);
        saveNotesToPreferences();
        refreshNoteViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshNoteViews();
    }

    private void refreshNoteViews() {
        noteList.clear(); // Clear the existing list
        notesContainer.removeAllViews(); // Clear the UI container
        loadNotesFromPreferences(); // Reload notes from SharedPreferences
        displayNotes(); // Redisplay the notes
    }

    private void saveNotesToPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_NOTE_COUNT, noteList.size());
        for(int i = 0; i < noteList.size(); i ++){
            Note note = noteList.get(i);
            editor.putString("note_title_" + i, note.getTitle());
            editor.putString("note_content_" + i, note.getContent());
        }
        editor.apply();

    }
    //Bir notun başlığını ve içeriğini paylaşmaya olanak tanır.
    public void shareNote(String title, String content) {
        String shareBody = title + "\n" + content;
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Note Shared from My Notes App");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
    //Bir notun detaylarını gösteren aktiviteyi açar.
    public void openNoteDetail(Note note) {
        int noteIndex = noteList.indexOf(note);
        Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
        intent.putExtra("NOTE_TITLE", note.getTitle());
        intent.putExtra("NOTE_CONTENT", note.getContent());
        intent.putExtra("NOTE_INDEX", noteIndex); // Add this line
        startActivity(intent);
    }
    //
    private int getColorFromString(String colorName) {
        int colorId = getResources().getIdentifier(colorName.toLowerCase(), "color", getPackageName());
        if (colorId != 0) {
            return getResources().getColor(colorId);
        } else {
            return Color.BLACK; // Default color if not found
        }
    }
}