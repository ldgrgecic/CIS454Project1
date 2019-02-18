package com.example.cis454project1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

// Class activity that allows for the user to buy a book
public class BuyBook extends AppCompatActivity {

    // ArrayLists that will be used for storing textbook objects and simply the titles of the textbooks respectively
    ArrayList<Textbook> textbooks = new ArrayList<Textbook>();
    ArrayList<String> titles = new ArrayList<String>(0);

    // ListView that allows for our screen to have list elements and update accordingly
    ListView listView;

    // ArrayAdapter which allows us to interact with an array list. Required for a ListView
    ArrayAdapter<String> arrayAdapter;

    // Textbook object that will defined later, instantiated globally to allow for every method in this class to use it
    Textbook textbook;

    // The TAG that will be used in database logs, refers to which activity was used.
    private static final String TAG = "BuyBookActivity";

    // Main function to create a saved instance and set the view to the activity_buy XML file
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        // Create a database reference to only the "textbooks" that we will use for grabbing information
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("textbooks");


        // Set the list view to the view of our XML file, reallistview.
        listView = findViewById(R.id.reallistview);

        // Instantiate the arrayAdapted with the current context (this), the layout for a simple item,
        // and the list we will use to fill in the items.
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,titles);

        // Set the adapted for our list view to the newly made adapter
        listView.setAdapter(arrayAdapter);

        // Function that waits for a user to click on an item from our list and will redirect them accordingly
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            // Passing in the current view, position, and id to reference the correct object
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Use a for loop to go through each list item and get it's location on the screen
                // Set the background to transparent so user can see the items
                for (int j = 0; j < parent.getChildCount(); j++)
                    parent.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);

                // Change the background color of the selected element
                view.setBackgroundColor(Color.LTGRAY);

                // The integer that refers to the position of the currently tapped item
                int selectedItemIndex = position;

                // Create a new intent that will move us to the Payment Activity class
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);

                // Pass the textbook we are referring to into the intent as a JSON format
                // Use GSON package to do this for us, give it the name info to refer to later
                intent.putExtra("info", (new Gson()).toJson(textbooks.get(position)));

                // Choose exactly which position we are putting into the next activity
                intent.putExtra("selectedTemplateIndex", selectedItemIndex);

                // Begin the next activity with the given result
                startActivityForResult(intent, 100);
            }
        });

        // Create an event listener that will wait read data from the database
        myRef.addValueEventListener(new ValueEventListener() {

            // Function that will activate with the initial value and then again whenever that data is updated
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Use a for loop to get each child from the "textbooks" child of the database
                // Used to make sure every textbook is displayed
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        // Make the textbook instance we create the value of the current iteration
                        textbook = ds.getValue(Textbook.class);
                        // Add the textbook instance to our list of textbooks
                        textbooks.add(textbook);
                        // Add the textbook title to the titles list which is what is displayed on the screen
                        titles.add(textbook.title);
                        // Notify the adapter that there has been a data change (addition in this case)
                        arrayAdapter.notifyDataSetChanged();
                }

            }

            // If the database cannot be read, add this error to the log
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });




    }
}
