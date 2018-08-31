package com.projeto.integrador.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.projeto.integrador.Configuracoes.UsuarioFirebase;
import com.projeto.integrador.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
    }

    public void  abrirTelaCadastro(View view){
            startActivities(new Intent[]{new Intent(this, CadastroActivity.class)});
}


    public void  abrirTelaLogin(View view){
        startActivities(new Intent[]{new Intent(this, LoginActivity.class)});
    }

    @Override
    protected void onRestart() {//se caso o usuario já estiver logado ele redireciona automaticamente
        super.onRestart();
        UsuarioFirebase.redirecionaUsuarioLogado(MainActivity.this);
    }
}
