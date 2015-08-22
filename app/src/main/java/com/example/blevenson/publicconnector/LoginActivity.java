package com.example.blevenson.publicconnector;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "MyPrefsFile";
    private static Set<String> favorites = new HashSet<String>();

    private Spinner selector;

    private Button joinButton;
    private Button createChat;

    private TextView message;
    private EditText userName;

    private String chatRoom;

    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        readFavs();

        Log.v("Fave", ("Favorites: " + favorites.toString()));

        userName = (EditText)findViewById(R.id.userName);
        message = (TextView)findViewById(R.id.loginMessage);

        Intent intent = getIntent();
        userName.setText(intent.getStringExtra("name"));

        createChat = (Button)findViewById(R.id.createChatbutton);
        createChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.getText().toString().trim().length() <= 0) {
                    message.setTextColor(Color.RED);
                    message.setTextSize(20);
                    message.setText("Please enter a user name");
                    return;
                }else{
                    message.setTextColor(Color.BLACK);
                    message.setTextSize(50);
                    message.setText("Login");
                }
                moveToCreateChatActivity();
            }
        });

        joinButton = (Button)findViewById(R.id.joinButton);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(userName.getText().toString().trim().length() <= 0) {
                    message.setTextColor(Color.RED);
                    message.setTextSize(20);
                    message.setText("Please enter a user name");
                    return;
                }else{
                    message.setTextColor(Color.BLACK);
                    message.setTextSize(50);
                    message.setText("Login");
                }

                if(chatRoom != null) {
                    //Move to main activty
                    moveToMainActivity();
                }
            }
        });
    }


    protected void onStart(){
        super.onStart();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("FavChatRooms", "Room 1");
        editor.commit();

        selector = (Spinner)findViewById(R.id.roomSelector);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.chatroom_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                new ArrayList<String>(favorites));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selector.setAdapter(adapter);


        selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chatRoom = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void moveToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userName", userName.getText().toString());
        intent.putExtra("chatRoom", chatRoom);
        startActivity(intent);
    }

    private void moveToCreateChatActivity(){
        Intent intent = new Intent(this, CreateChatRoom.class);
        intent.putExtra("userName", userName.getText().toString());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

        return super.onOptionsItemSelected(item);
    }

    public static void addToFavs(String in){
        Firebase myFirebaseRef = new Firebase("https://publicconnector.firebaseio.com/");

        while(favorites.size() > 2){
            String leaving = favorites.iterator().next();
            favorites.remove(leaving);
            myFirebaseRef.child(leaving).removeValue();
        }
        Log.v("Fave", ("Added to favorites:" + in));
        favorites.add(in);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveFavs();
    }

    private void readFavs(){
        SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE);
        favorites = settings.getStringSet("FavRooms", new HashSet<String>(Arrays.asList("Main")));
    }

    private void saveFavs(){
        SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, MODE_PRIVATE);
        editor = settings.edit();

        editor.putStringSet("FavRooms", favorites);
        editor.commit();
    }

    private void requestPassword(){
        Bundle bundle = new Bundle();
        bundle.putString("roomName", chatRoom);

        DialogFragment newFragment = new PasswordFragment();
        newFragment.setArguments(bundle);

        newFragment.show(getSupportFragmentManager(), "password");
    }
}
