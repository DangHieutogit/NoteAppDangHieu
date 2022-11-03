package com.example.noteapp;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class Utility {
    static void showToast(Context context, String message){
        //tao ra de hien len thong bao
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();

    }
    static CollectionReference getCollectionReferenceForNotes(){
        /*khai bao CollectionReference getCollectionReferenceForNotes
        ket noi du lieu Firebase user sau khi nguoi dung da dang nhap vao note app
        van bat dau them title + content se duoc luu vao firestore tren fire base
         */
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
       return FirebaseFirestore.getInstance().collection("notes")
                .document(currentUser.getUid()).collection("my_notes");
    }
    static String timestampToString(Timestamp timestamp){
        return new  SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate());
        //khai bao de hoat dong duoc timestamp adapter cua timestamp
    }

}
