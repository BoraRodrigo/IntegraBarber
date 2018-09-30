package com.projeto.integrador.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
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
    private Button button2;

    int clienteAlterando = 0;

    Barbeiro barbeiro;

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

        button2 = findViewById(R.id.button2);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        barbeiro = (Barbeiro) bundle.getSerializable("barbeiroAlterado");
        if(barbeiro == null){
            Log.e("Barbeiro", "precisa ser novo");
            barbeiro = (Barbeiro) bundle.getSerializable("barbeiro");
            //barbeiro = new Barbeiro();
        }
        else{
            Log.e("Barbeiro atual", barbeiro.getEmail().toString());
            colocaDados();
        }

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

                                        /*Intent intent = getIntent();
                                        Bundle bundle = intent.getExtras();

                                        /*if(barbeiro != null){
                                            barbeiro = (Barbeiro) bundle.getSerializable("barbeiro");
                                        }

                                        /*if(clienteAlterando == 0){
                                            barbeiro = (Barbeiro) bundle.getSerializable("barbeiro");
                                        }
                                        else if(clienteAlterando == 1){
                                            barbeiro = (Barbeiro) bundle.getSerializable("barbeiroAlterado");
                                        }*/

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

        barbearia.setId(autenticacao.getUid());

        barbeiro.Salvar();
        barbearia.Salvar();

        UsuarioFirebase.atualizarNomeUsuario(barbearia.getNomebarbearia());

        ////Atualizar Nome no UserProfile no perfil
        //UsuarioFirebase.atualizarNomeUsuario(cliente.getNome());

        startActivities(new Intent[]{new Intent(CadastroBarbeariaActivity.this, LoginActivity.class)});
        finish();

    }

    public void colocaDados(){//Barbeiro bar
        final DatabaseReference usuReference = ConfiguracaoFirebase.getDatabaseReference();
        usuReference.child("barbearia").orderByChild("id").equalTo(barbeiro.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Barbearia barbearia = postSnapshot.getValue(Barbearia.class);
                    txtNomeBarbearia.setText(barbearia.getNomebarbearia());
                    txtDescricao.setText(barbearia.getDescricao());
                    txtTelefone.setText(barbearia.getTelefone());
                    txtPagina.setText(barbearia.getPagina());
                    txtRua.setText(barbearia.getRua());
                    txtnumero.setText(String.valueOf(barbearia.getNumero()));
                    txtCidade.setText(barbearia.getCidade());
                    txtCEP.setText(barbearia.getCep());

                    button2.setText("Alterar");

                    clienteAlterando = 1;

                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
