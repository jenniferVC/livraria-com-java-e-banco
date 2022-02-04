/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;


public class Book {

    private int boid;
    private String titulo;
    private Author author;
    private Publisher publisher;
    private String email;
    

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {    
        this.titulo = titulo;
    }

    public int getBoid() {
        return boid;
    }

    public void setBoid(int boid) {
        this.boid = boid;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
