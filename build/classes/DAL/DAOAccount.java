package DAL;

import Models.Account;
import Models.Author;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DAOAccount {

    ConectionDB cdb = new ConectionDB();

    //incluir
    public void incluir(Account ac) throws SQLException {
        cdb.conectar();

        String query = "INSERT INTO livraria.account (email,password) "
                + "VALUES (?,?)";
        PreparedStatement prep = cdb.getConnection().prepareStatement(
                query,
                Statement.RETURN_GENERATED_KEYS); //retorna as chaves primárias que foram geradas
        prep.setString(1, ac.getEmailAddress());
        prep.setString(2, ac.getPassword());
        prep.execute();

        ResultSet ultimoID = prep.getGeneratedKeys();
        ultimoID.next();
        ac.setID(ultimoID.getInt(1));

        cdb.getConnection().commit();
        cdb.close(); //fecha conexão para não ficar conectado o tempo todo
    }

    //alterar - a model deve estar com o id preenchido
    public void alterar(Account ac) throws SQLException {
        cdb.conectar();
        try {
            String query = "UPDATE livraria.account "
                    + "SET email=?, password=? WHERE ID=?";
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);

            prep.setLong(3, ac.getID()); //alterando pelo ID
            prep.setString(1, ac.getEmailAddress());
            prep.setString(2, ac.getPassword());
            prep.execute();

            cdb.getConnection().commit();
            cdb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //excluir
    public void excluir(Account ac) {
        cdb.conectar();
        try {
            String query = "DELETE FROM livraria.account "
                    + "WHERE ID=?";
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);

            prep.setLong(1, ac.getID());
            prep.execute();

            cdb.getConnection().commit();
            cdb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //consultar 
    public String consultarPorEmail(String email) {
        cdb.conectar();

        Account ac = new Account();
        String emailTmp = "";
        String query = "SELECT * from livraria.account "
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
        ac.setEmailAddress(emailTmp);
        return emailTmp;
    }

    //consultar password
    public String consultarPorPSW(String psw) {
        cdb.conectar();

        Account ac = new Account();
        String pswTmp = "";
        String query = "SELECT * from livraria.account "
                + "WHERE password = ?";
        try {
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);
            prep.setString(1, psw);

            ResultSet list = prep.executeQuery();

            while (list.next()) {
                pswTmp = list.getString("password");
                break;
            }
            cdb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ac.setPassword(pswTmp);
        return pswTmp;
    }

    public List listar(String params) {
        cdb.conectar();
        List<Account> listaContas = new ArrayList<>();
        String query = "SELECT * from livraria.account " + params;

        try {
            PreparedStatement prep = cdb.getConnection().prepareStatement(query);
            ResultSet lista = prep.executeQuery();

            while (lista.next()) {
                Account ac = new Account();
                ac.setID(lista.getLong("ID"));
                ac.setEmailAddress(lista.getString("email"));
                ac.setPassword(lista.getString("password"));
                listaContas.add(ac);
            }
            cdb.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaContas;
    }

}
