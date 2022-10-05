package com.example.ihc.doador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ihc.R;
import com.example.ihc.entidades.Doacao;

import java.util.ArrayList;

public class ConcluidasAdapter extends BaseAdapter {

    private Context c;
    private ArrayList<Doacao> doacoes;
    private LayoutInflater inflater;
    private int id;

    public ConcluidasAdapter(Context c, ArrayList<Doacao> doacoes) {
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
        View v = inflater.inflate(R.layout.item_concluido, null);

        TextView txtDestinatario = (TextView)
                v.findViewById(R.id.txtDestinatario);

        TextView txtDataEntrega = (TextView)
                v.findViewById(R.id.txtDataEntregaDonatario);

        Doacao temp = doacoes.get(position);
        if (temp != null) {
            txtDestinatario.setText(String.valueOf(temp.getNomeDonatario()));
            txtDataEntrega.setText(String.valueOf(temp.getDataDeEntrega()));
        }

        return v;
    }

}
