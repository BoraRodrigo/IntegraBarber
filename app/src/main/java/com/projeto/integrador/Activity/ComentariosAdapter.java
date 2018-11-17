package com.projeto.integrador.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
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

import java.util.ArrayList;
import java.util.List;

public class ComentariosAdapter extends BaseAdapter {
    private TextView txtNomeUsuario, txtData, txtComentario;
    private RatingBar avaliacaoUsuario;

    private Activity act;

    private List<AvaliarBarbearia> listaAvaliacoes; // = new ArrayList<>();

    public ComentariosAdapter(Activity act, List<AvaliarBarbearia> listaAvaliacoes){
        this.act = act;
        this.listaAvaliacoes = listaAvaliacoes;
    }

    @Override
    public int getCount() {
        return listaAvaliacoes.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int posicao, View v, ViewGroup viewGroup) {
        View view = act.getLayoutInflater().inflate(R.layout.comentarios_adapter, viewGroup, false);

        txtNomeUsuario = view.findViewById(R.id.txtNomeUsuario);
        txtData = view.findViewById(R.id.txtData);
        avaliacaoUsuario = view.findViewById(R.id.avaliacaoUsuario);
        txtComentario = view.findViewById(R.id.txtComentario);

        AvaliarBarbearia avaliarBarbearia = listaAvaliacoes.get(posicao);

        txtNomeUsuario.setText(avaliarBarbearia.getNomeCliente());
        txtData.setText(avaliarBarbearia.getDataAvaliacao());
        avaliacaoUsuario.setRating(Integer.parseInt(String.valueOf(avaliarBarbearia.getAvaliacao().toString().charAt(0))));
        txtComentario.setText(avaliarBarbearia.getComentario());

        return view;
    }
}
