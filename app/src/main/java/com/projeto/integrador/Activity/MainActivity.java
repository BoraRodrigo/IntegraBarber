package com.projeto.integrador.Activity;

import android.Manifest;
import android.app.Activity;
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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.projeto.integrador.Configuracoes.ConfiguracaoFirebase;
import com.projeto.integrador.Configuracoes.PermissoesMaps;
import com.projeto.integrador.Configuracoes.UsuarioFirebase;
import com.projeto.integrador.Model.Barbeiro;
import com.projeto.integrador.Model.Cliente;
import com.projeto.integrador.R;

import static com.projeto.integrador.Configuracoes.UsuarioFirebase.getIdentificadoUsuario;

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

        final FirebaseUser user = autenticacao.getCurrentUser();
        if(user != null){
            Log.e("Logado", user.getEmail());
            //redirecionaUsuario();
            UsuarioFirebase.redirecionaUsuarioLogado(MainActivity.this);
        }
        else { // Comentado para não deixar tela em branco enquanto ele não redirecina o usuário
            setContentView(R.layout.activity_main);
        }

        //autenticacao.signOut();// Desloga possivel usuariologado na conta (Comentei esta linha para ele entrar automaticamente)

        /*final DatabaseReference usuReference = ConfiguracaoFirebase.getDatabaseReference();
        usuReference.child("clientes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Cliente c = postSnapshot.getValue(Cliente.class);
                    Log.e("", c.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });*/

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
        //redirecionaUsuario();
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

    public void redirecionaUsuario(){//passa a activy como parametro
        autenticacao= ConfiguracaoFirebase.getAutenticacao();
        final DatabaseReference usuReference = ConfiguracaoFirebase.getDatabaseReference();//.child("clientes").child(getIdentificadoUsuario()); //pega o id logado;
        final FirebaseUser user = autenticacao.getCurrentUser();

        usuReference.child("clientes").orderByChild("email").equalTo(user.getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int barbeiroEntra = 0;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Cliente c = postSnapshot.getValue(Cliente.class);
                    Log.e("Cliente","Entrou");
                    startActivities(new Intent[]{new Intent(MainActivity.this, InicialClienteActivity.class)});
                    barbeiroEntra = 1;
                    finish();
                    break;
                }
                if(barbeiroEntra == 0){
                    Log.e("Barbeiro","Entrou");
                    startActivities(new Intent[]{new Intent(MainActivity.this, InicialBarbeiroActivity.class)});
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
