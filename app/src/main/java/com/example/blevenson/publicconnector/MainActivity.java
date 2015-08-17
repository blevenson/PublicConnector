package com.example.blevenson.publicconnector;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.support.v7.app.ActionBar;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

/*
    A chat aplication for friends to start quick conversations

    Working titles:
        1.  Chap
            a.  Chat + app = Chap
            b.  Britsh for pal, or friend
    @author Brett Levenson
 */

public class MainActivity extends AppCompatActivity {

    private Firebase myFirebaseRef;

    private String userName;
    private String chatRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        chatRoom = intent.getStringExtra("chatRoom");
    }

    @Override
    protected void onStart() {
        super.onStart();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(chatRoom);

        final EditText messageText = (EditText)findViewById(R.id.messageText);
        final TextView messanger = (TextView)findViewById(R.id.display);
        messanger.setMovementMethod(new ScrollingMovementMethod());

        myFirebaseRef = new Firebase("https://publicconnector.firebaseio.com/").child(chatRoom);

        myFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messanger.setText("");
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Chat chatter = child.getValue(Chat.class);
                    messanger.append(chatter.getAuthor() + ": " + chatter.getMessage() + "\n");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        messageText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && messageText.getText().toString().trim().length() > 0) {
                    if (messageText.getText().charAt(0) == '/') {
                        //Command
                        if (messageText.getText().toString().toLowerCase().contains("clear"))
                            messanger.setText("");
                        else if (messageText.getText().toString().toLowerCase().contains("text") &&
                                messageText.getText().toString().toLowerCase().contains("color"))
                            try {
                                messanger.setTextColor(Color.parseColor(messageText.getText().toString().substring(12).toUpperCase()));
                            } catch (Exception e) {
                            }
                        else
                            messanger.append("[That is not a valid command]");
                    } else
                        myFirebaseRef.push().setValue(new Chat(messageText.getText().toString(), userName));

                    messageText.setText("");
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void moveHome(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
