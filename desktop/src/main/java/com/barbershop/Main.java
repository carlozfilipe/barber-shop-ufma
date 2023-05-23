/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.barbershop;

import com.barbershop.model.conexao.Conexao;
import com.barbershop.model.conexao.ConexaoMySQL;
import com.barbershop.model.domain.Categoria;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author carlos
 */
public class Main {

    public static void main(String[] args) throws SQLException {

        String sql = "SELECT * FROM categoria";
        
        Conexao conexao = new ConexaoMySQL();
        
        Categoria categoria = new Categoria(null, "Condicionador", "Condicionador para cabelo e barba.");
        
        String inserirSQL = "INSERT INTO categoria(nome, descricao) VALUES(?, ?)";
        
        PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(inserirSQL);
        
        preparedStatement.setString(1, categoria.getNome());
        preparedStatement.setString(2, categoria.getDescricao());
        
        int resultCRUD = preparedStatement.executeUpdate();
        System.out.println(resultCRUD);
        
        ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();

        while (result.next()) {
           System.out.println(result.getString("nome"));
        }
        
    }

}
