/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigohuffman;

/**
 *
 * @author Diego
 */
public class No {
    
    private String character;
    private double freq;
    private No filhoEsquerdo;
    private No filhoDireito;

    public No() {
   
    }
    
    public No(String character, int f) {
        this.character = character;
        freq = f;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public double getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }


    public No getFilhoEsquerdo() {
        return filhoEsquerdo;
    }

    public void setFilhoEsquerdo(No filhoEsquerdo) {
        this.filhoEsquerdo = filhoEsquerdo;
    }

    public No getFilhoDireito() {
        return filhoDireito;
    }

    public void setFilhoDireito(No filhoDireito) {
        this.filhoDireito = filhoDireito;
    }
    
    
    
}
