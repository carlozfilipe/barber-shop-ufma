/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import com.barbershop.model.connections.Conexao;
import com.barbershop.model.connections.ConexaoMysql;
import java.sql.SQLException;

/**
 * @author Alisson Santos
 * @author Andre Victor
 * @author Carlos Filipe
 * @author ItaloMatheus
 */

public class Main {
     public static void main (String[] args) throws SQLException{
        Conexao conexao = new ConexaoMysql(); 
        System.out.println(conexao.obterConexao());
}
}