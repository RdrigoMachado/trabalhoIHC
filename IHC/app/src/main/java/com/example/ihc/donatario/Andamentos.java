package com.example.ihc.donatario;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.ArrayLinkedVariables;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ihc.R;
import com.example.ihc.donatario.AndamentoAdapter;
import com.example.ihc.entidades.Alimento;
import com.example.ihc.entidades.Doacao;
import com.example.ihc.entidades.Doador;
import com.example.ihc.entidades.Donatario;
import com.example.ihc.entidades.STATUS;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Andamentos extends AppCompatActivity {

    private ArrayList<Doacao> doacoes;
    private AndamentoAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_andamentos2);
        doacoes = new ArrayList<Doacao>();
        carregaDadosEAtualizaLista();
        ListView listaDoacaoesAndamentos = (ListView) findViewById(R.id.listaDoacaoesAndamentos);
        adapter = new AndamentoAdapter(this, doacoes);
        listaDoacaoesAndamentos.setAdapter(adapter);
    }

    private void carregaDadosEAtualizaLista(){
        String url = "https://www.ufrgs.br/museudeinformatica/ihc/paginas/listar_doacoes_ativas.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(com.example.ihc.donatario.Andamentos.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // on below line passing our response to json object.
                    JSONArray json = new JSONArray(response);

                    for (int i = 0; i < json.length(); i++) {
                        Doacao d1 = new Doacao();
                        d1.setId(Integer.parseInt(json.getJSONObject(i).getString("id")));
                        d1.setStatus(json.getJSONObject(i).getInt("status"));
                        d1.setDataDeEntrega(json.getJSONObject(i).getString("data_entrega"));
                        d1.setNomeDoador(json.getJSONObject(i).getString("nome_doador"));
                        d1.setNomeDonatario(json.getJSONObject(i).getString("nome_donatario"));
                        Doador doador = new Doador();
                        doador.setId(Integer.parseInt(json.getJSONObject(i).getString("id_doador")));
                        d1.setDoador(doador);
                        ArrayList<Alimento> listaTemporario = new ArrayList<Alimento>();
                        Log.e("doacao/alimento",  response);

                        JSONArray alimentos = new JSONArray(json.getJSONObject(i).getString("alimentos"));
                        for (int j = 0; j < alimentos.length(); j++) {
                            Alimento temp = new Alimento();
                            temp.setNome(alimentos.getJSONObject(j).getString("nome"));
                            temp.setQuantidade(alimentos.getJSONObject(j).getInt("quantidade"));
                            temp.setMetrica(alimentos.getJSONObject(j).getString("metrica"));
                            listaTemporario.add(temp);
                            Log.e("doacao/alimento",  "alimento adicinado");

                        }
                        d1.setListaAlimentos(listaTemporario);

                        doacoes.add(d1);
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }}, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(com.example.ihc.donatario.Andamentos.this, "Fail to get course" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                // as we are passing data in the form of url encoded
                // so we are passing the content type below
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();
                // on below line we are passing our key and value pair to our parameters.
                params.put("id", "1");
                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}