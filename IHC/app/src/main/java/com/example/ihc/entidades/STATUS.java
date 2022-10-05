package com.example.ihc.entidades;

public enum STATUS {
    CONCLUIDA("Concluída"),
    CANCELADA("Cancelada"),
    ANDAMENTO("Andamento"),
    ESPERA("Em análise");

    private String textRepresentation;

    private STATUS(String textRepresentation) {
        this.textRepresentation = textRepresentation;
    }

    @Override public String toString() {
        return textRepresentation;
    }
}
