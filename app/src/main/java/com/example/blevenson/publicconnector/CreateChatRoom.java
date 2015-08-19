package com.example.blevenson.publicconnector;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/*
    @author Brett Levenson

    @ToDO
        1.  Fix the spacing for the password
        2.  Implement the password field
        3.  Add "Home" Buttons to all the different activities
        4.  Ability to delete a chat room once the creator leaves it
        5.  Add a poll system where you can ask the group a poll and analyze the answers
 */
public class CreateChatRoom extends AppCompatActivity {

    private String userName;

    private SharedPreferences.Editor editor;

    private Firebase myFirebaseRef;
    private DataSnapshot currentDataSnapshot;

    private EditText chatRoomName;
    private EditText password;

    private TextView errorWindow;

    private Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat_room);
        Firebase.setAndroidContext(this);

        Intent incoming = getIntent();
        userName = incoming.getStringExtra("userName");


        chatRoomName = (EditText) findViewById(R.id.chatRoomName);
        password = (EditText) findViewById(R.id.password);

        errorWindow = (TextView) findViewById(R.id.errorMessage);

        create = (Button) findViewById(R.id.buttonCreate);

        myFirebaseRef = new Firebase("https://publicconnector.firebaseio.com/");

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentDataSnapshot = dataSnapshot;
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        chatRoomName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (currentDataSnapshot.child(chatRoomName.getText().toString()).exists()) {
                        errorWindow.setTextColor(Color.RED);
                        errorWindow.setText("That chat room already exists");
                    } else {
                        errorWindow.setTextColor(Color.parseColor("#009933"));
                        errorWindow.setText("That chat room name is avalible");
                    }
                    return true;
                }
                return false;
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chatRoomName.getText().toString().trim().length() <= 0) {
                    errorWindow.setTextColor(Color.RED);
                    errorWindow.setText("This field can not be left blank");
                } else if (currentDataSnapshot.child(chatRoomName.getText().toString()).exists())
                    return;
                else {
                    //addRoomToFavroites(chatRoomName.getText().toString());
                    LoginActivity.addToFavs(chatRoomName.getText().toString());
                    moveToMainActivity();
                }
            }
        });

    }

    private void moveToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("chatRoom", chatRoomName.getText().toString());
        intent.putExtra("userName", userName);
        startActivity(intent);
    }

    private void addRoomToFavroites(String room) {
        SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE);
        editor = settings.edit();

        editor.putString("FavChatRooms", settings.getString("FavChatRooms", "None") + "," + room);
        editor.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_chat_room, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_bar_back:
                moveHome();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveHome();
    }

    private void moveHome() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("name", userName);
        startActivity(intent);
    }
}
