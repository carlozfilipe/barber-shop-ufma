/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.barbershop;

import com.barbershop.model.dao.UsuarioDao;
import com.barbershop.model.domain.Perfil;
import com.barbershop.model.domain.Usuario;
import java.time.LocalDateTime;

/**
 *
 * @author carlos
 */

public class UsuarioTest {
    
    public static void main(String[] args) {
        Usuario usuario = new Usuario(0L, "Pedro Neto", "root", "padras", Perfil.PADRAO, null, null);
        
        UsuarioDao usuarioDao = new UsuarioDao();
        String mensagem = usuarioDao.salvar(usuario);
        System.out.println(mensagem);
    }
}
