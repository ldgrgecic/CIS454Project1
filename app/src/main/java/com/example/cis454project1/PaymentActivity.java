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
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

// Class activity for the payment screen
public class PaymentActivity extends AppCompatActivity {

    // The TAG that will be used in database logs, refers to which activity was used.
    private static final String TAG = "PaymentActivity";

    // TextView variables that are used for displaying the textbook's information from the database into the UI.
    // One EditText variable for credit card number
    private TextView mtitle;
    private TextView mauthor;
    private TextView misbn;
    private TextView mprice;
    private EditText cardNum;

    // Textbook object that will defined later, instantiated globally to allow for every method in this class to use it
    private Textbook book;

    // Database reference to use throughout this program that will allow for access to our database
    DatabaseReference myRef;

    // Credit card string
    String card;

    // Main create function that begins with a saved instance and sets the view to the activity_payment XML file
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Make a new GSON object and read in the information that was passed from the previous intent
        // This is done to get the exact textbook that was passed through from the BuyBook class to here
        // This allows for a universally UI that can update depending on what was passed on with the intent
        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("info");

        // Pass in the object from the previous intent to our own book variable for ease of use accessing its attributes
        book = gson.fromJson(strObj, Textbook.class);

        // Create a database reference to only the "textbooks" that we will use for grabbing information
        myRef = FirebaseDatabase.getInstance().getReference("textbooks");

        // Read in the ViewText fields that we will load the database information into
        mtitle = findViewById(R.id.paytitle);
        mauthor = findViewById(R.id.payauthor);
        misbn = findViewById(R.id.payisbn);
        mprice = findViewById(R.id.payprice);
        cardNum = findViewById(R.id.creditcard);

        // Set the textbook's attributes to the ViewText fields
        mtitle.setText(book.title);
        mauthor.setText(book.author);
        misbn.setText(book.isbn);
        mprice.setText(Double.toString(book.price));



    }

    // Method that is invoked when the user clicks on the "Confirm Listing" button
    public void confirmPayment(View view) {

        // Boolean that will be used to test if a field is empty, if so we will cancel the listing
        boolean cancel = false;
        // View that will be highlighted if it is empty, starts null and will update if needed
        View focusView = null;

        // Read the card number information filled in by the user and set its error to null
        card = cardNum.getText().toString();
        cardNum.setError(null);

        // Test case to see if each field is empty or not
        // If a field is empty, set the error to alert the user and set the focused view to current view
        // This will allow for user to see field is empty, then set cancel to True to cancel the purchase
        if (TextUtils.isEmpty(card)) {
            cardNum.setError(getString(R.string.error_field_required));
            focusView = cardNum;
            cancel= true;
        }

        // If every field is full then do the following:
        else {

            // Inflate the pop-up window layout to fit the screen using built in APIs
            LayoutInflater inflater = (LayoutInflater)
                    getSystemService(LAYOUT_INFLATER_SERVICE);

            // Choose which view to inflate
            View popupView = inflater.inflate(R.layout.confirmpurchase, null);

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

                    // Remove this entry from the database
                    myRef.child(book.isbn).removeValue();

                    // Create a new intent that will jump back to the main activity
                    Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                    startActivity(intent);

                    // Return true to confirm the following above happened without error
                    return true;
                }
            });

        }

    }
}
