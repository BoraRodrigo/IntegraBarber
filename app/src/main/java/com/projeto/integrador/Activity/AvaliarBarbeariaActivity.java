package com.projeto.integrador.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.projeto.integrador.Configuracoes.ConfiguracaoFirebase;
import com.projeto.integrador.Model.AvaliarBarbearia;
import com.projeto.integrador.Model.Barbearia;
import com.projeto.integrador.Model.Barbeiro;
import com.projeto.integrador.R;

public class AvaliarBarbeariaActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private Button btnSalvar;
    private TextView txtNomeBarbearia, txtAvaliacao;
    private EditText txtComentario;

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();
    private FirebaseUser user = autenticacao.getCurrentUser();
    DatabaseReference fireReference = ConfiguracaoFirebase.getDatabaseReference();

    private Barbearia barbearia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliar_barbearia);

        ratingBar = findViewById(R.id.ratingBar);
        btnSalvar = findViewById(R.id.btnSalvar);
        txtNomeBarbearia = findViewById(R.id.txtNomeBarbearia);
        txtAvaliacao = findViewById(R.id.txtAvaliacao);
        txtComentario = findViewById(R.id.txtComentario);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        barbearia = (Barbearia) bundle.getSerializable("barbeariaAtual");

        if(barbearia != null){
            txtNomeBarbearia.setText("Avalie a barbearia: "+barbearia.getNomebarbearia());
        }
        else {
            finish(); // Continuava mostrando a tela
        }

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAvaliacao.setText("Sua avaliação é de: "+ratingBar.getRating());
                AvaliarBarbearia ab = new AvaliarBarbearia();
                ab.setId(fireReference.push().getKey());
                ab.setIdCliente(user.getUid());
                ab.setIdBarbeiro(barbearia.getId());
                ab.setAvaliacao(ratingBar.getRating());
                ab.setComentario(txtComentario.getText().toString());
                ab.Salvar();

                finish();
            }
        });
    }
}
