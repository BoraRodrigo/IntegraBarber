package com.projeto.integrador.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.projeto.integrador.Configuracoes.ConfiguracaoFirebase;
import com.projeto.integrador.Configuracoes.UsuarioFirebase;
import com.projeto.integrador.Model.Cliente;
import com.projeto.integrador.R;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText txtSenha, txtEmail;
    private FirebaseAuth autenticacao;
    private ProgressBar progressBarLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Recuperar Dados
        txtEmail =findViewById(R.id.txtEmailLogin);
        txtSenha=findViewById(R.id.txtSenhaLogin);
        progressBarLogin=findViewById(R.id.progressBarLogin);
    }

    public void validaLoginUsuario(View view){
        String email =txtEmail.getText().toString();
        String senha = txtSenha.getText().toString();
        progressBarLogin.setVisibility(View.VISIBLE);

       ///Muda a Cor da Progres Bar que de padrão vem vermelha
        progressBarLogin.getIndeterminateDrawable()
                .setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);

        if(!email.isEmpty()){
            if(!senha.isEmpty()){
                logarCliente(email, senha);
            }else{
                Toast.makeText(LoginActivity.this, "Digite Sua Senha", Toast.LENGTH_SHORT).show();
                progressBarLogin.setVisibility(View.GONE);
            }
        }else{
            Toast.makeText(LoginActivity.this, "Digite seu E-mail", Toast.LENGTH_SHORT).show();
            progressBarLogin.setVisibility(View.GONE);

        }

    }
    public void logarCliente(String email, String senha){//Cliente cliente
        autenticacao= ConfiguracaoFirebase.getAutenticacao();

        //Hoje 17/09/2018
        AuthCredential credencial = EmailAuthProvider.getCredential(email, senha);
        autenticacao.signInWithCredential(credencial).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                // Sign in success, update UI with the signed-in user's information
                Log.e("Login com sucesso", "signInWithCredential:success");
                final FirebaseUser user = autenticacao.getCurrentUser();
                final DatabaseReference usuReference = ConfiguracaoFirebase.getDatabaseReference();
                usuReference.child("clientes").orderByChild("email").equalTo(user.getEmail()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int barbeiroEntra = 0;
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            Cliente c = postSnapshot.getValue(Cliente.class);
                            //if(user.getEmail().equals(c.getEmail())){
                                startActivities(new Intent[]{new Intent(LoginActivity.this, InicialClienteActivity.class)});
                                barbeiroEntra = 1;
                                finish();
                                //break;
                            //}
                        }
                        if(barbeiroEntra==0){
                            startActivities(new Intent[]{new Intent(LoginActivity.this, InicialBarbeiroActivity.class)});
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //updateUI(user);
            } else{
                String exececao = "";
                try {
                    throw task.getException();
                } catch (FirebaseAuthInvalidUserException e) {
                    Toast.makeText(LoginActivity.this, "Não Cadastrado", Toast.LENGTH_SHORT).show();
                    progressBarLogin.setVisibility(View.GONE);


                } catch (FirebaseAuthInvalidCredentialsException e) {
                    Toast.makeText(LoginActivity.this, "E-mail e senha não Correspondem", Toast.LENGTH_SHORT).show();
                    progressBarLogin.setVisibility(View.GONE);


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Erro ao Efetuar Login", Toast.LENGTH_SHORT).show();
                    progressBarLogin.setVisibility(View.GONE);

                }

            }

            // ...
            }
        });

        /*autenticacao.signInWithEmailAndPassword(
                email, senha
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //Vereficar tipo de usuario logado Barbeiro Ou Cliente
                    txtEmail.setEnabled(false);
                    txtSenha.setEnabled(false);
                    UsuarioFirebase.redirecionaUsuarioLogado(LoginActivity.this);//passa a activy como parametro

                }else{
                    String exececao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        Toast.makeText(LoginActivity.this, "Não Cadastrado", Toast.LENGTH_SHORT).show();
                        progressBarLogin.setVisibility(View.GONE);


                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        Toast.makeText(LoginActivity.this, "E-mail e senha não Correspondem", Toast.LENGTH_SHORT).show();
                        progressBarLogin.setVisibility(View.GONE);


                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Erro ao Efetuar Login", Toast.LENGTH_SHORT).show();
                        progressBarLogin.setVisibility(View.GONE);

                    }

                }
            }
        });*/

    }

    public void esqueceuSenha(View view){
        autenticacao= ConfiguracaoFirebase.getAutenticacao();

        if(!txtEmail.getText().toString().isEmpty()){
            autenticacao.sendPasswordResetEmail(txtEmail.getText().toString());
        }
        else{
            Toast.makeText(getApplicationContext(),"Por favor, preencha o campo de email",Toast.LENGTH_SHORT).show();
        }
    }

    public void chama(View view){
        startActivities(new Intent[]{new Intent(this, InicialClienteActivity.class)});
    }

}
