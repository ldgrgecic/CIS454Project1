package com.example.cis454project1;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;

// Class activity for selling a book
public class SellBook extends AppCompatActivity {

    // The TAG that will be used in database logs, refers to which activity was used.
    private static final String TAG = "SellBookActivity";

    // EditText variables that are used for reading in the users input in the respective edit text fields
    private EditText bookTitle;
    private EditText bookAuthor;
    private EditText bookISBN;
    private EditText bookPrice;

    // Database reference that will be used throughout this class which will refer to our Firebase
    DatabaseReference myRef;

    // String variables that will store the respective data after being converted to strings (from user input to string)
    String title;
    String author;
    String isbn;
    String price;

    // Main create function that begins with a saved instance and sets the view to the activity_sell_book XML file
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_book);

        // Give the reference an instance of our database and get a reference to it so we can write / read with it
        myRef = FirebaseDatabase.getInstance().getReference();

        // Read in the EditText fields that the user filled out and store in the respective EditText variables
        bookTitle = findViewById(R.id.sellbooktitle);
        bookAuthor = findViewById(R.id.sellbookauthor);
        bookISBN = findViewById(R.id.sellbookisbn);
        bookPrice = findViewById(R.id.sellbookprice);

    }

    // Method that is invoked when the user clicks on the "Confirm Listing" button
    public void onTouch(View view) {

        // Boolean that will be used to test if a field is empty, if so we will cancel the listing
        boolean cancel = false;
        // View that will be highlighted if it is empty, starts null and will update if needed
        View focusView = null;

        // Converting the EditText fields to strings so we can use string functions on them and test for emptiness
        title = bookTitle.getText().toString();
        author = bookAuthor.getText().toString();
        isbn = bookISBN.getText().toString();
        price = bookPrice.getText().toString();

        // Set the errors to null to start off, will update if there is an error.
        bookTitle.setError(null);
        bookAuthor.setError(null);
        bookISBN.setError(null);
        bookPrice.setError(null);

        // Test cases to see if each field is empty or not
        // If a field is empty, set the error to alert the user and set the focused view to current view
        // This will allow for user to see field is empty, then set cancel to True to cancel the listing.
            if (TextUtils.isEmpty(title)) {
                bookTitle.setError(getString(R.string.error_field_required));
                focusView = bookTitle;
                cancel= true;
            }
            if (TextUtils.isEmpty(author)) {
                bookAuthor.setError(getString(R.string.error_field_required));
                focusView = bookAuthor;
                cancel = true;
            }
            if (TextUtils.isEmpty(isbn)) {
                bookISBN.setError(getString(R.string.error_field_required));
                focusView = bookISBN;
                cancel = true;
            }
            if (TextUtils.isEmpty(price)) {
                bookPrice.setError(getString(R.string.error_field_required));
                focusView = bookPrice;
                cancel = true;
            }
            // If one of the fields is empty, cancel the listing and show the user which field is empty
            if (cancel) {
                focusView.requestFocus();
            }
            // If every field is full then do the following:
            else {

                // Convert the string price into a double (database sees price as a double)
                double price2 = Double.parseDouble(bookPrice.getText().toString());

                // Create a new instance of a Textbook and give it the appropriate parameters
                Textbook book = new Textbook(title,isbn,author,price2);

                // Create a new field in the database under textbooks and insert our new book into DB with it's ISBN as identifier
                myRef.child("textbooks").child(isbn).setValue(book);

                // Inflate the pop-up window layout to fit the screen using built in APIs
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);

                // Choose which view to inflate
                View popupView = inflater.inflate(R.layout.confirmlisting, null);

                // Create the pop-up window using appropriate parameters (standard parameters for Android)
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;

                // Set it so only clicking on the pop-up window will dismiss it
                boolean focusable = false;

                // Officially create the window instance
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // Display the popup window, the parameters are standard are not edited for this program
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                // Set up a short function that will activate once it is tapped on
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // Dismiss the window
                        popupWindow.dismiss();

                        // Create a new intent that will jump back to the main activity
                        Intent intent = new Intent(SellBook.this, MainActivity.class);
                        startActivity(intent);

                        // Return true to confirm the following above happened without error
                        return true;
                    }
                });

            }
        }
    }

