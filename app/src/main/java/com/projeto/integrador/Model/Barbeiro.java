package com.projeto.integrador.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.projeto.integrador.Configuracoes.ConfiguracaoFirebase;

public class Barbeiro {
    private String id;
    private String nome;
    private String email;
    private String senha;
    private String tipo;




    public void Salvar() {
        DatabaseReference fireReference = ConfiguracaoFirebase.getDatabaseReference();
        DatabaseReference usuario = fireReference.child("barbeiro").child(getId());

        usuario.setValue(this);//Salva Dados banc
    }


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    @Exclude
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}