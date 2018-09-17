package com.projeto.integrador.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

//import barbearia.integradorvi.com.br.integradorbarber.Configuracoes.ConfiguracaoFirebase;
//import barbearia.integradorvi.com.br.integradorbarber.Configuracoes.UsuarioFirebase;
//import barbearia.integradorvi.com.br.integradorbarber.Model.Barbeiro;
//import barbearia.integradorvi.com.br.integradorbarber.Model.Cliente;
//import barbearia.integradorvi.com.br.integradorbarber.R;

import com.google.firebase.auth.FirebaseUser;
import com.projeto.integrador.Configuracoes.UsuarioFirebase;
import com.projeto.integrador.Model.Barbeiro;
import com.projeto.integrador.Model.Cliente;
import com.projeto.integrador.Configuracoes.ConfiguracaoFirebase;
import com.projeto.integrador.R;

public class CadastroActivity extends AppCompatActivity{

    private TextInputEditText txtEmail,txtNome, txtSenha;
    private LoginButton loginButton;
    private Switch switchTipousuario;

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        txtEmail=findViewById(R.id.txtEmailCadastro);
        txtNome=findViewById(R.id.txtNomeCadastro);
        txtSenha=findViewById(R.id.txtSenhacadastro);

        loginButton = findViewById(R.id.login_button);

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

                    //Teste - 02/09/2018
                    if(cliente.getTipo().equals("C")){
                        cadastrarUsuario(cliente, null);
                    }
                    else if(cliente.getTipo().equals("B")){
                        Barbeiro barbeiro = new Barbeiro();
                        barbeiro.setNome(nome);
                        barbeiro.setEmail(email);
                        barbeiro.setSenha(senha);
                        barbeiro.setTipo(tipo_Cadastro());

                        cadastrarUsuario(null, barbeiro);
                    }
                    // acabou teste - 02/09/2018

                    //cadastrarUsuario(cliente); Esse tava antes

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

    public void cadastrarUsuario(final Cliente cliente, final Barbeiro barbeiro){// Colocado barbeiro
        autenticacao= ConfiguracaoFirebase.getAutenticacao();

        //Teste
        String email = "";
        String senha = "";

        if(barbeiro == null){
            email = cliente.getEmail();
            senha = cliente.getSenha();
        }
        else{
            email = barbeiro.getEmail();
            senha = barbeiro.getSenha();
        }
        //Fim Teste
        autenticacao.getUid();

        autenticacao.createUserWithEmailAndPassword( //Teste aqui
                email,
                senha
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // Teste
                    if(cliente != null){
                        String idCliente= task.getResult().getUser().getUid();
                        cliente.setId(idCliente);

                        cliente.Salvar();

                        UsuarioFirebase.atualizarNomeUsuario(cliente.getNome());

                        startActivities(new Intent[]{new Intent(CadastroActivity.this, LoginActivity.class)});
                        finish();
                    }
                    else{
                        String idBarbeiro= task.getResult().getUser().getUid();
                        barbeiro.setId(idBarbeiro);

                        Log.e("Teste do id - getUid", autenticacao.getUid());

                        //barbeiro.Salvar();

                        UsuarioFirebase.atualizarNomeUsuario(barbeiro.getNome());

                        startActivities(new Intent[]{new Intent(CadastroActivity.this, CadastroBarbeariaActivity.class)});

                        Intent intent = new Intent(CadastroActivity.this, CadastroBarbeariaActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("barbeiro", barbeiro);
                        intent.putExtras(bundle);
                        startActivity(intent);

                        finish();
                    }
                    //Fim Teste

                    ////Atualizar Nome no UserProfile no perfil
                    //UsuarioFirebase.atualizarNomeUsuario(cliente.getNome()); POR CAUSA DO TESTE

                   /*if(cliente != null){//TESTE //cliente.getTipo().equals("C")  //Se o usuario Cadastrado for Cliente Redicrecions spos o Cadastro
                        startActivities(new Intent[]{new Intent(CadastroActivity.this, LoginActivity.class)});
                        finish();
                   }
                   else if(cliente.getTipo().equals("B")){//Se o usuario Cadastrado for Cliente Redicrecions spos o Cadastro
                       startActivities(new Intent[]{new Intent(CadastroActivity.this, CadastroBarbeariaActivity.class)});
                       finish();
                   }*/
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

    public void cadastraFacebook(View view){
        /*AccessToken accessToken = AccountKit.getCurrentAccessToken();

        if (accessToken != null) {
            //Handle Returning User
        } else {
            //Handle new or logged out user
        }*/

        // If using in a fragment
        //loginButton.setFragment(this);

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile");

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //AccessToken accessToken = AccessToken.getCurrentAccessToken();
                //boolean isLoggedIn = accessToken != null && !accessToken.isExpired(); // Ver se a pessoa está conectada
                Log.e("Sucesso!", "facebook:onSuccess:" + loginResult);
                firebaseLoginFacebook(loginResult.getAccessToken());
                Toast.makeText(getApplicationContext(),"Funcionou!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getApplicationContext(),"Cancelado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e("Erro ao logar", "Erro: ", exception);
                Toast.makeText(getApplicationContext(),"Não funcionou", Toast.LENGTH_SHORT).show();
            }
        });

        /*loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            LoginManager.getInstance().logInWithReadPermissions(CadastroActivity.this, Arrays.asList("public_profile"));
            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseLoginFacebook(AccessToken token) { //Pro Facebook
        AuthCredential credencial = FacebookAuthProvider.getCredential(token.getToken());
        autenticacao.signInWithCredential(credencial).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.e("Login com sucesso", "signInWithCredential:success");
                    FirebaseUser user = autenticacao.getCurrentUser();
                    startActivities(new Intent[]{new Intent(CadastroActivity.this, FacebookCadastroActivity.class)});
                    //updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.e("Erro ao logar", "signInWithCredential:failure", task.getException());
                    Toast.makeText(CadastroActivity.this, "Autenticação falhou.", Toast.LENGTH_SHORT).show();
                    //updateUI(null);
                }

                // ...
            }
        });
    }
}

