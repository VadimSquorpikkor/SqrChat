package com.squorpikkor.sqrchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView messageListView;
    private MessageAdapter adapter;
    private ProgressBar progressBar;
    private ImageButton sendImageButton;
    private Button sendMessageButton;
    private EditText messageEdittext;

    private String userName;

    FirebaseDatabase database;
    DatabaseReference messagesDatabaseReferences;
    ChildEventListener messagesChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://sqrchat-default-rtdb.europe-west1.firebasedatabase.app/");
        messagesDatabaseReferences = database.getReference().child("messages");

        ArrayList<ChatMessage> list = new ArrayList<>();

        userName = "DefaultUser";
        progressBar = findViewById(R.id.progress);
        sendImageButton = findViewById(R.id.send_photo_buttton);
        sendMessageButton = findViewById(R.id.send);
        messageEdittext = findViewById(R.id.message);

        messageListView = findViewById(R.id.list);
        adapter = new MessageAdapter(this, R.layout.item_list, list);
        messageListView.setAdapter(adapter);

        progressBar.setVisibility(progressBar.INVISIBLE);

        messageEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) sendMessageButton.setEnabled(true);
                else sendMessageButton.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        messageEdittext.setFilters(new InputFilter[]
                {new InputFilter.LengthFilter(500)});


        sendMessageButton.setOnClickListener(view -> {

            ChatMessage message = new ChatMessage();
            message.setText(messageEdittext.getText().toString());
            message.setName(userName);
            message.setImgUrl(null);//так как это текстовое сообщение
            messagesDatabaseReferences.push().setValue(message);
            messageEdittext.setText("");

        });

        messagesChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ChatMessage message = snapshot.getValue(ChatMessage.class);
                adapter.add(message);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        messagesDatabaseReferences.addChildEventListener(messagesChildEventListener);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out: signOut(); return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this, SignInActivity.class));
    }
}