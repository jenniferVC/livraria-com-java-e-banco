/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exemplo1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class App {

    public static void main(String[] argv){

        // Connection Configuration
        Properties connConfig = new Properties();
        connConfig.setProperty("user", "root");
        connConfig.setProperty("password", "123");
        //connConfig.setProperty("useSsl", "true");
        //connConfig.setProperty("serverSslCert", "/path/to/ca-bundle.pem");

        // Create Connection to MariaDB Enterprise
        try (Connection conn = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3308", connConfig)) {

            // Disable Auto-Commit
            conn.setAutoCommit(false);

            // Use Statement to Create library.books table
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(
                        "CREATE TABLE IF NOT EXISTS library.books ("
                        + "id INT PRIMARY KEY AUTO_INCREMENT,"
                        + "title VARCHAR(25),"
                        + "author VARCHAR(25),"
                        + "email VARCHAR(100))"
                        + "ENGINE=InnoDB;");

                // Prepare INSERT Statement to Add Books
                try (PreparedStatement prep = conn.prepareStatement(
                        "INSERT INTO library.books (title, author, email) VALUES (?, ?, ?)", //(1,2,3)
                        Statement.RETURN_GENERATED_KEYS)) {

                    // Add book
                    prep.setString(1, "Cem anos de solidao"); //1 é a primeira interrogação da linha 49 (1,?,?) substituindo por essa frase
                    prep.setString(2, "Gabriel Garcia Marques"); //2 é a segunda interrogação da linha 49 (1,2,?) substituindo por essa frase
                    prep.setString(3, "editoraplaneta@gmail.com"); //injecao de sql
                    prep.addBatch(); //addbatch adiciona na lista 

                    // Add book
                    prep.setString(1, "Os trabalhadores do mar");
                    prep.setString(2, "Victor Hugo");
                    prep.setString(3, "teste@teste.com");
                    prep.addBatch();

                    // Add book
                    prep.setString(1, "Biblia");
                    prep.setString(2, "Vários");
                    prep.setString(3, "teste1@teste1.com");
                    prep.addBatch();

                    // Execute Prepared Statements in Batch
                    System.out.println("Batch Counts");
                    int[] updateCounts = prep.executeBatch(); //executeBatch executa de uma só vez todos os batch's feitos e faz uma contagem 
                    for (int count : updateCounts) {

                        // Print Counts
                        System.out.println(count);
                    }
                }

                // Prepare UPDATE Statement
                try (PreparedStatement prep = conn.prepareStatement(
                        "UPDATE library.books "
                        + "SET email = ? WHERE id = ?")) { //interrogação vai ser substituido, 

                    // Change Email Address
                    prep.setString(1, "editoraplanetaXX@gmail.com"); //primeira interrogação 1 

                    // Change ID
                    prep.setInt(2, 1); //segunda interrogação = 2, 
                    prep.execute();
                }

                // Prepare DELETE Statement
                try (PreparedStatement prep = conn.prepareStatement(
                        "DELETE FROM library.books "
                        + "WHERE id = ?")) {

                    // ID of Row to Remove
                    prep.setInt(1, 3);
                    prep.execute(); //retorna o status, se conseguiu fazer a operação ou não
                }

                // Commit Changes
                conn.commit(); //persiste as mudanças feitas no Banco

                // Execute a SELECT Statement
                ResultSet book_list = stmt.executeQuery( //executeQuery É SÓ PRA SELECT, pois é especializado para retornar o objeto que está sendo retornado pelo ResultSet
                        "SELECT title, author, email "
                        + "FROM library.books");

                // Iterate over the Result-set
                System.out.println("Books:");
                while (book_list.next()) {

                    // Print Row
                    System.out.println(
                            String.format(
                                    "- %s %s <%s>",
                                    book_list.getString("title"),
                                    book_list.getString("author"),
                                    book_list.getString("email")));
                }
            } // Catch SQL Exceptions for Queries
            catch (SQLException exc) {
                exc.printStackTrace();
                conn.rollback();
            }
        } // Catch SQL Exceptions from Connection
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
