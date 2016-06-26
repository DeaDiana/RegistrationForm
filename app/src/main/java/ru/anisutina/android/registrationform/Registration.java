package ru.anisutina.android.registrationform;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {
    private static final String EMAIL_SUBJECT_TEMPLATE = ", your registration is completed!";

    Registration() {
    }
    private String full_name;
    private String email;
    private String user_name;
    private String password;
    private String birth;

    private TextView full_name_tv;
    private TextView email_tv;
    private TextView user_name_tv;
    private TextView password_tv;
    private TextView birth_tv;
    private Button send_btn;

    private void init() {
        full_name_tv = (TextView) findViewById(R.id.name);
        email_tv = (TextView) findViewById(R.id.email);
        user_name_tv = (TextView) findViewById(R.id.username);
        password_tv = (TextView) findViewById(R.id.password);
        birth_tv = (TextView) findViewById(R.id.birth);
        Button send_btn = (Button) findViewById(R.id.button);
        full_name_tv.startAnimation(AnimationUtils.loadAnimation(Registration.this, R.anim.slide_from_left));
        birth_tv.startAnimation(AnimationUtils.loadAnimation(Registration.this, R.anim.slide_from_right));
        email_tv.startAnimation(AnimationUtils.loadAnimation(Registration.this, R.anim.slide_from_left));
        user_name_tv.startAnimation(AnimationUtils.loadAnimation(Registration.this, R.anim.slide_from_right));
        password_tv.startAnimation(AnimationUtils.loadAnimation(Registration.this, R.anim.slide_from_left));
        send_btn.startAnimation(AnimationUtils.loadAnimation(Registration.this, R.anim.slide_from_right));

        full_name_tv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if((full_name_tv.length() != 0) && (full_name_tv.length() < 3)) {
                    full_name_tv.setError("Write your real name, please!");
                } else {
                    full_name = full_name_tv.getText().toString();
                }
            }
        });

        email_tv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if((email_tv.length() != 0) && (!validateEmail(email_tv.getText().toString()))) {
                    email_tv.setError("Write correct email, please!");
                } else {
                    email = email_tv.getText().toString();
                }
            }
        });
        birth_tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                showDatePickerDialog(getCurrentFocus());
                }

                return true;
            }
        });

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent =  new Intent(Intent.ACTION_SEND);
                String[] emails = new String[] {email};
                String email_body = new String("Fullname: " + full_name_tv.getText() + "\nDate of birth: " + birth_tv.getText()
                                            + "\nUsername: " + user_name_tv.getText() + "\nPassword: " + password_tv.getText());
                emailIntent.putExtra(Intent.EXTRA_EMAIL, emails);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, full_name + EMAIL_SUBJECT_TEMPLATE);
                emailIntent.putExtra(Intent.EXTRA_TEXT, email_body);
                emailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailIntent, "Choose email client:"));
            }
        });

    }
    private void restore() {
        full_name_tv.setText(full_name);
        email_tv.setText(email);
        birth_tv.setText(birth);
        user_name_tv.setText(user_name);
        password_tv.setText(password);
    }

    private boolean validateEmail(String email) {
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_registration);
        init();
        restore();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_registration);
        init();
        restore();
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(this.getSupportFragmentManager(), "datePicker");
    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void save() {
        birth = birth_tv.getText().toString();
        email = email_tv.getText().toString();
        full_name = full_name_tv.getText().toString();
        user_name = user_name_tv.getText().toString();
        password = password_tv.getText().toString();
    }
}