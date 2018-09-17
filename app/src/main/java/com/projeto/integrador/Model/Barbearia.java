package com.projeto.integrador.Model;

import com.google.firebase.database.DatabaseReference;
import com.projeto.integrador.Configuracoes.ConfiguracaoFirebase;

import java.io.Serializable;

//import barbearia.integradorvi.com.br.integradorbarber.Configuracoes.ConfiguracaoFirebase;

public class Barbearia implements Serializable{

    private String id;
    private String idBarbeiro;
    private String nomebarbearia;
    private String descricao;
    private String telefone;
    private String pagina;

    private String rua;
    private String cidade;
    private String cep;
    private int numero ;

    public void Salvar(){
        DatabaseReference fireReference= ConfiguracaoFirebase.getDatabaseReference();
        DatabaseReference usuario =fireReference.child("barbearia").child(getId());

        usuario.setValue(this);//Salva Dados banc
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNomebarbearia() {
        return nomebarbearia;
    }

    public void setNomebarbearia(String nomebarbearia) {
        this.nomebarbearia = nomebarbearia;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getPagina() {
        return pagina;
    }

    public void setPagina(String pagina) {
        this.pagina = pagina;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdBarbeiro() {
        return idBarbeiro;
    }

    public void setIdBarbeiro(String idBarbeiro) {
        this.idBarbeiro = idBarbeiro;
    }
}
