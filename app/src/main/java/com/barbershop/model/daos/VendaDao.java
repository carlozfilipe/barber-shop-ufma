/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.barbershop.model.daos;

import com.barbershop.model.connections.Conexao;
import com.barbershop.model.connections.ConexaoMysql;
import com.barbershop.model.entities.Cliente;
import com.barbershop.model.entities.Produto;
import com.barbershop.model.entities.Usuario;
import com.barbershop.model.entities.Venda;
import com.barbershop.model.entities.VendaDetalhes;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alisson Santos
 * @author Andre Victor
 * @author Carlos Filipe
 * @author ItaloMatheus
 */

public class VendaDao {
    
    private final Conexao conexao;
    private final ProdutoDao produtoDao;

    public VendaDao() {
        this.conexao = new ConexaoMysql();
        this.produtoDao = new ProdutoDao();
    }
    
    public String salvar(Venda venda) {
        return venda.getId() == 0L ? adicionar(venda) : editar(venda);
    }

    private String adicionar(Venda venda) {
        String sql = "INSERT INTO venda(totalVenda, valorPago, troco, desconto, cliente_id, usuario_id) VALUES (?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            
            preparedStatementSet(preparedStatement, venda);
            
            int resultado = preparedStatement.executeUpdate();
            
            if(resultado == 1) {
                Long idDaVenda = buscarIdDaUltimaVenda();
                System.out.println("ID DA VENDA: " + idDaVenda);
                
                venda.setId(idDaVenda);
                
                venda.getVendasDetalhes()
                        .values()
                        .stream()
                        .forEach(vd -> {
                            vd.setVenda(venda);
                            
                            final int quantidade = vd.getProduto().getQuantidade() - vd.getQuantidade();
                            
                            String mensagem = produtoDao.atualizarQuantidade(vd.getProduto().getId(), 
                                    quantidade, vd.getVenda().getUsuario().getId());
                            
                            System.out.println(mensagem);
                            adicionarVendaItem(vd);
                        });
                
                return "Venda adicionado com sucesso.";
            } else {
                return "Nao foi possivel adiconar venda";
            }
            
        } catch (SQLException e) {
            return String.format("Error: %s", e.getMessage());
        }
    }

    private String editar(Venda venda) {
        String sql = "UPDATE venda SET totalVenda = ?, valorPago = ?, troco = ?, desconto = ?, cliente_id = ?, usuario_id = ?, ultimaActualizacao = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            
            preparedStatementSet(preparedStatement, venda);
            
            int resultado = preparedStatement.executeUpdate();
            
            if(resultado == 1) {
                venda.getVendasDetalhes()
                        .values()
                        .stream()
                        .forEach(vd -> editarVendaItem(vd));
                
                return "Venda editado com sucesso.";
            } else {
                return "Não foi possivel editar venda";
            }
            
        } catch (SQLException e) {
            return String.format("Erro: %s", e.getMessage());
        }
    }
    
    private void preparedStatementSet(PreparedStatement preparedStatement, Venda venda) throws SQLException {
       preparedStatement.setBigDecimal(1, venda.getTotalVenda());
       preparedStatement.setBigDecimal(2, venda.getValorPago());
       preparedStatement.setBigDecimal(3, venda.getTroco());
       preparedStatement.setBigDecimal(4, venda.getDesconto());
       preparedStatement.setLong(5, venda.getCliente().getId());
       preparedStatement.setLong(6, venda.getUsuario().getId());
       
       if(venda.getId() != 0L) {
           preparedStatement.setObject(7, LocalDateTime.now());
           preparedStatement.setLong(8, venda.getId());
       }
       
    }
    
    private void adicionarVendaItem(VendaDetalhes vendaDetalhes) {
        String sql = "INSERT INTO vendaDetalhes(quantidade, total, desconto, venda_id, produto_id) VALUES(?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            
            preparedStatementSetDetalhes(preparedStatement, vendaDetalhes);
            
            int resultado = preparedStatement.executeUpdate();
            
            String mensagem = resultado == 1 ? "Venda Detalhes adicionado com sucesso." : "Nao foi possivel adiconar Venda detalhes";
            System.out.println(mensagem);
        } catch (SQLException e) {
            System.out.println(String.format("Error: %s", e.getMessage()));
        }
    }
    
    private void editarVendaItem(VendaDetalhes vendaDetalhes) {
        String sql = "UPDATE vendaDetalhes SET quantidade  = ?, total  = ?, desconto  = ? WHERE venda_id = ? AND produto_id = ?";
        
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            
            preparedStatementSetDetalhes(preparedStatement, vendaDetalhes);
            
            int resultado = preparedStatement.executeUpdate();
            
            String mensagem = resultado == 1 ? "Venda Detalhes adicionado com sucesso." : "Nao foi possivel adiconar Venda detalhes";
            System.out.println(mensagem);
        } catch (SQLException e) {
            System.out.println(String.format("Error: %s", e.getMessage()));
        }
    }
    
    private void preparedStatementSetDetalhes(PreparedStatement preparedStatement, VendaDetalhes vendaDetalhes) throws SQLException {
       preparedStatement.setInt(1, vendaDetalhes.getQuantidade());
       preparedStatement.setBigDecimal(2, vendaDetalhes.getTotal());
       preparedStatement.setBigDecimal(3, vendaDetalhes.getDesconto());
       preparedStatement.setLong(4, vendaDetalhes.getVenda().getId());
       preparedStatement.setLong(5, vendaDetalhes.getProduto().getId());
    }
    
    
    public List<Venda> todosVendas() {
        String sql = "SELECT * FROM venda v, cliente c, usuario u WHERE v.cliente_id = c.id AND v.usuario_id = u.id ORDER BY v.id";
        List<Venda> vendas = new ArrayList<>();
        
        try {
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();
            
            while(result.next()) {
                vendas.add(getVenda(result));
            }
            
        } catch (SQLException e) {
            System.out.println(String.format("Error: %s", e.getMessage()));
        }
        
        return vendas;
    }
    
    public List<VendaDetalhes> buscaDetalhesDaVendaPeloId(Long id) {
        String sql = String.format("select * from venda v, vendaDetalhes vi, produto p, cliente c, usuario u where v.cliente_id = c.id and v.usuario_id = u.id and vi.venda_id = v.id \n" +
" and vi.produto_id = p.id and v.id = %d", id);
        List<VendaDetalhes> vendaDetalhes = new ArrayList<>();
        
        try {
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();
            
            while(result.next()) {
                vendaDetalhes.add(getVendaDetalhes(result));
            }
            
        } catch (SQLException e) {
            System.out.println(String.format("Error: %s", e.getMessage()));
        }
        
        return vendaDetalhes;
    }
    
    private VendaDetalhes getVendaDetalhes(ResultSet result) throws SQLException {
        VendaDetalhes vendaDetalhes = new VendaDetalhes();
        Venda venda = new Venda();
        Cliente cliente = new Cliente();
        Usuario usuario = new Usuario();
        Produto produto = new Produto();
        
        cliente.setId(result.getLong("id"));
        cliente.setNome(result.getString("nome"));
        
        usuario.setId(result.getLong("id"));
        usuario.setNome(result.getString("nome"));
        
        venda.setId(result.getLong("id"));
        venda.setTotalVenda(result.getBigDecimal("totalVenda"));
        venda.setValorPago(result.getBigDecimal("valorPago"));
        venda.setTroco(result.getBigDecimal("troco"));
        venda.setDesconto(result.getBigDecimal("desconto"));
        venda.setDataHoraCriacao(result.getObject("dataHoraCriacao", LocalDateTime.class));
        
        venda.setCliente(cliente);
        venda.setUsuario(usuario);
        
        produto.setId(result.getLong("id"));
        produto.setNome(result.getString("nome"));
        produto.setPreco(result.getBigDecimal("preco"));
        
        vendaDetalhes.setQuantidade(result.getInt("quantidade"));
        vendaDetalhes.setDesconto(result.getBigDecimal("desconto"));
        vendaDetalhes.setTotal(result.getBigDecimal("total"));
        vendaDetalhes.setVenda(venda);
        vendaDetalhes.setProduto(produto);

        return vendaDetalhes;
    }
    
    private Venda getVenda(ResultSet result) throws SQLException {
        Venda venda = new Venda();
        Cliente cliente = new Cliente();
        Usuario usuario = new Usuario();
        
        cliente.setId(result.getLong("id"));
        cliente.setNome(result.getString("nome"));
        
        usuario.setId(result.getLong("id"));
        usuario.setNome(result.getString("nome"));
        
        venda.setId(result.getLong("v.id"));
        venda.setTotalVenda(result.getBigDecimal("totalVenda"));
        venda.setValorPago(result.getBigDecimal("valorPago"));
        venda.setTroco(result.getBigDecimal("troco"));
        venda.setDesconto(result.getBigDecimal("desconto"));
        venda.setDataHoraCriacao(result.getObject("dataHoraCriacao", LocalDateTime.class));
        
        venda.setCliente(cliente);
        venda.setUsuario(usuario);

        return venda;
    }
    
    public Usuario buscarUsuarioPeloId(Long id) {
        String sql = String.format("SELECT * FROM usuario WHERE id = %d", id);
        
        try {
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();
            
            if(result.next()) {
//                return getUsuario(result);
            }
            
        } catch (SQLException e) {
            System.out.println(String.format("Error: %s", e.getMessage()));
        }
        
        return null;
    }
    
    public String deleteUsuarioPeloId(Long id) {
        String sql = String.format("DELETE FROM usuario WHERE id = %d", id);
        
        try {
            PreparedStatement preparedStatement = conexao.obterConexao().prepareStatement(sql);
            
            int resultado = preparedStatement.executeUpdate();
            
            return resultado == 1 ? "Usuario apagado com sucesso" : "Nao foi possivel apagar";
            
        } catch (SQLException e) {
            return String.format("Error: %s", e.getMessage());
        }
    }

    private Long buscarIdDaUltimaVenda() {
        String sql = "SELECT max(id) FROM venda";
        
        try {
            ResultSet result = conexao.obterConexao().prepareStatement(sql).executeQuery();
            
            if(result.next()) {
                return result.getLong(1);
            }
        } catch (SQLException e) {
            System.out.println(String.format("Error: %s", e.getMessage()));
        }
        
        return null;
    }
}
