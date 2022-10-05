package com.example.ihc.doador;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.ihc.R;
import com.example.ihc.entidades.Alimento;

import java.util.ArrayList;

public class AlimentoRemovivelAdapter extends BaseAdapter {

    private Context c;
    private ArrayList<Alimento> alimentos;
    private LayoutInflater inflater;
    private int id;

    public AlimentoRemovivelAdapter(Context c, ArrayList<Alimento> alimentos) {
        this.c = c;
        this.alimentos = alimentos;
        inflater = (LayoutInflater)
                c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return alimentos.size();
    }

    @Override
    public Object getItem(int position) {
        return alimentos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(R.layout.item_alimento_removivel, null);

        TextView txtNome = (TextView)
                v.findViewById(R.id.txtNome);

        TextView txtQuantidade = (TextView)
                v.findViewById(R.id.txtQuantidade);
        TextView txtMetrica = (TextView)
                v.findViewById(R.id.txtMetrica);

        Button btRemover = (Button) v.findViewById(R.id.btRemoverAlimento);

        btRemover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                remove(position);
            }

        });

        Alimento temp = alimentos.get(position);
        if (temp != null) {
            txtNome.setText(temp.getNome());
            txtQuantidade.setText(String.valueOf(temp.getQuantidade()));
            txtMetrica.setText(temp.getMetrica());
        }

        return v;
    }

    public void remove(int position){
        alimentos.remove(alimentos.get(position));
        Log.e("dentro do adapter", alimentos.size() + "");

        notifyDataSetChanged();
    }


}