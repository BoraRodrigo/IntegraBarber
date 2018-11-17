package com.projeto.integrador.Model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import com.projeto.integrador.Configuracoes.ConfiguracaoFirebase;

public class Cliente {

    private String id;
    private String nome;
    private String email;
    private String senha;
    private String tipo;

    public Cliente(String id, String nome, String email, String senha, String tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipo = tipo;
    }

    public Cliente() {

    }
    public  void Salvar(){
        DatabaseReference fireReference= ConfiguracaoFirebase.getDatabaseReference();
        DatabaseReference usuario =fireReference.child("clientes").child(getId());

        usuario.setValue(this);//Salva Dados banco
        //.push();Adiciona um Id De incrremente
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
    }///define para não salvar a senha no cadastro

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
