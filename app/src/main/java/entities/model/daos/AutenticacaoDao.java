/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities.model.daos;

import com.barbershop.model.entities.PERFIL;
import com.barbershop.model.entities.Usuario;
import com.barbershop.model.exception.NegocioException;
import com.barbershop.view.models.LoginDto;
import javax.swing.JOptionPane;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Alisson Santos
 * @author Andre Victor
 * @author Carlos Filipe
 * @author ItaloMatheus
 */

public class AutenticacaoDao {
    
    private final UsuarioDao usuarioDao;

    public AutenticacaoDao() {
        this.usuarioDao = new UsuarioDao();
    }
    
    public boolean temPermissao(Usuario usuario) {
        try {
            permissao(usuario);
            return true;
        } catch (NegocioException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Usuario sem permissao", 0);
            System.out.println("ERROR: " + e.getMessage());
            return false;
        }
    }
    
    private void permissao(Usuario usuario) {
        if(!PERFIL.ADMIN.equals(usuario.getPerfil())) {
            throw new NegocioException("Sem permissao para realizar essa ação.");
        }
    }

    public Usuario login(LoginDto login) {
        Usuario usuario = usuarioDao.buscarUsuarioPeloUsername(login.getUsername());
        
        if(usuario == null || !usuario.isEstado()) 
            return null;
       
        if(usuario.isEstado() && validaSenha(usuario.getSenha(), login.getSenha())) {
            usuarioDao.atualizarUltimoLogin(usuario);
            return usuario;
        }
        return null;
    }
    
//    private boolean validaSenha(String usuarioSenha, String loginSenha) {
//        return usuarioSenha.equals(loginSenha);
//    }
    
    private boolean validaSenha(String usuarioSenha, String loginSenha) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(loginSenha, usuarioSenha);
    }
    
}
