package com.dma.otp_verification_with_phone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpSendActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    EditText etPhone;
    Button btnSend;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_send);

        mAuth = FirebaseAuth.getInstance();

        etPhone = findViewById(R.id.etPhone);

        btnSend = findViewById(R.id.btnSend);
        progressBar = findViewById(R.id.progressBar);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etPhone.getText().toString().trim().isEmpty()) {
                    Toast.makeText(OtpSendActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
                } else if (etPhone.getText().toString().trim().length() != 10) {
                    Toast.makeText(OtpSendActivity.this, "Type valid Phone Number", Toast.LENGTH_SHORT).show();
                } else {
                   otpSend();
                }
            }
        });
    }

    private void otpSend() {
        progressBar.setVisibility(View.VISIBLE);
        btnSend.setVisibility(View.INVISIBLE);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progressBar.setVisibility(View.GONE);
                btnSend.setVisibility(View.VISIBLE);
                Toast.makeText(OtpSendActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                progressBar.setVisibility(View.GONE);
                btnSend.setVisibility(View.VISIBLE);
                Toast.makeText(OtpSendActivity.this, "OTP is successfully send.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OtpSendActivity.this, OtpVerifyActivity.class);
                intent.putExtra("phone", etPhone.getText().toString().trim());
                intent.putExtra("verificationId", verificationId);
                startActivity(intent);
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+880" + etPhone.getText().toString().trim())
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


}