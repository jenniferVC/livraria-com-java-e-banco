/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAL;

import Models.Account;
import Models.BillingInfo;
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
public class DAOBillingInfo {

    ConectionDB cdb = new ConectionDB();

    //incluir
    protected void incluir(BillingInfo bi) throws SQLException {
        cdb.conectar();

        String query = "INSERT INTO livraria.billinginfo (email,pagamento) "
                + "VALUES (?,?)";
        PreparedStatement prep = cdb.getConnection().prepareStatement(
                query,
                Statement.RETURN_GENERATED_KEYS); //retorna as chaves primárias que foram geradas
        prep.setString(1, bi.getAccount().getEmailAddress());
        prep.setString(2, bi.getPagamento());
        prep.execute();

        /*ResultSet ultimoID = prep.getGeneratedKeys();
        ultimoID.next();
        si.(ultimoID.getInt(1));*/
        cdb.getConnection().commit();
        cdb.close(); //fecha conexão para não ficar conectado o tempo todo
    }

    protected void alterar(String pagamento, String email) throws SQLException {
        cdb.conectar();
        try {
            String query = "UPDATE livraria.billinginfo "
                    + "SET pagamento=? WHERE email=?";
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);
            prep.setString(2, pagamento);
            prep.setString(1, email);
            prep.execute();

            cdb.getConnection().commit();
            cdb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //excluir
    protected void excluir(String email) {
        cdb.conectar();
        try {
            String query = "DELETE FROM livraria.billinginfo "
                    + "WHERE email=?";
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);

            prep.setString(1, email);
            prep.execute();

            cdb.getConnection().commit();
            cdb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected String consultarPorEmail(String email) {
        cdb.conectar();

        Account bi = new Account();
        String emailTmp = "";
        String query = "SELECT * from livraria.billinginfo "
                + "WHERE email = ?";
        try {
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);
            prep.setString(1, email);

            ResultSet list = prep.executeQuery();

            while (list.next()) {
                emailTmp = list.getString("email");
                break;
            }
            cdb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        bi.setEmailAddress(emailTmp);
        return emailTmp;
    }

    protected List listar(String params) {
        cdb.conectar();
        List<BillingInfo> listaPagamento = new ArrayList<>();
        String query = "SELECT * from livraria.billinginfo " + params;

        try {
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);
            ResultSet lista = prep.executeQuery();

            while (lista.next()) {
                BillingInfo si = new BillingInfo();
                si.setPagamento(lista.getString("pagamento"));
                Account ac = new Account();
                ac.setEmailAddress(lista.getString("email"));
                si.setAccount(ac);
                listaPagamento.add(si);
            }
            cdb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaPagamento;
    }

}
