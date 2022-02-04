/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAL;

import Models.Author;
import Models.Book;
import Models.Publisher;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DAOBook {

    ConectionDB cdb = new ConectionDB();

    //incluir
    public void incluir(Book book) throws SQLException {
        cdb.conectar();

        String query = "INSERT INTO livraria.books (titulo,author,email, fkpuid, fkauid) "
                + "VALUES (?,?,?,?,?)";
        PreparedStatement prep = cdb.getConnection().prepareStatement(
                query,
                Statement.RETURN_GENERATED_KEYS); //retorna a última ID gerada
        prep.setString(1, book.getTitulo());
        prep.setString(2, book.getAuthor().getName());
        prep.setString(3, book.getEmail());
        prep.setInt(4, book.getPublisher().getPuid());
        prep.setInt(5, book.getAuthor().getAuid());
        prep.execute();

        cdb.getConnection().commit();
        cdb.close(); //fecha conexão para não ficar conectado o tempo todo
    }

    //alterar - a model deve estar com o id preenchido
    public void alterar(Book book) throws SQLException {
        cdb.conectar();
        try {
            String query = "UPDATE livraria.books "
                    + "SET titulo=?, author=?, email=? WHERE boid=?";
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);

            prep.setInt(4, book.getBoid());
            prep.setString(1, book.getTitulo());
            prep.setString(2, book.getAuthor().getName());
            prep.setString(3, book.getEmail());
            prep.execute();

            cdb.getConnection().commit();
            cdb.close(); //fecha conexão para não ficar conectado o tempo todo
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //excluir
    public void excluir(int id) {
        cdb.conectar();
        try {
            String query = "DELETE FROM livraria.books "
                    + "WHERE boid=?";
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);

            prep.setInt(1, id);
            prep.execute();

            cdb.getConnection().commit();
            cdb.close(); //fecha conexão para não ficar conectado o tempo todo
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //consultar
    public Book consultarPorID(int idbook) {
        cdb.conectar();

        Book book = new Book();
        Author au = new Author();
        Publisher pu =new Publisher();
        int idTmp = -1;
        String tituloTmp = "";
        String emailTmp = "";
        String query = "SELECT * from livraria.books "
                + "WHERE boid = ?";
        try {
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);
            prep.setInt(1, idbook);

            ResultSet list = prep.executeQuery(); //executeQuery pois está executando SELECT

            while (list.next()) {
                idTmp = list.getInt("boid");
                tituloTmp = list.getString("titulo");
                au.setName(list.getString("author"));
                emailTmp = list.getString("email");
                pu.setPuid(list.getInt("fkpuid"));
                break;
            }
            cdb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        book.setBoid(idTmp);
        book.setAuthor(au);
        book.setPublisher(pu);
        book.setEmail(emailTmp);
        return book;
    }

    public List listar(String params) {
        cdb.conectar();
        List<Book> listaLivros = new ArrayList<>();
        String query = "SELECT * from livraria.books " + params; //params para uso de WHERE

        try {
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);
            ResultSet lista = prep.executeQuery();

            while (lista.next()) {
                Book book = new Book();
                book.setBoid(lista.getInt("boid"));
                book.setTitulo(lista.getString("titulo"));
                Author autor = new Author();
                autor.setName(lista.getString("author"));
                book.setAuthor(autor);
                book.setEmail(lista.getString("email"));
                Publisher publicadora = new Publisher();
                publicadora.setPuid(lista.getInt("fkpuid"));
                autor.setAuid(lista.getInt("fkauid"));

                listaLivros.add(book);
                //break;
            }
            cdb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaLivros;
    }

}
