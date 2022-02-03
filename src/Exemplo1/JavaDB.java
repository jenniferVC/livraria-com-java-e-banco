/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exemplo1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;


public class JavaDB {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] argv) {
        Properties connConfig = new Properties();
        // setProperty define proprieades de Chave e Valor para connConfig
        connConfig.setProperty("user", "root"); //para a chave User, tem o valor Root
        connConfig.setProperty("password", "123"); //para a chave password, tem  o valor 123
        //Conexão                            getConnection tenta fazer a conexão SE conseguir armazena no objeto conn SENÃO cai no Catch do Try-Catch
        try (Connection conn = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3308", connConfig)) {
            try (Statement stmt = conn.createStatement()) {
                //ResultSet é parcido co ArrayList, porém se acessa com chave, valor
                try (ResultSet _list = stmt.executeQuery("SELECT usid, name FROM library.user")) { //fazendo uma pesquisa e retornando o resultado dela para a lista de ResultSet
                    while (_list.next()) { //ENQUANTO existir próximo elemento na lista
                        //%d imprime a ID que é do tipo inteiro e o %s imprime a String que é o name
                        System.out.println(String.format("%d %s", 
                            _list.getInt("usid"), 
                            _list.getString("name")));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
