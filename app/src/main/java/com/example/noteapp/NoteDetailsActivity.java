package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {
    EditText titleEditText, contentEditText;
    ImageButton saveNoteBtn;
    TextView pageTitleTextView;
    String title,content,docId;
    TextView deleteNoteTvBtn;
    boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.notes_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteNoteTvBtn = findViewById(R.id.delete_note_tv_btn);
        //receive data
        /*
        sau khi click itemview se co giu lieu cua title content doc id
        neu doc id rong thi se con page title la new note con da co se hien edit your note

         */
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if (docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);
        if (isEditMode){
            pageTitleTextView.setText("Edit your Note");
            deleteNoteTvBtn.setVisibility(View.VISIBLE);
        }


        saveNoteBtn.setOnClickListener((v) -> saveNote());

        deleteNoteTvBtn.setOnClickListener((v)-> deleteNoteFormFirebase());

    }




    void saveNote() {
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();

        if (noteTitle == null || noteTitle.isEmpty()) {
            titleEditText.setError("Title is required");
            //neu dong title trong se hien thi ra thong bao Title is required
            return;
        }
        //khai bao note tao file note khai bao cac ham title,content, timestamp
        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteToFireBase(note);

    }
    void saveNoteToFireBase(Note note){
        DocumentReference documentReference;
        /*
        khai bao ham DocumentReference -> documentReference = Utility.getCollectionReferenceForNotes().document
        luu vao du lieu tren firestore
         */
        if (isEditMode){
            //update the note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);

        }else {
            //create new note
            documentReference = Utility.getCollectionReferenceForNotes().document();

        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //note is added
                    Utility.showToast(NoteDetailsActivity.this,"Note added Successfully");
                    finish();

                }else {
                    Utility.showToast(NoteDetailsActivity.this,"Failed while adding note");


                }


            }
        });


    }
    void deleteNoteFormFirebase() {
        //ham deletenote tuong tu nhu edit
 
        DocumentReference documentReference;
        /*
        khai bao ham DocumentReference -> documentReference = Utility.getCollectionReferenceForNotes().document
        luu vao du lieu tren firestore
         */

            //delete new note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);


        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            /*
            thay doi phan set note sang thanh delete sau khi an vao btn delete
             */
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //note is delete
                    Utility.showToast(NoteDetailsActivity.this,"Note deleted Successfully");
                    finish();

                }else {
                    Utility.showToast(NoteDetailsActivity.this,"Failed while deleting note");


                }


            }
        });


    }
}

