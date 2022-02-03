/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAL;

import Models.Account;
import Models.Shipper;
import Models.ShippingInfo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Familia
 */
public class DAOShippingInfo {

    ConectionDB cdb = new ConectionDB();

    //incluir
    protected void incluir(ShippingInfo si) throws SQLException {
        cdb.conectar();

        String query = "INSERT INTO livraria.shippinginfo (endereco,shipper) "
                + "VALUES (?,?)";
        PreparedStatement prep = cdb.getConnection().prepareStatement(
                query,
                Statement.RETURN_GENERATED_KEYS); //retorna as chaves primárias que foram geradas
        prep.setString(1, si.getAddress());
        prep.setString(2, si.getShipper().getName());
        prep.execute();

        /*ResultSet ultimoID = prep.getGeneratedKeys();
        ultimoID.next();
        si.(ultimoID.getInt(1));*/

        cdb.getConnection().commit();
        cdb.close(); //fecha conexão para não ficar conectado o tempo todo
    }

    protected void alterar(ShippingInfo si) throws SQLException {
        cdb.conectar();
        try {
            String query = "UPDATE livraria.shippinginfo "
                    + "SET endereco=? WHERE shipper=?";
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);

            prep.setString(1, si.getAddress());
            prep.setString(2, si.getShipper().getName());
            prep.execute();

            cdb.getConnection().commit();
            cdb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //excluir
    protected void excluir(ShippingInfo si) {
        cdb.conectar();
        try {
            String query = "DELETE FROM livraria.shippinginfo "
                    + "WHERE shipper=?";
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);

            prep.setString(1, si.getShipper().getName());
            prep.execute();

            cdb.getConnection().commit();
            cdb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected String consultarPorEndereco(String endereco) {
        cdb.conectar();

        ShippingInfo si = new ShippingInfo();
        String enderecoTmp = "";
        String query = "SELECT * from livraria.shippinginfo "
                + "WHERE endereco = ?";
        try {
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);
            prep.setString(1, endereco);

            ResultSet list = prep.executeQuery();

            while (list.next()) {
                enderecoTmp = list.getString("endereco");
                break;
            }
            cdb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        si.setAddress(enderecoTmp);
        return enderecoTmp;
    }


    protected List listar(String params) {
        cdb.conectar();
        List<ShippingInfo> listaEntrega = new ArrayList<>();
        String query = "SELECT * from livraria.shippinginfo " + params;

        try {
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);
            ResultSet lista = prep.executeQuery();

            while (lista.next()) {
                ShippingInfo si = new ShippingInfo();
                si.setAddress(lista.getString("endereco"));
                Shipper s = new Shipper();
                s.setName(lista.getString("shipper"));
                si.setShipper(s);
                listaEntrega.add(si);
            }
            cdb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaEntrega;
    }

}
