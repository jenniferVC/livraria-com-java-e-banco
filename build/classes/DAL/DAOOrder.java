/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAL;

import Models.Order;
import Models.OrderItens;
import Models.User;
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
public class DAOOrder {

    ConectionDB cdb = new ConectionDB();

    //incluir
    public void incluirOrder(Order o) throws SQLException {
        cdb.conectar();

        String query = "INSERT INTO livraria.order (orid, date, fkusid,endereco) "
                + "VALUES (?,?,?,?)";
        PreparedStatement prep = cdb.getConnection().prepareStatement(
                query,
                Statement.RETURN_GENERATED_KEYS); //retorna as chaves primárias que foram geradas
        prep.setInt(1, o.getOrID());
        prep.setString(2, o.getDate());
        prep.setInt(3, o.getUser().getUsid());
        prep.setString(4, o.getShippinginfo().getAddress());
        //prep.setString(5, o.getBillinginfo().getText());
        prep.execute();
        cdb.getConnection().commit();
        ResultSet ultimoID = prep.getGeneratedKeys();
        ultimoID.next();
        o.setOrID(ultimoID.getInt(1));

        cdb.close(); //fecha conexão para não ficar conectado o tempo todo
    }

    public void incluirOrderItens(OrderItens oi) throws SQLException {
        cdb.conectar();

        String query1 = "INSERT INTO livraria.orderitens (fkorid, fkboid, qtde) "
                + "VALUES (?,?,?)";
        PreparedStatement prep1 = cdb.getConnection().prepareStatement(
                query1,
                Statement.RETURN_GENERATED_KEYS); //retorna as chaves primárias que foram geradas

        prep1.setInt(1, oi.getOrder().getOrID());
        prep1.setInt(2, oi.getBooks().getBoid());
        prep1.setInt(3, oi.getQtde());
        prep1.execute();

        cdb.getConnection().commit();
        cdb.close(); //fecha conexão para não ficar conectado o tempo todo
    }

    //alterar - a model deve estar com o id preenchido
    public void alterar(Order o) throws SQLException {
        cdb.conectar();
        try {
            String query = "UPDATE livraria.user "
                    + "SET name=? WHERE usid=?";
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);

            // prep.setInt(2, u.getUsid()); //alterando pelo ID
            //prep.setString(1, u.getName());
            prep.execute();

            cdb.getConnection().commit();
            cdb.close(); //fecha conexão para não ficar conectado o tempo todo
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //excluir
    public void excluir(User u) {
        cdb.conectar();
        try {
            String query = "DELETE FROM livraria.user "
                    + "WHERE usid=?";
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);

            prep.setInt(1, u.getUsid());
            prep.execute();

            cdb.getConnection().commit();
            cdb.close(); //fecha conexão para não ficar conectado o tempo todo
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //consultar
    public int consultarPorNome(User u) {
        cdb.conectar();
        int idTmp = -1;
        String query = "SELECT * from livraria.user "
                + "WHERE name = ?";
        try {
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);
            prep.setString(1, u.getName());

            ResultSet list = prep.executeQuery();

            while (list.next()) {
                idTmp = list.getInt("usid");
                break;
            }
            cdb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        u.setUsid(idTmp);
        return idTmp;
    }

    public List listar(String params) {
        cdb.conectar();
        List<User> listaUsuarios = new ArrayList<>();
        String query = "SELECT * from livraria.user " + params;

        try {
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);
            ResultSet lista = prep.executeQuery();

            while (lista.next()) {
                User u = new User();
                u.setUsid(lista.getInt("usid"));
                u.setName(lista.getString("name"));
                listaUsuarios.add(u);
            }
            cdb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaUsuarios;
    }
}
