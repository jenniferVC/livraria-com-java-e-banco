/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAL;

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

/**
 *
 * @author Familia
 */
public class DAOPublisher {

    ConectionDB cdb = new ConectionDB();

    //incluir
    public void incluir(Publisher u) throws SQLException {
        cdb.conectar();

        String query = "INSERT INTO livraria.publisher (name,endereco) "
                + "VALUES (?,?)";
        PreparedStatement prep = cdb.getConnection().prepareStatement(
                query,
                Statement.RETURN_GENERATED_KEYS); //retorna as chaves primárias que foram geradas
        prep.setString(1, u.getName());
        prep.setString(2, u.getEndereco());
        prep.execute();

        ResultSet ultimoID = prep.getGeneratedKeys();
        ultimoID.next();
        u.setPuid(ultimoID.getInt(1));

        cdb.getConnection().commit();

        cdb.close(); //fecha conexão para não ficar conectado o tempo todo
    }

    //alterar - a model deve estar com o id preenchido
    public void alterar(Publisher u) throws SQLException {
        cdb.conectar();
        try {
            String query = "UPDATE livraria.publisher "
                    + "SET name=?, endereco=? WHERE puid=?";
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);

            prep.setInt(3, u.getPuid()); //alterando pelo ID
            prep.setString(1, u.getName());
            prep.setString(2, u.getEndereco());

            prep.execute();

            cdb.getConnection().commit();
            cdb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //excluir
    public void excluir(Publisher u) {
        cdb.conectar();
        try {
            String query = "DELETE FROM livraria.publisher "
                    + "WHERE puid=?";
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);

            prep.setInt(1, u.getPuid());
            prep.execute();

            cdb.getConnection().commit();
            cdb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //consultar
   protected Publisher consultarPorID(String id) {
        cdb.conectar();

        Publisher pub = new Publisher();
        int idTmp = -1;
        String nomeTmp = "";
        String enderecoTmp="";
        String query = "SELECT * from livraria.publisher "
                + "WHERE puid = ?";
        try {
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);
            prep.setString(1, id);

            ResultSet list = prep.executeQuery(); //executeQuery pois está executando SELECT

            while (list.next()) {
                idTmp = list.getInt("puid");
                nomeTmp=list.getString("name");
                enderecoTmp = list.getString("endereco");
                break;
            }
            cdb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pub.setPuid(idTmp);
        pub.setName(nomeTmp);
        pub.setEndereco(enderecoTmp);
        return pub;
    }

    public List listar(String params) {
        cdb.conectar();
        List<Publisher> listaPublicadoras = new ArrayList<>();
        String query = "SELECT * from livraria.publisher " + params;

        try {
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);
            ResultSet lista = prep.executeQuery();

            while (lista.next()) {
                Publisher u = new Publisher();
                u.setPuid(lista.getInt("puid"));
                u.setName(lista.getString("name"));
                u.setEndereco(lista.getString("endereco"));

                listaPublicadoras.add(u);
            }
            cdb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaPublicadoras;
    }
}
