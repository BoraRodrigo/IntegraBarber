package com.projeto.integrador.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.projeto.integrador.Configuracoes.ConfiguracaoFirebase;
import com.projeto.integrador.Model.AvaliarBarbearia;
import com.projeto.integrador.Model.Barbearia;
import com.projeto.integrador.Model.Cliente;
import com.projeto.integrador.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VisualizarAvaliacoesActivity extends AppCompatActivity {

    private TextView txtNomeBarbearia;
    private ListView listView;
    private Button btnAvaliarBarbearia;

    private List<AvaliarBarbearia> listaAvaliacoes = new ArrayList<>();

    private Barbearia barbearia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_avaliacoes);

        txtNomeBarbearia = findViewById(R.id.txtNomeBarbearia);
        listView = findViewById(R.id.listView);
        btnAvaliarBarbearia = findViewById(R.id.btnAvaliarBarbearia);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        barbearia = (Barbearia) bundle.getSerializable("barbeariaAtual");

        if(barbearia != null){
            txtNomeBarbearia.setText("Avaliações da Barbearia: "+barbearia.getNomebarbearia());
        }
        else {
            finish(); // Continuava mostrando a tela
        }

        final DatabaseReference usuReference = ConfiguracaoFirebase.getDatabaseReference();
        usuReference.child("avaliacoes").orderByChild("idBarbeiro").equalTo(barbearia.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    AvaliarBarbearia ab = postSnapshot.getValue(AvaliarBarbearia.class);
                    listaAvaliacoes.add(ab);
                }

                final ComentariosAdapter ca = new ComentariosAdapter(VisualizarAvaliacoesActivity.this, listaAvaliacoes);
                listView.setAdapter(ca);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        btnAvaliarBarbearia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getActivity().startActivities(new Intent[]{new Intent(getActivity(), AvaliarBarbeariaActivity.class)});

                Intent intent = new Intent(VisualizarAvaliacoesActivity.this, AvaliarBarbeariaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("barbeariaAtual", barbearia);
                bundle.putSerializable("listaAvaliacoes", (Serializable) listaAvaliacoes);
                intent.putExtras(bundle);
                startActivity(intent);

                finish();
            }
        });
    }
}
