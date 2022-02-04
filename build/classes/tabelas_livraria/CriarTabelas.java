package tabelas_livraria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class CriarTabelas {

    public static void main(String[] argv) throws SQLException, ClassNotFoundException {
        // Connection Configuration
        Properties connConfig = new Properties();
        connConfig.setProperty("user", "root");
        connConfig.setProperty("password", "123");
        //connConfig.setProperty("useSsl", "true");
        //connConfig.setProperty("serverSslCert", "/path/to/ca-bundle.pem");
// Create Connection to MariaDB Enterprise
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3308/library", connConfig)) {

            // Disable Auto-Commit
            conn.setAutoCommit(false);

            // user
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(
                        "CREATE TABLE IF NOT EXISTS library.user ("
                        + "usid INT PRIMARY KEY AUTO_INCREMENT,"
                        + "name VARCHAR(50))"
                        + "ENGINE=InnoDB;");
            } catch (Exception e) {
                e.printStackTrace();
            }

            // publisher
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(
                        "CREATE TABLE IF NOT EXISTS library.publisher ("
                        + "puid INT PRIMARY KEY,"
                        + "name VARCHAR(25),"
                        + "endereco VARCHAR(25))"
                        + "ENGINE=InnoDB;");
            } catch (Exception e) {
                e.printStackTrace();
            }

            // author
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(
                        "CREATE TABLE IF NOT EXISTS library.author ("
                        + "auid INT PRIMARY KEY,"
                        + "name VARCHAR(50))"
                        + "ENGINE=InnoDB;");
            } catch (Exception e) {
                e.printStackTrace();
            }

            // book
            /*try (Statement stmt = conn.createStatement()) {
                stmt.execute(
                        "CREATE TABLE IF NOT EXISTS library.books ("
                        + "boID INT PRIMARY KEY AUTO_INCREMENT,"
                        + "titulo VARCHAR(50),"
                        + "pags INT NULL,"
                        + "fkpuID INT NOT NULL,"
                        + "fkauID INT NOT NULL,"
                        + "CONSTRAINT FK_puID FOREIGN KEY (fkpuID)"
                        + " REFERENCES LIBRARY.publisher(puID),"
                        + "CONSTRAINT FK_auID FOREIGN KEY (fkauID)"
                        + " REFERENCES LIBRARY.author(auID))"
                        + "ENGINE=InnoDB;");
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(
                        "CREATE TABLE IF NOT EXISTS library.books ("
                        + "boid INT PRIMARY KEY AUTO_INCREMENT,"
                        + "titulo VARCHAR(25),"
                        + "author VARCHAR(25),"
                        + "email VARCHAR(25),"        
                        + "fkpuid INT NOT NULL,"
                        + "fkauid INT NOT NULL,"
                        + "CONSTRAINT FK_puid FOREIGN KEY (fkpuid)"
                        + " REFERENCES LIBRARY.publisher(puid),"
                        + "CONSTRAINT FK_auid FOREIGN KEY (fkauid)"
                        + " REFERENCES LIBRARY.author(auid))"
                        + "ENGINE=InnoDB;");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
