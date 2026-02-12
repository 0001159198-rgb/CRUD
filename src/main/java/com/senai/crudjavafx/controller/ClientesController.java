package com.senai.crudjavafx.controller;

import com.senai.crudjavafx.dao.ClientesDAO;
import com.senai.crudjavafx.model.Cliente;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.SQLException;

public class ClientesController {
    @FXML private TextField pNome;
    @FXML private TextField pCPF;
    @FXML private TextField pTelefone;
    @FXML private TextField pEmail;
    @FXML private TextField pEndereco;

    @FXML private TableView<Cliente> tabelaClientes;
    @FXML private TableColumn<Cliente, Integer> colId;
    @FXML private TableColumn<Cliente, String> colNome;
    @FXML private TableColumn<Cliente, String> colCPF;
    @FXML private TableColumn<Cliente, String> colTelefone;
    @FXML private TableColumn<Cliente, String> colEmail;
    @FXML private TableColumn<Cliente, String> colEndereco;

    private ClientesDAO dao = new ClientesDAO();
    private Cliente clienteSelecionado;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        atualizarTabela();
    }

    private void atualizarTabela() {
        try {
            tabelaClientes.setItems(FXCollections.observableArrayList(dao.listarTodos()));
        } catch (SQLException e) {
            exibirAlerta("Erro", e.getMessage());
        }
    }

    @FXML
    public void salvarClientes() {
        try {
            if (clienteSelecionado == null) {
                dao.inserir(new Cliente(pNome.getText(), pCPF.getText(), pTelefone.getText(), pEmail.getText(), pEndereco.getText()));
            } else {
                clienteSelecionado.setNome(pNome.getText());
                clienteSelecionado.setCpf(pCPF.getText());
                clienteSelecionado.setTelefone(pTelefone.getText());
                clienteSelecionado.setEmail(pEmail.getText());
                clienteSelecionado.setEndereco(pEndereco.getText());
                dao.atualizar(clienteSelecionado);
            }
            atualizarTabela();
            limparCampos();
        } catch (SQLException e) {
            exibirAlerta("Erro", e.getMessage());
        }
    }

    @FXML
    public void excluirCliente() {
        if (clienteSelecionado != null) {
            try {
                dao.excluir(clienteSelecionado.getId());
                atualizarTabela();
                limparCampos();
            } catch (SQLException e) {
                exibirAlerta("Erro", e.getMessage());
            }
        } else {
            exibirAlerta("Aviso", "Selecione um cliente para excluir!");
        }
    }

    @FXML
    public void selecionarItem() {
        clienteSelecionado = tabelaClientes.getSelectionModel().getSelectedItem();
        if (clienteSelecionado != null) {
            pNome.setText(clienteSelecionado.getNome());
            pCPF.setText(clienteSelecionado.getCpf());
            pTelefone.setText(clienteSelecionado.getTelefone());
            pEmail.setText(clienteSelecionado.getEmail());
            pEndereco.setText(clienteSelecionado.getEndereco());
        }
    }

    @FXML
    public void limparCampos() {
        pNome.clear();
        pCPF.clear();
        pTelefone.clear();
        pEmail.clear();
        pEndereco.clear();
        clienteSelecionado = null;
        tabelaClientes.getSelectionModel().clearSelection();
    }

    private void exibirAlerta(String titulo, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setContentText(msg);
        alert.show();
    }
}