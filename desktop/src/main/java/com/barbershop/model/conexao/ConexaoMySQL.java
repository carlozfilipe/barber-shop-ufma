/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.barbershop.model.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author carlos
 */
public class ConexaoMySQL implements Conexao {
        
    private final String USUARIO = "root";
    private final String SENHA = "flamengo";
    private final String URL = "jdbc:mysql://172.17.0.2:3306/db_barbershop?user=root";
    private Connection conectar;
    
    @Override
    public Connection obterConexao() throws SQLException {
        
        if (conectar == null) {
            conectar = DriverManager.getConnection(URL, USUARIO, SENHA);
        }
        
        return conectar;
    }

}
