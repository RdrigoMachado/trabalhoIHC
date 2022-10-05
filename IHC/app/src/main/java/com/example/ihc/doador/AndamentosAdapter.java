package com.example.ihc.doador;
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
import com.example.ihc.entidades.Doacao;
import com.example.ihc.entidades.Donatario;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AndamentosAdapter extends BaseAdapter {

    private Context c;
    private ArrayList<Doacao> doacoes;
    private LayoutInflater inflater;
    private int id;

    public AndamentosAdapter(Context c, ArrayList<Doacao> doacoes) {
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

        Donatario donatario = new Donatario();
        downloadDonatario(doacoes.get(position).getDonatario().getId(), donatario);

        View v = inflater.inflate(R.layout.item_andamento, null);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirJanelaInfo(doacoes.get(position), donatario);
            }
        });
        TextView txtDestinatario = (TextView)
                v.findViewById(R.id.txtNome);

        TextView txtStatus = (TextView)
                v.findViewById(R.id.txtEndereco);

        TextView txtData = (TextView)
                v.findViewById(R.id.txtData);

        Button btExcluirLista = (Button)
                v.findViewById(R.id.btRemoverAlimento);

        btExcluirLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Andamentos)c).abrirCaixaDeConfirmacaoParaDeletar(position);
            }
        });

        Doacao temp = doacoes.get(position);
        if (temp != null) {
            txtDestinatario.setText(String.valueOf(temp.getNomeDonatario()));
            txtStatus.setText(String.valueOf(temp.getStatus().toString()));

            String data_banco = temp.getDataDeEntrega().toString();

            String ano = data_banco.substring(0, 4);
            String mes = data_banco.substring(5, 7);
            String dia = data_banco.substring(8, 10);

            txtData.setText(dia + "/" + mes + "/" + ano);
        }

        return v;
    }


    private void abrirJanelaContato(Donatario donatario){
        LayoutInflater layoutinflater = LayoutInflater.from(c);
        View promptUserView = layoutinflater.inflate(R.layout.contato, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
        alertDialogBuilder.setView(promptUserView);

        TextView txtContatoTelefone = (TextView) promptUserView.findViewById(R.id.txtContatoTelefone);
        txtContatoTelefone.setText(donatario.getTelefone());
        TextView txtContatoEmail = (TextView) promptUserView.findViewById(R.id.txtContatoEmail);
        txtContatoEmail.setText(donatario.getEmail());

        alertDialogBuilder.setPositiveButton("Fechar",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        // all set and time to build and show up!
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void abrirJanelaInfo(Doacao doacao, Donatario donatario){

        // load the dialog_promt_user.xml layout and inflate to view
        LayoutInflater layoutinflater = LayoutInflater.from(c);
        View promptUserView = layoutinflater.inflate(R.layout.info_doacao, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);

        alertDialogBuilder.setView(promptUserView);

        TextView txtDonatario = (TextView) promptUserView.findViewById(R.id.txtDoador);
        txtDonatario.setText(doacao.getNomeDonatario());
        TextView txtData = (TextView) promptUserView.findViewById(R.id.txtDataEntregaDonatario);
        txtData.setText(doacao.getDataDeEntrega());
        TextView txtEndereco = (TextView) promptUserView.findViewById(R.id.txtEnderecoDonatario);
        txtEndereco.setText(donatario.getEndereco());
        ListView listaAlimentosDoacao= (ListView) promptUserView.findViewById(R.id.listaAlimentosDoacao);
        AlimentosAdapter adapter = new AlimentosAdapter(c, doacao.getListaAlimentos());
        listaAlimentosDoacao.setAdapter(adapter);



        Button btMostrarContato = (Button) promptUserView.findViewById(R.id.btMostrarContato);
        btMostrarContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirJanelaContato(donatario);
            }
        });
        // prompt for username
        alertDialogBuilder.setPositiveButton("Fechar",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        // all set and time to build and show up!
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    public void downloadDonatario(int id, Donatario donatario){
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
                            donatario.setId(Integer.parseInt(json.getString("id")));
                            donatario.setEndereco(json.getString("endereco"));
                            donatario.setTelefone(json.getString("telefone"));
                            donatario.setEmail(json.getString("email"));
                            donatario.setNome(json.getString("nome"));
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
                params.put("tabela", "donatario");
                // at last we are returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}