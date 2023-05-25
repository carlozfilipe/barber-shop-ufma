/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import entities.model.daos.UsuarioDao;
import com.barbershop.model.entities.PERFIL;
import com.barbershop.model.entities.Usuario;

/**
 * @author Alisson Santos
 * @author Andre Victor
 * @author Carlos Filipe
 * @author ItaloMatheus
 */

public class UsuarioTeste {
    public static void main (String[] args){
        Usuario usuario =new Usuario(0L, "Pedro Silva", "pedro", "root", PERFIL.PADRAO, null, null);
        UsuarioDao usuario1= new UsuarioDao();
        String mensagem = usuario1.salvar(usuario);
        System.out.println(mensagem);
        
    }
    
}
