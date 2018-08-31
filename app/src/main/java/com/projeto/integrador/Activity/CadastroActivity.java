package com.projeto.integrador.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.projeto.integrador.Configuracoes.UsuarioFirebase;
import com.projeto.integrador.Model.Cliente;

import com.projeto.integrador.Configuracoes.ConfiguracaoFirebase;

import com.projeto.integrador.R;

public class CadastroActivity extends AppCompatActivity{

    private TextInputEditText txtEmail,txtNome, txtSenha;
    private Switch switchTipousuario;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        txtEmail=findViewById(R.id.txtEmailCadastro);
        txtNome=findViewById(R.id.txtNomeCadastro);
        txtSenha=findViewById(R.id.txtSenhacadastro);

        switchTipousuario=findViewById(R.id.switchTipoUsuario);
    }
    public void validaDados(View view) {
        String nome = txtNome.getText().toString();
        String email = txtEmail.getText().toString();
        String senha = txtSenha.getText().toString();

        if (!nome.isEmpty()) {
            if (!email.isEmpty()) {
                if (!senha.isEmpty()) {

                    Cliente cliente = new Cliente();
                    cliente.setNome(nome);
                    cliente.setEmail(email);
                    cliente.setSenha(senha);
                    cliente.setTipo(tipo_Cadastro());

                    cadastrarUsuario(cliente);

                } else {
                    Toast.makeText(CadastroActivity.this, "Preencha A Senha", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CadastroActivity.this, "Preencha o Email", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CadastroActivity.this, "Preencha o Nome", Toast.LENGTH_SHORT).show();
        }
    }

    public String tipo_Cadastro(){//Varefica qse é cliente ou barbeiro
        return switchTipousuario.isChecked()?"C":"B";//if ternario

    }

    public void cadastrarUsuario(final Cliente cliente){
        autenticacao= ConfiguracaoFirebase.getAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                cliente.getEmail(),
                cliente.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                   String idCliente= task.getResult().getUser().getUid();
                   cliente.setId(idCliente);

                   cliente.Salvar();

                   ////Atualizar Nome no UserProfile no perfil
                    UsuarioFirebase.atualizarNomeUsuario(cliente.getNome());

                   if(cliente.getTipo().equals("C")){//Se o usuario Cadastrado for Cliente Redicrecions spos o Cadastro
                        startActivities(new Intent[]{new Intent(CadastroActivity.this, LoginActivity.class)});
                        finish();
                   }
                    if(cliente.getTipo().equals("B")){//Se o usuario Cadastrado for Cliente Redicrecions spos o Cadastro
                        startActivities(new Intent[]{new Intent(CadastroActivity.this, CadastroBarbeariaActivity.class)});
                        finish();
                    }
                }else {//Trata execesões do firebase se caso não realizar cadastro
                    String exececao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        Toast.makeText(CadastroActivity.this, "Digete Uma senha mais Forte", Toast.LENGTH_SHORT).show();

                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        Toast.makeText(CadastroActivity.this, "E-mail Invalido", Toast.LENGTH_SHORT).show();

                    } catch (FirebaseAuthUserCollisionException e) {
                        Toast.makeText(CadastroActivity.this, "Conta já Cadastrada", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(CadastroActivity.this, "Erro ao Cadastrar", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}

