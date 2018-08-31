package com.projeto.integrador.Configuracoes;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.projeto.integrador.Activity.CadastroBarbeariaActivity;
import com.projeto.integrador.Activity.MapsActivity;
import com.projeto.integrador.Model.Cliente;

public class UsuarioFirebase{

    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth usuario =ConfiguracaoFirebase.getAutenticacao();
        return usuario.getCurrentUser();
    }

    public static boolean atualizarNomeUsuario(String nome){
        try {
            FirebaseUser user =getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
            return true;
        }catch (Exception e ){
            e.printStackTrace();
            return false;
        }

    }
    public  static  void redirecionaUsuarioLogado(final Activity activity){//passa a activy como parametro

        FirebaseUser user = getUsuarioAtual();//verefica se usuario já não esta logado
        if(user!=null){
            DatabaseReference usuReference= ConfiguracaoFirebase.getDatabaseReference()
                    .child("clientes")
                    .child(getIdentificadoUsuario());//pega o id logado

            usuReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) { Cliente cliente =dataSnapshot.getValue(Cliente.class);

                   String tipoCliente=cliente.getTipo();

                    if(tipoCliente.equals("C")){//Verefica se é barbeiro ou cliente e redireciona a tela
                        activity.startActivity(new Intent(activity, MapsActivity.class));
                 }else{
                        activity.startActivity(new Intent(activity, CadastroBarbeariaActivity.class));
                   }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }
    public static String getIdentificadoUsuario(){
        return  getUsuarioAtual().getUid();//retorna o Id logado
    }
}
