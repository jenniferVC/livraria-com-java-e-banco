package DAL;

import Models.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DAOUser {

    ConectionDB cdb = new ConectionDB();

    //incluir
    public void incluir(User u) throws SQLException {
        cdb.conectar();

        String query = "INSERT INTO livraria.user (usid, name) "
                + "VALUES (?,?)";
        PreparedStatement prep = cdb.getConnection().prepareStatement(
                query,
                Statement.RETURN_GENERATED_KEYS); //retorna as chaves primárias que foram geradas
        prep.setInt(1, u.getUsid());
        prep.setString(2, u.getName());
        prep.execute();

        ResultSet ultimoID = prep.getGeneratedKeys();
        ultimoID.next();
        u.setUsid(ultimoID.getInt(1));

        cdb.getConnection().commit();
        cdb.close(); //fecha conexão para não ficar conectado o tempo todo
    }

    //alterar - a model deve estar com o id preenchido
    public void alterar(User u) throws SQLException {
        cdb.conectar();
        try {
            String query = "UPDATE livraria.user "
                    + "SET name=? WHERE usid=?";
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);

            prep.setInt(2, u.getUsid()); //alterando pelo ID
            prep.setString(1, u.getName());
            prep.execute();

            cdb.getConnection().commit();
            cdb.close(); //fecha conexão para não ficar conectado o tempo todo
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //excluir
    public void excluir(int id, int idOI) {
        cdb.conectar();
        try {
            String query1 = "DELETE FROM livraria.orderitens "
                    + "WHERE fkorid=?";
            PreparedStatement prep = cdb.getConnection().prepareStatement(query1);
            prep.setInt(1, idOI);
            prep.execute();

            String query2 = "DELETE FROM livraria.order "
                    + "WHERE fkusid=?";
            PreparedStatement prep2 = cdb.getConnection().prepareStatement(query2);
            prep2.setInt(1, id);
            prep2.execute();

            String query3 = "DELETE FROM livraria.user "
                    + "WHERE usid=?";
            PreparedStatement prep3 = cdb.getConnection().prepareStatement(query3);
            prep3.setInt(1, id);
            prep3.execute();

            cdb.getConnection().commit();
            cdb.close(); //fecha conexão para não ficar conectado o tempo todo
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //consultar
    public User consultarPorNome(String nome) {
        cdb.conectar();
        String nomeTmp = "";
        int idTmp = -1;
        User u = new User();
        String query = "SELECT * from livraria.user "
                + "WHERE name = ?";
        try {
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);
            prep.setString(1, nome);

            ResultSet list = prep.executeQuery();

            while (list.next()) {
                nomeTmp = list.getString("name");
                idTmp = list.getInt("usid");
                break;
            }
            cdb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        u.setName(nomeTmp);
        u.setUsid(idTmp);
        return u;
    }

    //consultar
    public User consultarPorUser(User u) {
        cdb.conectar();
        String nomeTmp = "";
        int idTmp = -1;
        String query = "SELECT * from livraria.user "
                + "WHERE name = ?";
        try {
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);
            prep.setString(1, u.getName());

            ResultSet list = prep.executeQuery();

            while (list.next()) {
                nomeTmp = list.getString("name");
                idTmp = list.getInt("usid");
                break;
            }
            cdb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        u.setName(nomeTmp);
        u.setUsid(idTmp);
        return u;
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
