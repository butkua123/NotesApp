package com.example.notesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NoteDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        TextView titleView = findViewById(R.id.noteDetailTitle);
        TextView contentView = findViewById(R.id.noteDetailContent);

        // Get the data passed from MainActivity
        String noteTitle = getIntent().getStringExtra("NOTE_TITLE");
        String noteContent = getIntent().getStringExtra("NOTE_CONTENT");


        titleView.setText(noteTitle);
        contentView.setText(noteContent);

        Button editNoteButton = findViewById(R.id.editNoteButton);
        editNoteButton.setOnClickListener(new View.OnClickListener() {
            //Aktivitede gerçekleşen tıklama olaylarını işler.
            // Kullanıcı arayüzündeki çeşitli elemanlara yapılan tıklamalara yanıt verir.
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteDetailActivity.this, EditNoteActivity.class);
                intent.putExtra("NOTE_TITLE", noteTitle);
                intent.putExtra("NOTE_CONTENT", noteContent);
                intent.putExtra("NOTE_INDEX", getIntent().getIntExtra("NOTE_INDEX", -1));
                startActivity(intent);
            }
        });
    }
}
