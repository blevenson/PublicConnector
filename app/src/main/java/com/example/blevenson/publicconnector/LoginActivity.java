package com.example.blevenson.publicconnector;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private Spinner selector;

    private Button joinButton;
    private Button createChat;

    private TextView message;
    private EditText userName;

    private String chatRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        selector = (Spinner)findViewById(R.id.roomSelector);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.chatroom_array, android.R.layout.simple_spinner_item);
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

        userName = (EditText)findViewById(R.id.userName);
        message = (TextView)findViewById(R.id.loginMessage);

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
}
