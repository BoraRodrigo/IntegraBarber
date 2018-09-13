package com.projeto.integrador.Activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.projeto.integrador.Configuracoes.ConfiguracaoFirebase;
import com.projeto.integrador.Configuracoes.UsuarioFirebase;
import com.projeto.integrador.Model.Cliente;
import com.projeto.integrador.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AlterarFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AlterarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlterarFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextInputEditText txtEmail,txtNome, txtSenha;
    private Button btnAlterar;
    private Cliente cliente;
    private OnFragmentInteractionListener mListener;

    public AlterarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlterarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlterarFragment newInstance(String param1, String param2) {
        AlterarFragment fragment = new AlterarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alterar, container, false);

        txtEmail = view.findViewById(R.id.txtEmailCadastro);
        txtNome = view.findViewById(R.id.txtNomeCadastro);
        txtSenha = view.findViewById(R.id.txtSenhacadastro);
        btnAlterar = view.findViewById(R.id.btnAlterar);

        final FirebaseUser usuario = UsuarioFirebase.getUsuarioAtual();
        final DatabaseReference usuReference = ConfiguracaoFirebase.getDatabaseReference();

        usuReference.child("clientes").orderByChild("email").equalTo(usuario.getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    cliente = postSnapshot.getValue(Cliente.class);
                    txtNome.setText(cliente.getNome());
                    txtEmail.setText(cliente.getEmail());
                    txtSenha.setText(cliente.getSenha());
                    break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cliente.setNome(txtNome.getText().toString());
                cliente.setEmail(txtEmail.getText().toString());
                cliente.setSenha(txtSenha.getText().toString());
                if(!txtSenha.getText().toString().isEmpty()){
                    cliente.setSenha(txtSenha.getText().toString());
                    usuario.updatePassword(cliente.getSenha()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.e("Senha", "Galvão, diga Tino, vai mudar");
                            }
                        }
                    });
                }
                usuario.updateEmail(cliente.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.e("Email", "Galvão, diga Tino, vai mudar");
                        }
                    }
                });
                usuReference.child("clientes").child(cliente.getId()).setValue(cliente);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
