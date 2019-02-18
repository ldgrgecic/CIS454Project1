package com.example.cis454project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

// Main activity that the other two activities, BuyBook and SellBook, stem from
public class MainActivity extends AppCompatActivity {

    // Creation function that will set a instance and then set the view to the activity_main view (XML file found in res/layout)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    // Method that is activated when the "Sell Book" button is clicked on the main screen
    public void sellBook(View view){
        // Create a new intent that will  move to the SellBook class activity
        Intent intent = new Intent(this, SellBook.class);

        // Start the activity with the intent mentioned above (jump to SellBook.java)
        startActivity(intent);
    }
    public void buyBook(View view){
        // Create a new intent that will  move to the BuyBook class activity
        Intent intent = new Intent(this, BuyBook.class);

        // Start the activity with the intent mentioned above (jump to BuyBook.java)
        startActivity(intent);
    }
}
