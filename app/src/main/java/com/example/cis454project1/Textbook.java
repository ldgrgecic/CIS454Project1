package com.example.cis454project1;

// Textbook class
public class Textbook {

    // Textbook attributes that are needed
    public String title;
    public String isbn;
    public String author;
    public double price;

    // Main constructor
    public Textbook(String ttl, String in, String aut, double pc) {
        title = ttl;
        isbn = in;
        author = aut;
        price = pc;
    }

    // Empty constructor for blank instances
    public Textbook(){ }

    // Get methods for the respective attributes
    public String getTitle() {
        return title;
    }

    public String getISBN() { return isbn; }

    public String getAuthor() { return author;}

    public double getPrice() { return price;}

}

