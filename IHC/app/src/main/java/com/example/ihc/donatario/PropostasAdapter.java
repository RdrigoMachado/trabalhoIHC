package com.example.ihc.donatario;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ihc.R;
import com.example.ihc.doador.AlimentosAdapter;
import com.example.ihc.entidades.Doacao;
import com.example.ihc.entidades.Doador;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PropostasAdapter extends BaseAdapter {

    private Context c;
    private ArrayList<Doacao> doacoes;
    private LayoutInflater inflater;
    private int id;

    public PropostasAdapter(Context c, ArrayList<Doacao> doacoes) {
        this.c = c;
        this.doacoes = doacoes;
        inflater = (LayoutInflater)
                c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return doacoes.size();
    }

    @Override
    public Object getItem(int position) {
        return doacoes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Doador doador = new Doador();

        downloadDoador(doacoes.get(position).getDoador().getId(), doador);

        View v = inflater.inflate(R.layout.item_propostas, null);


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirJanelaInfo(doacoes.get(position), doador, position);
            }
        });
        TextView txtAndamentoDoador = (TextView)
                v.findViewById(R.id.txtDestinatario);

        TextView txtAndamentoDataEntrega = (TextView)
                v.findViewById(R.id.txtDataEntregaDonatario);

        Doacao temp = doacoes.get(position);
        if (temp != null) {
            txtAndamentoDoador.setText(String.valueOf(temp.getNomeDoador()));
            txtAndamentoDataEntrega.setText(String.valueOf(temp.getDataDeEntrega()));
        }

        return v;
    }







    private void abrirJanelaContato(Doador doador){
        LayoutInflater layoutinflater = LayoutInflater.from(c);
        View promptUserView = layoutinflater.inflate(R.layout.contato, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
        alertDialogBuilder.setView(promptUserView);

        TextView txtContatoTelefone = (TextView) promptUserView.findViewById(R.id.txtContatoTelefone);
        txtContatoTelefone.setText(doador.getTelefone());
        TextView txtContatoEmail = (TextView) promptUserView.findViewById(R.id.txtContatoEmail);
        txtContatoEmail.setText(doador.getEmail());

        alertDialogBuilder.setPositiveButton("Fechar",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        // all set and time to build and show up!
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void abrirJanelaInfo(Doacao doacao, Doador doador, int posicao){

        // load the dialog_promt_user.xml layout and inflate to view
        LayoutInflater layoutinflater = LayoutInflater.from(c);
        View promptUserView = layoutinflater.inflate(R.layout.info_doacao_donatario2, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);

        alertDialogBuilder.setView(promptUserView);

        TextView txtDonatario = (TextView) promptUserView.findViewById(R.id.txtDoador);
        txtDonatario.setText(doacao.getNomeDoador());
        TextView txtData = (TextView) promptUserView.findViewById(R.id.txtDataEntregaDonatario);
        txtData.setText(doacao.getDataDeEntrega());
        TextView txtEndereco = (TextView) promptUserView.findViewById(R.id.txtEnderecoDonatario);
        txtEndereco.setText(doador.getEndereco());
        ListView listaAlimentosDoacao= (ListView) promptUserView.findViewById(R.id.listaAlimentosDoacao);
        AlimentosAdapter adapter = new AlimentosAdapter(c, doacao.getListaAlimentos());
        listaAlimentosDoacao.setAdapter(adapter);

        alertDialogBuilder.setPositiveButton("Fechar",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();



        Button btAceitar = (Button) promptUserView.findViewById(R.id.btReceber);
        btAceitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarStatus(doacao.getId(), "1", posicao);
                alertDialog.cancel();
            }
        });


        Button btRecusar = (Button) promptUserView.findViewById(R.id.btRecusar);
        btRecusar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mudarStatus(doacao.getId(), "-1", posicao);
                alertDialog.cancel();
            }
        });



        Button btMostrarContato = (Button) promptUserView.findViewById(R.id.btMostrarContato);
        btMostrarContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirJanelaContato(doador);
            }
        });


    }


    public void mudarStatus(int id, String status, int posicao){
        String url = "https://www.ufrgs.br/museudeinformatica/ihc/paginas/mudar_status.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(c);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("1")){
                    doacoes.remove(posicao);
                    notifyDataSetChanged();

                }

                Log.e("mudar", response);
                notifyDataSetChanged();

            }}, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(c, "Fail to get course" + error, Toast.LENGTH_SHORT).show();
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
                params.put("id", id + "");
                params.put("status", status);
                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }





    public void downloadDoador(int id, Doador doador){
        String url = "https://www.ufrgs.br/museudeinformatica/ihc/paginas/visualizar.php";

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(c);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if(!response.equals("-1")){
                        Log.e("onResponse", response);
                        // on below line passing our response to json object.
                        JSONObject json = new JSONObject(response);

                        for (int i = 0; i < json.length(); i++) {
                            doador.setId(Integer.parseInt(json.getString("id")));
                            doador.setEndereco(json.getString("endereco"));
                            doador.setTelefone(json.getString("telefone"));
                            doador.setEmail(json.getString("email"));
                            doador.setNome(json.getString("nome"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }}, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(c, "Fail to get course" + error, Toast.LENGTH_SHORT).show();
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
                params.put("id", id + "");
                params.put("tabela", "doador");
                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}
