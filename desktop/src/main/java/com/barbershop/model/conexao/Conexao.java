/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.barbershop.model.conexao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author carlos
 */
public interface Conexao {
    
    public Connection obterConexao() throws SQLException;
    
}
