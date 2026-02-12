package com.senai.crudjavafx.model;

public class Venda {
    private int id;
    private Cliente cliente;
    private Produto produto;
    private int quantidade;
    private double valorUnitario;
    private double valorTotal;

    public Venda() {}

    public Venda(Cliente cliente, Produto produto, int quantidade, double valorUnitario, double valorTotal) {
        this.cliente = cliente;
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.valorTotal = valorTotal;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public double getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(double valorUnitario) { this.valorUnitario = valorUnitario; }
    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }

    public String getClienteNome() { return cliente != null ? cliente.getNome() : ""; }
    public String getProdutoNome() { return produto != null ? produto.getNome() : ""; }
}