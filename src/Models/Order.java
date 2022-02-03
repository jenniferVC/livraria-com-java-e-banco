package Models;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private int orID;
    private String date;
    private User user;
    private Book book;
    private ShippingInfo shippinginfo;
    private BillingInfo billinginfo;
    List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
    }

    public boolean removeBook(Book book) {
        return books.remove(book);
    }

    public int getOrID() {
        return orID;
    }

    public void setOrID(int orID) {
        this.orID = orID;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public ShippingInfo getShippinginfo() {
        return shippinginfo;
    }

    public void setShippinginfo(ShippingInfo si) {
        this.shippinginfo = si;
    }

    public BillingInfo getBillinginfo() {
        return billinginfo;
    }

    public void setBillinginfo(BillingInfo bi) {
        this.billinginfo = billinginfo;
    }

}
