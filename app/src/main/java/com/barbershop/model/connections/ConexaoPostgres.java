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

public class ConexaoPostgres implements Conexao{

    @Override
    public Connection obterConexao() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void fecharConexao() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
