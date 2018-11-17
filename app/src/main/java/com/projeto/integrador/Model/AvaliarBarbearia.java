package com.projeto.integrador.Model;

import com.google.firebase.database.DatabaseReference;
import com.projeto.integrador.Configuracoes.ConfiguracaoFirebase;

import java.io.Serializable;

public class AvaliarBarbearia implements Serializable {
    private String id;
    private String idCliente;
    private String nomeCliente;
    //private Cliente cliente;
    private String idBarbeiro;
    private Float avaliacao;
    private String comentario;
    private String dataAvaliacao;

    public AvaliarBarbearia(String id, String idCliente, String nomeCliente, String idBarbeiro, Float avaliacao, String comentario, String dataAvaliacao) {
        this.id = id;
        this.idCliente = idCliente;
        this.nomeCliente = nomeCliente;
        this.idBarbeiro = idBarbeiro;
        this.avaliacao = avaliacao;
        this.comentario = comentario;
        this.dataAvaliacao = dataAvaliacao;
    }

    public AvaliarBarbearia() {

    }

    public  void Salvar(){
        DatabaseReference fireReference = ConfiguracaoFirebase.getDatabaseReference();
        DatabaseReference usuario = fireReference.child("avaliacoes").child(getId());

        usuario.setValue(this);//Salva Dados banc
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    /*public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }*/

    public String getIdBarbeiro() {
        return idBarbeiro;
    }

    public void setIdBarbeiro(String idBarbeiro) {
        this.idBarbeiro = idBarbeiro;
    }

    public Float getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Float avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getDataAvaliacao() {
        return dataAvaliacao;
    }

    public void setDataAvaliacao(String dataAvaliacao) {
        this.dataAvaliacao = dataAvaliacao;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }
}
