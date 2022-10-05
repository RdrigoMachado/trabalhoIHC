package com.example.ihc.donatario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ihc.R;

public class TelaInicialDonatario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_inicial_donatario);

        Button btPropostas = (Button) findViewById(R.id.btPropostas);
        Button btAndamentos = (Button) findViewById(R.id.btAndamentos);

        btPropostas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirJanelaPropostas();
            }
        });

        btAndamentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirJanelaAndamentos();
            }
        });
    }

    private void abrirJanelaPropostas(){
        Intent abrirJanelaPropostas = new Intent(this, Propostas.class);
        startActivity(abrirJanelaPropostas);
    }

    private void abrirJanelaAndamentos(){
        Intent abrirJanelaAndamentos = new Intent(this, Andamentos.class);
        startActivity(abrirJanelaAndamentos);
    }
}