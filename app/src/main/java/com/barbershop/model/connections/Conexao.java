/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barbershop.model.connections;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Alisson Santos
 * @author Andre Victor
 * @author Carlos Filipe
 * @author ItaloMatheus
 */

public interface Conexao {
    
    public Connection obterConexao() throws SQLException;
    public void fecharConexao() throws SQLException;
    
}
