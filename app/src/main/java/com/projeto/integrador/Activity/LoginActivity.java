package com.projeto.integrador.Activity;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.projeto.integrador.Configuracoes.UsuarioFirebase;
import com.projeto.integrador.Model.Cliente;

import com.projeto.integrador.Configuracoes.ConfiguracaoFirebase;
import com.projeto.integrador.R;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText txtSenha, txtEmail;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Recuperar Dados
        txtEmail =findViewById(R.id.txtEmailLogin);
        txtSenha=findViewById(R.id.txtSenhaLogin);
    }

    public void validaLoginUsuario(View view){
        String email =txtEmail.getText().toString();
        String senha = txtSenha.getText().toString();

        if(!email.isEmpty()){
            if(!senha.isEmpty()){
                Cliente cliente = new Cliente();
                cliente.setEmail(email);
                cliente.setSenha(senha);

                logarCliente(cliente);
            }else{
                Toast.makeText(LoginActivity.this, "Digite Sua Senha", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(LoginActivity.this, "Digite seu E-mail", Toast.LENGTH_SHORT).show();

        }

    }
    public void logarCliente(Cliente cliente){
    autenticacao= ConfiguracaoFirebase.getAutenticacao();
    autenticacao.signInWithEmailAndPassword(
      cliente.getEmail(),cliente.getSenha()
    ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                //Vereficar tipo de usuario logado Barbeiro Ou Cliente
                UsuarioFirebase.redirecionaUsuarioLogado(LoginActivity.this);//passa a activy como parametro
            }else{
                String exececao = "";
                try {
                    throw task.getException();
                } catch (FirebaseAuthInvalidUserException e) {
                    Toast.makeText(LoginActivity.this, "Não Cadastrado", Toast.LENGTH_SHORT).show();

                } catch (FirebaseAuthInvalidCredentialsException e) {
                    Toast.makeText(LoginActivity.this, "E-mail e senha não Correspondem", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Erro ao Efetuar Login", Toast.LENGTH_SHORT).show();
                }

            }
        }
    });

    }
}
