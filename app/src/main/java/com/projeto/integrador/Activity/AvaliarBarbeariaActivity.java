package com.projeto.integrador.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.projeto.integrador.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AvaliarBarbeariaActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private Button btnSalvar;
    private TextView txtNomeBarbearia, txtAvaliacao;
    private EditText txtComentario;

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();
    private FirebaseUser user = autenticacao.getCurrentUser();
    DatabaseReference fireReference = ConfiguracaoFirebase.getDatabaseReference();

    private Barbearia barbearia;
    private AvaliarBarbearia avaliacaoAtual;

    private List<AvaliarBarbearia> listaAvaliacoes;

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

        final SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
        final Date dataAtual = new Date();

        listaAvaliacoes = (List<AvaliarBarbearia>) bundle.getSerializable("listaAvaliacoes");

        if(listaAvaliacoes != null){
            for(int i=0; i<listaAvaliacoes.size(); i++){
                if(listaAvaliacoes.get(i).getIdCliente().equals(user.getUid())){
                    ratingBar.setRating(listaAvaliacoes.get(i).getAvaliacao());
                    txtComentario.setText(listaAvaliacoes.get(i).getComentario());

                    avaliacaoAtual = listaAvaliacoes.get(i);

                    break;
                }
            }
        }

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtAvaliacao.setText("Sua avaliação é de: "+ratingBar.getRating());

                AvaliarBarbearia ab;

                if(avaliacaoAtual == null){
                    ab = new AvaliarBarbearia();
                    ab.setId(fireReference.push().getKey());
                }
                else{
                    ab = avaliacaoAtual;
                    ab.setId(avaliacaoAtual.getId());
                }

                ab.setIdCliente(user.getUid());
                ab.setNomeCliente(user.getDisplayName());
                ab.setIdBarbeiro(barbearia.getId());
                ab.setAvaliacao(ratingBar.getRating());
                ab.setComentario(txtComentario.getText().toString());
                ab.setDataAvaliacao(formataData.format(dataAtual));
                ab.Salvar();

                finish();
            }
        });
    }
}
