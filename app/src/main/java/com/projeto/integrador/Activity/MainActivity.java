package com.projeto.integrador.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.projeto.integrador.Configuracoes.ConfiguracaoFirebase;
import com.projeto.integrador.Configuracoes.PermissoesMaps;
import com.projeto.integrador.Configuracoes.UsuarioFirebase;
import com.projeto.integrador.R;



public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;

    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };///Lista de permisoes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Validar permissões
        PermissoesMaps.validarPermissoes(permissoes, this, 1);

        autenticacao= ConfiguracaoFirebase.getAutenticacao();
        autenticacao.signOut();// Desloga possivel usuariologado na conta

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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int permissaoResultado : grantResults){
            if( permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }

    }
    private void alertaValidacaoPermissao(){//Alerta de permissão se caso alguma estiver negada
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();


    }





}
