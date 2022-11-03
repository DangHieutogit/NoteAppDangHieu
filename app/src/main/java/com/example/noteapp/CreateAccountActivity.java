package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class CreateAccountActivity extends AppCompatActivity {

    //khai bao set tu xml

    EditText emailEdt, passwordEdt,confirmPasswordEdt;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtnTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailEdt = findViewById(R.id.email_Edt);
        passwordEdt = findViewById(R.id.password_Edt);
        confirmPasswordEdt = findViewById(R.id.confirm_password_Edt);
        createAccountBtn = findViewById(R.id.create_account_btn);
        progressBar = findViewById(R.id.progress_bar);
        loginBtnTV = findViewById(R.id.login_tv_btn);

        //khai bao set on click cho nut button vs tv sau khi bam loginbtn se hoan thanh
        //sau khi bam nut tao account moi se duoc chuyen den ham void createAccount
        createAccountBtn.setOnClickListener(v->createAccount());
        loginBtnTV.setOnClickListener(v->finish());

    }
    void createAccount(){
        //
        String email = emailEdt.getText().toString();
        String password = passwordEdt.getText().toString();
        String confirmPassword = confirmPasswordEdt.getText().toString();

        boolean isValidated = validateData(email,password,confirmPassword);
        if(!isValidated){
            return;
        }

        createAccountInFirebase(email,password);

    }

    private void createAccountInFirebase(String email, String password) {
        changeInProgress(true);
        //kiemtra ket noi firebase
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        //kiem tra ket noi tao tai khoan sau khi hoan thanh
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccountActivity.this,
               //sau khi CompleleListener
                new OnCompleteListener<AuthResult>() {
                    @Override
                    //task nv
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if (task.isSuccessful()){
                            //creating acc in done / tao xong tai khoan
                            Utility.showToast(CreateAccountActivity.this,"Succesfully create Account, check email to verify");
                          //Hien thi ra man hinh CreateAccountActivity dong thong bao Succesfully create Account, check email to verify
                            firebaseAuth.getCurrentUser().sendEmailVerification();
                            //gui ve email de ktra
                            firebaseAuth.signOut();
                            //dang xuat /
                            finish();
                            //hoan thanh
                        }else {
                            //failure
                            Utility.showToast(CreateAccountActivity.this,task.getException().getLocalizedMessage());
                          //hien thi ra thong bao loi tai khoan khong tao duoc

                        }

                    }
                }
        );

    }

    void changeInProgress(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }

    //khai bao boolean de kiemtra dang ky nguoi dung
    boolean validateData(String email,String password,String confirmPassword){
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
        if (!password.equals(confirmPassword)){
            //kiem tra password equals(co giong nhau khong) neu kh se hien loi Error -> password not matched
            confirmPasswordEdt.setError("Password not matched ");
            return false;
        }
        return true;

    }

}