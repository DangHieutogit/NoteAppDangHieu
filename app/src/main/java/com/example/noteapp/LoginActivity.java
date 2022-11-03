package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailEdt, passwordEdt;
    Button loginBtn;
    ProgressBar progressBar;
    TextView createAccountBtnTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdt = findViewById(R.id.email_Edt);
        passwordEdt = findViewById(R.id.password_Edt);
        createAccountBtnTV = findViewById(R.id.create_tv_btn);
        progressBar = findViewById(R.id.progress_bar);
        loginBtn = findViewById(R.id.login_btn);

        loginBtn.setOnClickListener((v)-> loginUser());
        //sau khi click vao nut login se duoc chuyen den loginUser
        createAccountBtnTV.setOnClickListener((v)->startActivity(new Intent(LoginActivity.this,CreateAccountActivity.class)));
        //sau khi click vafo nut createAccount se duoc chuyen den man hinh CreateAccount
    }

     void loginUser() {
         String email = emailEdt.getText().toString();
         String password = passwordEdt.getText().toString();

         boolean isValidated = validateData(email,password);
         if(!isValidated){
             return;
         }

         loginAccountInFirebase(email,password);

     }

     void loginAccountInFirebase(String email,String password){
         FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
         changeInProgress(true);
         //ket noi voi ham void changeInprogress
         //khai bao firebaseauthe
         firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                 changeInProgress(false);
                    if (task.isSuccessful()){
                        //login to success
                        if (firebaseAuth.getCurrentUser().isEmailVerified()){
                            //ktra neu email dung, se chuyen den man hinh chinh
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                        }else {
                            Utility.showToast(LoginActivity.this,"Email not verified, Please verify your email.");
                            //thong bao kiem tra xac thuc qua email
                        }

                    }else {
                        //login failed
                        Utility.showToast(LoginActivity.this,task.getException().getLocalizedMessage());
                        //kiem tra neu tai khoan chua thuc hien


                    }
             }
         });
         //thiet lap firebase dang nhap bang email and password
     }

    void changeInProgress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }

    //khai bao boolean de kiemtra dang ky nguoi dung
    boolean validateData(String email,String password){
        //validate the data that are input by user
        //xac thuc du lieu duoc nhap boi nguoi dung

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //kiem tra cac ky tu email matcher(doi chieu)
            emailEdt.setError("Email is invalid");
            return  false;
        }
        if (password.length()<6){
            //kiem tra cac ky tu do dai cua chuoi duoi 6 ky tu se xuat hien Error ->Password lengh is invalid
            passwordEdt.setError("Password lengh is invalid");
            return false;
        }
        return true;

    }
}