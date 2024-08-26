package com.internet.javaservices;


public enum UrpRepositorioENUM {
    PRODUCCION(1, "/Reportes/Produccion/");
    
    private int codigo;
    private String address;

    private UrpRepositorioENUM(int codigo, String address) {
        this.codigo = codigo;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
