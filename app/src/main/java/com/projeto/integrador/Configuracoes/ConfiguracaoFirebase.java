package com.projeto.integrador.Configuracoes;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {
    private static DatabaseReference databaseReference;
    private static FirebaseAuth autenticacao;

    //Retorna a instancia do Firebase
    public static DatabaseReference getDatabaseReference(){
        if(databaseReference==null){
            databaseReference= FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }
    public static FirebaseAuth getAutenticacao(){
        if(autenticacao==null){
            autenticacao=FirebaseAuth.getInstance();

        }
        return autenticacao;
    }
}
