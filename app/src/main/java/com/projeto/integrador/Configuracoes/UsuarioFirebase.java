package com.projeto.integrador.Configuracoes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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
import com.projeto.integrador.Activity.InicialClienteActivity;
import com.projeto.integrador.Activity.MapsActivity;
import com.projeto.integrador.Configuracoes.ConfiguracaoFirebase;
import com.projeto.integrador.Model.Barbeiro;
import com.projeto.integrador.Model.Cliente;

//import barbearia.integradorvi.com.br.integradorbarber.Activity.CadastroBarbeariaActivity;
//import barbearia.integradorvi.com.br.integradorbarber.Activity.MapsActivity;
//import barbearia.integradorvi.com.br.integradorbarber.Model.Barbeiro;
//import barbearia.integradorvi.com.br.integradorbarber.Model.Cliente;
import com.projeto.integrador.Activity.CadastroBarbeariaActivity;
import com.projeto.integrador.Activity.MapsActivity;
import com.projeto.integrador.Model.Cliente;

public class UsuarioFirebase{

    static Boolean clienteLogado = false;



    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth usuario = ConfiguracaoFirebase.getAutenticacao();
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

        final FirebaseUser user = getUsuarioAtual(); //verefica se usuario já não esta logado
        if(user!=null){
            final DatabaseReference usuReference = ConfiguracaoFirebase.getDatabaseReference();
            //.child("clientes ou barbeiro") //.child(getIdentificadoUsuario()); //pega o id logado

            usuReference.child("clientes").orderByChild("email").equalTo(user.getEmail()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Cliente c = postSnapshot.getValue(Cliente.class);
                        //Log.e("Lista", c.getEmail()); //Log.e("User", user.getEmail());
                        clienteLogado = true;;
                        Log.e("xablau","é cliente");
                        testeLocaco(usuReference, activity);
                        break;
                    }
                    if(!clienteLogado){
                        clienteLogado = false;
                        testeLocaco(usuReference, activity);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public static void testeLocaco(DatabaseReference usuReference, final Activity activity){
        if(clienteLogado != null){
            if(clienteLogado == true){
                usuReference = ConfiguracaoFirebase.getDatabaseReference().child("clientes").child(getIdentificadoUsuario());//pega o id logado
                Log.e("xablau2","é cliente2");
            }
            else if(clienteLogado == false){
                usuReference = ConfiguracaoFirebase.getDatabaseReference().child("barbeiro").child(getIdentificadoUsuario());//pega o id logado
                Log.e("xablau2","é barbeiro2");
            }

            //clienteLogado = null;

            usuReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Cliente cliente =dataSnapshot.getValue(Cliente.class);
                    Barbeiro barbeiro = dataSnapshot.getValue(Barbeiro.class);

                    String tipoCliente = "";

                    if(cliente != null){// Teste
                        tipoCliente=cliente.getTipo();
                    }
                    else if(barbeiro != null){
                        tipoCliente=barbeiro.getTipo();
                    }

                    if(tipoCliente.equals("C")){//Verefica se é barbeiro ou cliente e redireciona a tela
                        activity.startActivity(new Intent(activity, InicialClienteActivity.class));
                    }
                    else if(tipoCliente.equals("B")){
                        activity.startActivity(new Intent(activity, CadastroBarbeariaActivity.class));

                        Intent intent = new Intent(activity, CadastroBarbeariaActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("barbeiro", barbeiro);
                        intent.putExtras(bundle);

                        activity.startActivity(intent);
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
