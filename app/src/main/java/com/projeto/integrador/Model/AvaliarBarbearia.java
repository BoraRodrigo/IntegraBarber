package com.projeto.integrador.Model;

import com.google.firebase.database.DatabaseReference;
import com.projeto.integrador.Configuracoes.ConfiguracaoFirebase;

public class AvaliarBarbearia {
    private String id;
    private String idCliente;
    private String idBarbeiro;
    private Float avaliacao;
    private String comentario;

    public AvaliarBarbearia(String id, String idCliente, String idBarbeiro, Float avaliacao, String comentario) {
        this.id = id;
        this.idCliente = idCliente;
        this.idBarbeiro = idBarbeiro;
        this.avaliacao = avaliacao;
        this.comentario = comentario;
    }

    public AvaliarBarbearia() {

    }

    public  void Salvar(){
        DatabaseReference fireReference= ConfiguracaoFirebase.getDatabaseReference();
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
    ;
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
}
