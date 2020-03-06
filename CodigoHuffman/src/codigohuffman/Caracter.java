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
public class Caracter {
    private double freq;
    private char character;

    public Caracter(int freq, char character) {
        this.freq = freq;
        this.character = character;
    }

    public double getFreq() {
        return freq;
    }

    public void setFreq(double freq) {
        this.freq = freq;
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }
    
}
