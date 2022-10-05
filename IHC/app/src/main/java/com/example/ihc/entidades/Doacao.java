package com.example.ihc.entidades;

import java.util.ArrayList;

public class Doacao {

    private Donatario donatario;
    private Doador doador;
    private ArrayList<Alimento> listaAlimentos;
    private int id;
    private STATUS status;
    private String dataDeEntrega;
    private String nomeDoador;
    private String nomeDonatario;

    public String getNomeDoador() {
        return nomeDoador;
    }

    public void setNomeDoador(String nomeDoador) {
        this.nomeDoador = nomeDoador;
    }

    public String getNomeDonatario() {
        return nomeDonatario;
    }

    public void setNomeDonatario(String nomeDonatario) {
        this.nomeDonatario = nomeDonatario;
    }

    public String getDataDeEntrega() {
        return dataDeEntrega;
    }

    public void setDataDeEntrega(String dataDeEntrega) {
        this.dataDeEntrega = dataDeEntrega;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(int status) {
        switch (status){
            case 1:
                this.status = STATUS.ANDAMENTO;
                break;
            case -1:
                this.status = STATUS.CANCELADA;
                break;
            case 0:
                this.status = STATUS.ESPERA;
                break;
            case 2:
                this.status = STATUS.CONCLUIDA;
                break;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Donatario getDonatario() {
        return donatario;
    }

    public void setDonatario(Donatario donatario) {
        this.donatario = donatario;
    }

    public Doador getDoador() {
        return doador;
    }

    public void setDoador(Doador doador) {
        this.doador = doador;
    }

    public ArrayList<Alimento> getListaAlimentos() {
        return listaAlimentos;
    }

    public void setListaAlimentos(ArrayList<Alimento> listaAlimentos) {
        this.listaAlimentos = listaAlimentos;
    }
}
