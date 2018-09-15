package com.projeto.integrador.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.projeto.integrador.Activity.LoginActivity;
import com.projeto.integrador.Configuracoes.ConfiguracaoFirebase;
import com.projeto.integrador.Configuracoes.UsuarioFirebase;
import com.projeto.integrador.Model.Barbearia;
import com.projeto.integrador.Model.Barbeiro;

//import barbearia.integradorvi.com.br.integradorbarber.Configuracoes.ConfiguracaoFirebase;
//import barbearia.integradorvi.com.br.integradorbarber.Configuracoes.UsuarioFirebase;
//import barbearia.integradorvi.com.br.integradorbarber.Model.Barbearia;
//import barbearia.integradorvi.com.br.integradorbarber.Model.Barbeiro;
//import barbearia.integradorvi.com.br.integradorbarber.R;
import com.projeto.integrador.R;

public class CadastroBarbeariaActivity extends AppCompatActivity {

    private TextInputEditText txtNomeBarbearia, txtDescricao, txtTelefone, txtPagina, txtRua, txtCidade, txtCEP,txtnumero;
    private FirebaseAuth autenticacao;

    Barbeiro barbeiro;
    DatabaseReference databaseReference;// = FirebaseDatabase.getInstance().getReferenceFromUrl("https://integrabarber-65f96.firebaseio.com/");;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_barbearia);

        txtNomeBarbearia = findViewById(R.id.txtNomeBarbearia);
        txtDescricao = findViewById(R.id.txtDescricao);
        txtTelefone = findViewById(R.id.txtTelefone);
        txtPagina = findViewById(R.id.txtPagina);
        txtRua = findViewById(R.id.txtRua);
        txtCidade = findViewById(R.id.txtCidade);
        txtCEP = findViewById(R.id.txtCEP);
        txtnumero=findViewById(R.id.txtNumero);

    }

    public void validaCampos(View view) {
        if (!txtNomeBarbearia.getText().toString().isEmpty()) {
            if (!txtDescricao.getText().toString().isEmpty()) {
                if (!txtTelefone.getText().toString().isEmpty()) {
                    if (!txtPagina.getText().toString().isEmpty()) {
                        if (!txtRua.getText().toString().isEmpty()) {
                            if (!txtCidade.getText().toString().isEmpty()) {
                                if (!txtCEP.getText().toString().isEmpty()) {
                                    if (!txtnumero.getText().toString().isEmpty()) {


                                        Barbearia barbearia = new Barbearia();
                                        barbearia.setNomebarbearia(txtNomeBarbearia.getText().toString());
                                        barbearia.setDescricao(txtDescricao.getText().toString());
                                        barbearia.setTelefone(txtTelefone.getText().toString());
                                        barbearia.setPagina(txtPagina.getText().toString());
                                        barbearia.setRua(txtRua.getText().toString());
                                        barbearia.setCidade(txtCidade.getText().toString());
                                        barbearia.setCep(txtCEP.getText().toString());
                                        barbearia.setNumero(Integer.parseInt(txtnumero.getText().toString()));

                                        Intent intent = getIntent();
                                        Bundle bundle = intent.getExtras();
                                        barbeiro = (Barbeiro) bundle.getSerializable("barbeiro");

                                        barbearia.setIdBarbeiro(barbeiro.getId());

                                        cadastrarBarbearia(barbearia);
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Preencha o Numero", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Preencha o CEP", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Preencha a Cidade", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Preencha a Rua", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Preencha a Página", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Preencha o Telefone", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Preencha a Descrição", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Preencha o Nome da Barbearia", Toast.LENGTH_SHORT).show();
        }
    }

    public void cadastrarBarbearia(final Barbearia barbearia) {
        autenticacao = ConfiguracaoFirebase.getAutenticacao();
        //autenticacao.createUserWithEmailAndPassword(barbeiro.getEmail(), barbeiro.getSenha()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
        //@Override
        //public void onComplete(@NonNull Task<AuthResult> task) {
        //if (task.isSuccessful()) {

        //String idBarbearia = task.getResult().getUser().getUid();
        //barbearia.setId(idBarbearia);

        barbearia.setId(autenticacao.getUid());

        barbeiro.Salvar();
        barbearia.Salvar();

        UsuarioFirebase.atualizarNomeUsuario(barbearia.getNomebarbearia());

        ////Atualizar Nome no UserProfile no perfil
        //UsuarioFirebase.atualizarNomeUsuario(cliente.getNome()); POR CAUSA DO TESTE

        //if (cliente != null) {//TESTE //cliente.getTipo().equals("C")  //Se o usuario Cadastrado for Cliente Redicrecions spos o Cadastro
        startActivities(new Intent[]{new Intent(CadastroBarbeariaActivity.this, LoginActivity.class)});
        finish();

        //Toast.makeText(getApplicationContext(), "Chegou", Toast.LENGTH_SHORT).show();
        //}
        //} else {//Trata execesões do firebase se caso não realizar cadastro
        //String exececao = "";
                    /*try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        Toast.makeText(getApplicationContext(), "Digite Uma senha mais Forte", Toast.LENGTH_SHORT).show();

                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        Toast.makeText(getApplicationContext(), "E-mail Invalido", Toast.LENGTH_SHORT).show();

                    } catch (FirebaseAuthUserCollisionException e) {
                        Toast.makeText(getApplicationContext(), "Conta já Cadastrada", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Erro ao Cadastrar", Toast.LENGTH_SHORT).show();
                    }*/
        //}
    }
    //});
    //}
}
