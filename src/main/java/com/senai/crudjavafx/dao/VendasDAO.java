package com.senai.crudjavafx.dao;

import com.senai.crudjavafx.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendasDAO {
    private Connection connection;

    public VendasDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    public void salvar(Venda venda) throws SQLException {
        // Usando cliente_id e produto_id como chaves estrangeiras
        String sql = "INSERT INTO vendas (cliente_id, produto_id, quantidade, vunitario, total) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, venda.getCliente().getId());
            stmt.setInt(2, venda.getProduto().getId());
            stmt.setInt(3, venda.getQuantidade());
            stmt.setDouble(4, venda.getValorUnitario());
            stmt.setDouble(5, venda.getValorTotal());
            stmt.execute();
        }
    }

    public List<Venda> listarTodas() throws SQLException {
        List<Venda> lista = new ArrayList<>();
        String sql = "SELECT v.*, c.nome AS nome_c, p.nome AS nome_p FROM vendas v " +
                "JOIN clientes c ON v.cliente_id = c.id " +
                "JOIN produtos p ON v.produto_id = p.id";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("cliente_id"));
                c.setNome(rs.getString("nome_c"));

                Produto p = new Produto();
                p.setId(rs.getInt("produto_id"));
                p.setNome(rs.getString("nome_p"));

                Venda v = new Venda(c, p, rs.getInt("quantidade"), rs.getDouble("vunitario"), rs.getDouble("total"));
                v.setId(rs.getInt("id"));
                lista.add(v);
            }
        }
        return lista;
    }
}