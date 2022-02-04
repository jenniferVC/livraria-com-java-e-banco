/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAL;

import Models.Author;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DAOAuthor {
    
    ConectionDB cdb = new ConectionDB();

    //incluir
    protected void incluir(Author a) throws SQLException {
        cdb.conectar();
        
        String query = "INSERT INTO livraria.author (name) "
                + "VALUES (?)";
        PreparedStatement prep = cdb.getConnection().prepareStatement(
                query,
                Statement.RETURN_GENERATED_KEYS); //retorna as chaves primárias que foram geradas
        prep.setString(1, a.getName());
        prep.execute();
        
        ResultSet ultimoID = prep.getGeneratedKeys();
        ultimoID.next();
        a.setAuid(ultimoID.getInt(1));
        
        cdb.getConnection().commit();
        cdb.close(); //fecha conexão para não ficar conectado o tempo todo
    }

    //alterar - a model deve estar com o id preenchido
    protected void alterar(Author a) throws SQLException {
        cdb.conectar();
        try {
            String query = "UPDATE livraria.author "
                    + "SET name=? WHERE auid=?";
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);
            
            prep.setInt(2, a.getAuid()); //alterando pelo ID
            prep.setString(1, a.getName());
            prep.execute();
            
            cdb.getConnection().commit();
            cdb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //excluir
    protected void excluir(Author a) {
        cdb.conectar();
        try {
            String query = "DELETE FROM livraria.author "
                    + "WHERE auid=?";
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);
            
            prep.setInt(1, a.getAuid());
            prep.execute();
            
            cdb.getConnection().commit();
            cdb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //consultar
    protected int consultarPorNome(Author a) {
        cdb.conectar();
        int idTmp = -1;
        String query = "SELECT * from livraria.author "
                + "WHERE name = ?";
        try {
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);
            prep.setString(1, a.getName());
            
            ResultSet list = prep.executeQuery();
            
            while (list.next()) {
                idTmp = list.getInt("auid");
                break;
            }
            cdb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        a.setAuid(idTmp);
        return idTmp;
    }
    
    protected List listar(String params) {
        cdb.conectar();
        List<Author> listaAutores = new ArrayList<>();
        String query = "SELECT * from livraria.author " + params;
        
        try {
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);
            ResultSet lista = prep.executeQuery();
            
            while (lista.next()) {
                Author a = new Author();
                a.setAuid(lista.getInt("auid"));
                a.setName(lista.getString("name"));
                listaAutores.add(a);
            }
            cdb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaAutores;
    }
    
}
