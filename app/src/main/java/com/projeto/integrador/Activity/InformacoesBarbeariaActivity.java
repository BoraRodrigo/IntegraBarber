package com.projeto.integrador.Activity;

import android.content.Intent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.projeto.integrador.Model.Barbearia;
import com.projeto.integrador.Model.Barbeiro;
import com.projeto.integrador.R;

public class InformacoesBarbeariaActivity extends AppCompatActivity {

    Barbearia barbearia;
    private TextView txtNomeDaBarbearia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes_barbearia);
    }
}
