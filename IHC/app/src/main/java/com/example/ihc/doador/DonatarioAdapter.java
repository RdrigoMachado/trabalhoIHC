package com.example.ihc.doador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ihc.R;
import com.example.ihc.entidades.Donatario;

import java.util.ArrayList;

public class DonatarioAdapter extends BaseAdapter {

    private Context c;
    private ArrayList<Donatario> donatarios;
    private LayoutInflater inflater;
    private int id;

    public DonatarioAdapter(Context c, ArrayList<Donatario> donatarios) {
        this.c = c;
        this.donatarios = donatarios;
        inflater = (LayoutInflater)
                c.getSystemService(c.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return donatarios.size();
    }

    @Override
    public Object getItem(int position) {
        return donatarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(R.layout.item_donatario, null);

        TextView txtDdonatario  = (TextView)
                v.findViewById(R.id.txtNome);

        TextView txtEndereco = (TextView)
                v.findViewById(R.id.txtEndereco);

        Donatario donatario = donatarios.get(position);
        if (donatario != null) {
            txtDdonatario.setText(String.valueOf(donatario.getNome()));
            txtEndereco.setText(String.valueOf(donatario.getEndereco()));
        }
        return v;
    }

}