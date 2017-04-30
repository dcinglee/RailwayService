package com.railwayservice.application.vo;

import java.math.BigInteger;

public class RsaKeyData {
    private String modulusHex;
    private String exponentHex;

    private String modulus;
    private String exponent;

    public RsaKeyData() {
    }

    public RsaKeyData(BigInteger modulus, BigInteger exponent) {
        this.modulusHex = modulus.toString(16);
        this.exponentHex = exponent.toString(16);
        this.modulus = modulus.toString();
        this.exponent = exponent.toString();
    }

    public String getModulusHex() {
        return modulusHex;
    }

    public void setModulusHex(String modulusHex) {
        this.modulusHex = modulusHex;
    }

    public String getExponentHex() {
        return exponentHex;
    }

    public void setExponentHex(String exponentHex) {
        this.exponentHex = exponentHex;
    }

    public String getModulus() {
        return modulus;
    }

    public void setModulus(String modulus) {
        this.modulus = modulus;
    }

    public String getExponent() {
        return exponent;
    }

    public void setExponent(String exponent) {
        this.exponent = exponent;
    }
}