package com.projeto.integrador.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.projeto.integrador.Configuracoes.ConfiguracaoFirebase;
import com.projeto.integrador.Configuracoes.UsuarioFirebase;
import com.projeto.integrador.R;

public class InicialBarbeiroActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LoginFragment.OnFragmentInteractionListener,
        MapsFragment.OnFragmentInteractionListener,
        AlterarFragment.OnFragmentInteractionListener{

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView nomeUsuario;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getAutenticacao();;
    private FirebaseUser user = autenticacao.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial_barbeiro);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        nomeUsuario = (TextView) headerView.findViewById(R.id.txt_Nome_Login);
        nomeUsuario.setText(user.getDisplayName());

        getSupportFragmentManager().beginTransaction().replace(R.id.conteudo_dos_Fragmentos, new MapsFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_item_one);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragmento = null; //Mudei os dois de lugar
        Class fragmentClass = null;

        switch (menuItem.getItemId()) {
            case R.id.nav_item_one: {
                //tentativa de carregar o maps Dentro do fragmento sem sucesso
                fragmentClass= AlterarFragment.class; //MapsFragment.class;
                Toast.makeText(this, "Menu 1", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.menuSair:{
                FirebaseAuth.getInstance().signOut();
                finish();
                Toast.makeText(this, "Usuário deslogado", Toast.LENGTH_SHORT).show();
                startActivities(new Intent[]{new Intent(this, LoginActivity.class)});
                break;
            }

            case R.id.nav_item_two: {
                Toast.makeText(this, "Menu 2", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_item_three: {
                Toast.makeText(this, "Menu 3", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_item_four: {
                Toast.makeText(this, "Menu 4", Toast.LENGTH_SHORT).show();
                break;
            }
            default: {
                Toast.makeText(this, "Menu Default", Toast.LENGTH_SHORT).show();
                break;
            }
        }

        //Mudei de lugar também
        if(fragmentClass!=null) {
            try {
                fragmento = (Fragment) fragmentClass.newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.conteudo_dos_Fragmentos, fragmento).commit();

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }


    @Override //Caso o navigation drawer aberto usuario clicar ele não fecha a app somente o navigation drawer
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
