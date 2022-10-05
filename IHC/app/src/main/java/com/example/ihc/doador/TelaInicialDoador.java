package com.example.ihc.doador;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ihc.R;


public class TelaInicialDoador extends AppCompatActivity {

    private Button btNova, btAndamentos, btConcluidas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_inicial_doador);
        setTitle("Doações");

        btNova =(Button) findViewById(R.id.btPropostas);
        btAndamentos =(Button) findViewById(R.id.btAndamentos);
        btConcluidas =(Button) findViewById(R.id.btConcluidos);

        btNova.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaNova();
            }
        });
        btAndamentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaAndamentos();
            }
        });
        btConcluidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaConcluidas();
            }
        });

    }

    public void abrirTelaNova(){
        Intent abrirTelaNova = new Intent(this, Nova.class);
        startActivity(abrirTelaNova);
    }
    private void abrirTelaAndamentos(){
        Intent abrirTelaAndamentos = new Intent(this, Andamentos.class);
        startActivity(abrirTelaAndamentos);
    }
    public void abrirTelaConcluidas(){
        Intent abrirTelaConcluidas = new Intent(this, Concluidas.class);
        startActivity(abrirTelaConcluidas);
    }
}