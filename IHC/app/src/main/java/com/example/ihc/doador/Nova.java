package com.example.ihc.doador;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ihc.R;
import com.example.ihc.entidades.Alimento;
import com.example.ihc.entidades.Donatario;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Nova extends AppCompatActivity {

    List<Donatario> donatarios;
    ListView listaAlimentos;
    Spinner spinnerDonatario;
    TextView txtData;
    Button btDatePicker;
    Button btAdd, btCancelar, btSalvar;
    String data = "";
    List<Alimento> alimentos = new ArrayList<Alimento>();
int id_doacao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova);
        setTitle("Adicionar Nova Doação");
        listaAlimentos = (ListView) findViewById(R.id.listaAlimentos);
        spinnerDonatario = (Spinner) findViewById(R.id.spinnerDonatarios);
        btDatePicker = (Button) findViewById(R.id.btDatePicker);
        btCancelar = (Button) findViewById(R.id.btCancelar);
        btCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btSalvar = (Button) findViewById(R.id.btSalvar);
        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int posicao = spinnerDonatario.getSelectedItemPosition();
                if(!data.equals("") && alimentos.size() > 0){
                    criaDoacao(donatarios.get(posicao).getId(), 1, data, (ArrayList<Alimento>) alimentos);
                }
            }
        });

        btAdd = (Button) findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // load the dialog_promt_user.xml layout and inflate to view
                LayoutInflater layoutinflater = LayoutInflater.from(Nova.this);
                View promptUserView = layoutinflater.inflate(R.layout.adicionar_alimento, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Nova.this);

                alertDialogBuilder.setView(promptUserView);

                EditText edNomeAlimento = (EditText) promptUserView.findViewById(R.id.edNomeAlimento);
                EditText edQuantidade = (EditText) promptUserView.findViewById(R.id.edQuantidade);
                Spinner metrica = (Spinner) promptUserView.findViewById(R.id.spinnerMetrica);
                String[] opcoes =  new String[] {"Lt", "KG"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Nova.this,
                        android.R.layout.simple_spinner_item,
                        opcoes);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                metrica.setAdapter(adapter);

                // prompt for username
                alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // and display the username on main activity layout
                        Alimento temp = new Alimento();

                        if(edNomeAlimento.getText().toString().equals("")){
                            return;
                        }
                        if(edQuantidade.getText().toString().equals("")){
                            return;
                        }
                        if(metrica.getSelectedItem().toString().toString().equals("")){
                            return;
                        }

                        temp.setNome(edNomeAlimento.getText().toString());
                        temp.setQuantidade(Integer.parseInt(edQuantidade.getText().toString()));
                        temp.setMetrica(metrica.getSelectedItem().toString());
                        alimentos.add(temp);
                        AlimentoRemovivelAdapter alimentoRemovivelAdapter = new AlimentoRemovivelAdapter(getApplicationContext(), (ArrayList<Alimento>) alimentos);
                        listaAlimentos.setAdapter(alimentoRemovivelAdapter);

                    }
                });

                // all set and time to build and show up!
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });

        carregaListaDeDoacoesAtivas();
    }

    public void atualizaData(int dia, int mes, int ano){
        String mes_dois_digitos;
        String dia_dois_digitos;
        if (mes < 10){
            mes_dois_digitos =  "0" + mes;
        } else {
            mes_dois_digitos =  "" + mes;
        }

        if (dia < 10){
            dia_dois_digitos =  "0" + dia;
        } else {
            dia_dois_digitos =  "" + dia;
        }

        this.data = ano + "-" + mes_dois_digitos + "-" + dia_dois_digitos;
        btDatePicker.setText(dia_dois_digitos + "/" + mes_dois_digitos + "/" + ano);
    }



    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(this);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public  static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        Nova instancia;
        public DatePickerFragment(Nova instancia)
        {
            this.instancia = instancia;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(requireContext(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            instancia.atualizaData(day, month + 1, year);
        }
    }


    private void carregaListaDeDoacoesAtivas(){

        String URL = "https://www.ufrgs.br/museudeinformatica/ihc/paginas/listar_donatarios.php";

        donatarios = new ArrayList<Donatario>();

        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(Nova.this);

        StringRequest request = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // on below line passing our response to json object.
                    JSONArray json = new JSONArray(response);

                    for (int i = 0; i < json.length(); i++) {
                        Donatario d1 = new Donatario();
                        d1.setId(json.getJSONObject(i).getInt("id"));
                        d1.setNome(json.getJSONObject(i).getString("nome"));
                        d1.setEmail(json.getJSONObject(i).getString("email"));
                        d1.setTelefone(json.getJSONObject(i).getString("telefone"));
                        d1.setCpfCnpj(json.getJSONObject(i).getString("cpf_cnpj"));
                        d1.setEndereco(json.getJSONObject(i).getString("endereco"));


                        donatarios.add(d1);
                        DonatarioAdapter adapter = new DonatarioAdapter(getApplicationContext(), (ArrayList<Donatario>) donatarios);
                        spinnerDonatario.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }}, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(Nova.this, "Fail to get course" + error, Toast.LENGTH_SHORT).show();
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

                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }


    private void criaDoacao(int idDonatario, int idDoador, String data, ArrayList<Alimento> alimentos){
        String URL = "https://www.ufrgs.br/museudeinformatica/ihc/Negocio/adicionar.php";
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(Nova.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               if(!response.equals("-1")){
                   try {
                       id_doacao = Integer.parseInt(response);
                       for (Alimento alimento : alimentos) {
                           criarAlimentos(id_doacao, alimento);
                       }
                       Toast.makeText(Nova.this, "Adicionado", Toast.LENGTH_LONG).show();
                       finish();
                   }catch (Exception e){
                       Log.e("onErrorResponse:" , response);
                       Log.e("onErrorResponse:" , e.toString());
                   }
               }
            }}, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(Nova.this, "Fail to get course" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("onErrorResponse:" , error.toString());

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

                params.put("tabela", "doacao");
                params.put("id_doador", "" + idDoador);
                params.put("id_donatario", "" + idDonatario);
                params.put("data_entrega", data);
                params.put("status", "0");

                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
    private void criarAlimentos(int id_doacao, Alimento alimento){
        String URL = "https://www.ufrgs.br/museudeinformatica/ihc/Negocio/adicionar.php";
        // creating a new variable for our request queue
        RequestQueue queue = Volley.newRequestQueue(Nova.this);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Nova.this, "Resposta: " + response, Toast.LENGTH_LONG).show();
                Log.e("onResponse:" , response);
            }}, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(Nova.this, "Fail to get course" + error.toString(), Toast.LENGTH_LONG).show();
                Log.e("onErrorResponse:" , error.toString());

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

                params.put("tabela", "alimento");
                params.put("id_doacao", "" + id_doacao);
                params.put("nome", "" + alimento.getNome());
                params.put("quantidade", String.valueOf(alimento.getQuantidade()));
                params.put("metrica", alimento.getMetrica());


                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}