package com.projeto.integrador.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.projeto.integrador.Configuracoes.ConfiguracaoFirebase;
import com.projeto.integrador.Model.Barbeiro;
import com.projeto.integrador.Model.Cliente;
import com.projeto.integrador.R;

public class FacebookCadastroActivity extends Activity {

    private Button btnSalvar;
    private Switch switchTipousuario;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();;
    FirebaseUser user = autenticacao.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_cadastro);

        btnSalvar = findViewById(R.id.btnSalvar);
        switchTipousuario=findViewById(R.id.switchTipoUsuario);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String tipoUsuario = "";

            if(switchTipousuario.isChecked()){
                tipoUsuario = "C";
            }
            else{
                tipoUsuario = "B";
            }

            if(tipoUsuario.equals("C")){
                Cliente cliente = new Cliente();
                cliente.setId(user.getUid());
                cliente.setNome(user.getDisplayName());
                cliente.setEmail(user.getEmail());
                cliente.setTipo(tipoUsuario);
                cliente.setSenha(""); //senha vazia porque não sei o que colocar
                cliente.Salvar();

                startActivities(new Intent[]{new Intent(FacebookCadastroActivity.this, InicialClienteActivity.class)});
            }
            else if(tipoUsuario.equals("B")){
                Barbeiro barbeiro = new Barbeiro();
                barbeiro.setId(user.getUid());
                barbeiro.setNome(user.getDisplayName());
                barbeiro.setEmail(user.getEmail());
                barbeiro.setTipo(tipoUsuario);
                barbeiro.setSenha(""); //senha vazia porque não sei o que colocar

                startActivities(new Intent[]{new Intent(FacebookCadastroActivity.this, CadastroBarbeariaActivity.class)});

                Intent intent = new Intent(FacebookCadastroActivity.this, CadastroBarbeariaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("barbeiro", barbeiro);
                intent.putExtras(bundle);
                startActivity(intent);

                finish();
            }
            }
        });
    }

}
