package com.senai.crudjavafx.controller;

import com.senai.crudjavafx.dao.ClientesDAO;
import com.senai.crudjavafx.dao.ProdutoDAO;
import com.senai.crudjavafx.dao.VendasDAO;
import com.senai.crudjavafx.model.Cliente;
import com.senai.crudjavafx.model.Produto;
import com.senai.crudjavafx.model.Venda;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.sql.SQLException;

public class VendasController {

    @FXML private ComboBox<Cliente> cbCliente;
    @FXML private ComboBox<Produto> cbProduto;
    @FXML private Spinner<Integer> spQuantidade;
    @FXML private TextField txtValorUnitario;
    @FXML private TextField txtValorTotal;
    @FXML private TableView<Venda> tabelaVendas;
    @FXML private TableColumn<Venda, Integer> colId;
    @FXML private TableColumn<Venda, String> colCliente1;
    @FXML private TableColumn<Venda, String> colProduto;
    @FXML private TableColumn<Venda, Integer> colQtd;
    @FXML private TableColumn<Venda, Double> colValorU;
    @FXML private TableColumn<Venda, Double> colValorT;

    private VendasDAO vendaDao = new VendasDAO();
    private ClientesDAO clienteDao = new ClientesDAO();
    private ProdutoDAO produtoDao = new ProdutoDAO();

    @FXML
    public void initialize() {
        // Configuração das colunas da TableView
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCliente1.setCellValueFactory(new PropertyValueFactory<>("clienteNome"));
        colProduto.setCellValueFactory(new PropertyValueFactory<>("produtoNome"));
        colQtd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colValorU.setCellValueFactory(new PropertyValueFactory<>("valorUnitario"));
        colValorT.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));

        // Configuração do Spinner (1 a 100)
        spQuantidade.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));

        // Listener: Quando selecionar um produto, busca o preço e calcula o total
        cbProduto.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                txtValorUnitario.setText(String.valueOf(novo.getPreco()));
                calcularTotal();
            }
        });

        // Listener: Quando mudar a quantidade no Spinner, recalcula o total
        spQuantidade.valueProperty().addListener((obs, antigo, novo) -> calcularTotal());

        carregarComboBoxes();
        atualizarTabela();
    }

    private void calcularTotal() {
        try {
            double preco = Double.parseDouble(txtValorUnitario.getText());
            int qtd = spQuantidade.getValue();
            txtValorTotal.setText(String.valueOf(preco * qtd));
        } catch (Exception e) {
            txtValorTotal.setText("0.00");
        }
    }

    private void carregarComboBoxes() {
        try {
            cbCliente.setItems(FXCollections.observableArrayList(clienteDao.listarTodos()));
            cbProduto.setItems(FXCollections.observableArrayList(produtoDao.listarTodos()));
        } catch (SQLException e) {
            exibirAlerta("Erro", "Erro ao carregar dados: " + e.getMessage());
        }
    }

    @FXML
    void adicionarItem(ActionEvent event) {
        Cliente c = cbCliente.getSelectionModel().getSelectedItem();
        Produto p = cbProduto.getSelectionModel().getSelectedItem();

        if (c != null && p != null) {
            try {
                Venda v = new Venda(c, p, spQuantidade.getValue(),
                        p.getPreco(), Double.parseDouble(txtValorTotal.getText()));
                vendaDao.salvar(v);
                atualizarTabela();
                limparCampos(null);
            } catch (SQLException e) {
                exibirAlerta("Erro ao salvar", e.getMessage());
            }
        } else {
            exibirAlerta("Aviso", "Selecione um cliente e um produto!");
        }
    }

    @FXML
    void finalizarVenda(ActionEvent event) {
        exibirAlertaInfo("Sucesso", "Venda realizada e finalizada!");
        atualizarTabela();
    }

    @FXML
    void relatorioVendas(ActionEvent event) {
        Venda selecionada = tabelaVendas.getSelectionModel().getSelectedItem();

        if (selecionada != null) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Relatório de Venda");
            alert.setHeaderText("Comprovante de Venda #" + selecionada.getId());

            String texto = String.format(
                    "Cliente: %s\n" +
                            "Produto: %s\n" +
                            "----------------------------\n" +
                            "Quantidade: %d\n" +
                            "Valor Unitário: R$ %.2f\n" +
                            "----------------------------\n" +
                            "VALOR TOTAL: R$ %.2f\n" +
                            "Data: 2026",
                    selecionada.getClienteNome(),
                    selecionada.getProdutoNome(),
                    selecionada.getQuantidade(),
                    selecionada.getValorUnitario(),
                    selecionada.getValorTotal()
            );

            alert.setContentText(texto);
            alert.showAndWait();
        } else {
            exibirAlerta("Aviso", "Selecione uma venda na tabela para gerar o relatório.");
        }
    }

    @FXML
    void limparCampos(ActionEvent event) {
        cbCliente.getSelectionModel().clearSelection();
        cbProduto.getSelectionModel().clearSelection();
        txtValorUnitario.clear();
        txtValorTotal.clear();
        spQuantidade.getValueFactory().setValue(1);
    }

    @FXML
    void selecionarItem(MouseEvent event) {
        // Opcional: preencher campos ao clicar na tabela para consulta
    }

    private void atualizarTabela() {
        try {
            tabelaVendas.setItems(FXCollections.observableArrayList(vendaDao.listarTodas()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void exibirAlerta(String titulo, String msg) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setContentText(msg);
        alert.show();
    }

    private void exibirAlertaInfo(String titulo, String msg) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(msg);
        alert.show();
    }
}