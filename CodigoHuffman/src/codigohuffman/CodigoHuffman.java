/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package codigohuffman;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

/**
 *
 * @author Diego
 */
public class CodigoHuffman {

    static PrintWriter gravaArq;
    static List<No> tabelaDeCodigo = new ArrayList();
    static List<Codigo> tabelaDeCodigos = new ArrayList();
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {

        criarArquivoFREQUENCIA();
        
        // criar o arquivo TABELADECODIGOS.txt utilizando TEXTOCOMUM.txt e FREQUENCIA.txt e o algoritmo ArvoreDeHuffman que foi implementado
        criarArquivoTABELADECODIGOS();
        
        // Lendo tabela de codigos e armazenando os dados na lista tabelaDeCodigos para utiliza-la na codificacao e decodificacao
        lerTabelaDeCodigos();
        
        //cria o arquivo CODIFICADO.txt
        codificarTexto();
        //cria o arquivo TEXTODECODIFICADO.txt
        decodificar();
        
        // TODO code application logic here
    }
    
    static void criarArquivoFREQUENCIA() throws IOException{
       
        // Le TEXTOCOMUM.TXT E CRIA lista de frequencia dos caracteres do texto  **********************************************************************
        List<Caracter> listaDeCarac = new ArrayList();
        
        double quantidadeDeLetras = 0;
        File file = new File(".\\TEXTOCOMUM.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        
        String linha = reader.readLine(); // le a primeira linha
        while (linha != null) {
        //System.out.printf("%s\n", linha);
        
        for(int i=0; i < linha.length(); i++){
            char letra = linha.charAt(i);
            quantidadeDeLetras = quantidadeDeLetras + 1;
            
            // Checa se letra ja foi encontrada
            boolean foiEncontrado = false;
            for(int j = 0; j < listaDeCarac.size(); j++){
               if(listaDeCarac.get(j).getCharacter() == letra){
                   listaDeCarac.get(j).setFreq(listaDeCarac.get(j).getFreq() + 1);
                   foiEncontrado = true;
                   break;
               }
            }
            if(foiEncontrado == false){
                Caracter novoCaracter = new Caracter(1, letra);
                listaDeCarac.add(novoCaracter);
            }
        }

        linha = reader.readLine(); // le o resto das linhas no while, criando a lista de caracteres e a quantidade que eles aparecem no texto
        }
        
        // Mostra no output a lista de caracteres e a quantidade que eles aparecem, e em seguida a quantidade total de caracteres no texto.
        System.out.println(" ***** Frequencia dos caracteres no texto: *****");
        for(int k=0; k < listaDeCarac.size(); k++){
            System.out.println(listaDeCarac.get(k).getCharacter() + "--" + listaDeCarac.get(k).getFreq());
        }
        System.out.println("Quantidade Total de Characteres no texto: " + quantidadeDeLetras);
        System.out.println("*********************************************************");
        
      // ************************************************************************************************ 
        
        FileWriter arq = new FileWriter(".\\FREQUENCIA.txt");
        gravaArq = new PrintWriter(arq);
        // CRIA O ARQUIVO FREQUENCIA.txt UTILIZANDO AS FREQUENCIAS DOS CARACTERES PARA RETIRAR SUAS RESPECTIVAS FREQUENCIAS RELATIVAS
        System.out.println("***** Arquivo FREQUENCIA.txt  que contem as frequencias RELATIVAS *****");
        for (int i=0; i < listaDeCarac.size(); i++){
            String character = Character.toString(listaDeCarac.get(i).getCharacter());
            double freq = (listaDeCarac.get(i).getFreq() / quantidadeDeLetras) * 100; // calculando a frequencia relativa
            if(freq < 1){
                freq = 1; // para que a frequencia nao seja dada como zero assumo a menor frequencia relativa possivel como 1
            }
        gravaArq.println(character + '-' + freq);
        System.out.println(character + '-' + freq);
        }
        gravaArq.close();
        System.out.println("*********************************************************");
    }
    
    static void criarArquivoTABELADECODIGOS() throws IOException{
        
        // Cria a lista de Nos, com suas respectivas frequencias relativas, que sera usada no algoritmo guloso de Huffman, utilizando o arquivo FREQUENCIA.txt 
        List<Caracter> listaDeCarac = new ArrayList();
        File file = new File(".\\FREQUENCIA.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String linha = reader.readLine(); // le a primeira linha
        while (linha != null) {
            if(linha.charAt(0) == '-'){
                String[] split = linha.split("-");
                listaDeCarac.add(new Caracter(Integer.parseInt(split[1]), '-'));
            } else {
                String[] split = linha.split("-");
                double freq = Double.parseDouble(split[1]);
                listaDeCarac.add(new Caracter((int) freq, split[0].charAt(0)));
            }
             
        linha = reader.readLine(); // le o resto das linhas no while
        }
        reader.close();
        
        No[] L = new No[listaDeCarac.size()];
        for (int i=0; i < listaDeCarac.size(); i++){
            
            String character = Character.toString(listaDeCarac.get(i).getCharacter());
            double freq = listaDeCarac.get(i).getFreq();
            if(freq < 1){
                freq = 1;
            }
            No novoNo = new No(character, (int) freq);
            L[i] = novoNo;
        }
        
        // ordena a lista criada usando o QuickSort
        MyQuickSort mq = new MyQuickSort();
        No[] Lordenada = mq.ordernarListaDeCaracteres(L);
        
        // ***********************************************************************************************************************************************
        
        
        // Usa o Algoritmo de Huffman para retornar a raiz para posterior busca dos codigos  *************************************************************
        No raiz = ArvoreDeHuffman(Lordenada, Lordenada.length);
        // ***********************************************************************************************************************************************
        
      // Cria o arquivo TABELADECODIGOS.txt percorrendo a arvore e buscando os codigos de cada caracter **************************************************
        FileWriter arq = new FileWriter(".\\TABELADECODIGOS.txt");
        gravaArq = new PrintWriter(arq);
        System.out.println("*** Arquivo TABELADECODIGOS.txt que contem os codigos adquiridos a partir da arvore de Huffman *****");
        for(int k=0;k<L.length;k++){
            buscarCodigo(Lordenada[k].getFreq(), raiz, "");
        }
        System.out.println("*********************************************************");
        gravaArq.close();
        // ***********************************************************************************************************************************************
        
    }
    
    static void codificarTexto() throws FileNotFoundException, IOException{
        
        
        File file = new File(".\\TEXTOCOMUM.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        
        FileWriter arq = new FileWriter(".\\CODIFICADO.txt");
        gravaArq = new PrintWriter(arq);
        
        System.out.println("***** Texto Codificado *****");
        
        // lENDO TEXTOCOMUM.txt  LETRA POR LETRA VOU CRIANDO A LINHA CODIFICADA QUE ENTAO VAI SER GRAVADA NO ARQUIVO CODIFICADO.txt
        String linha = reader.readLine(); // le a primeira linha
        while (linha != null) {
            String linhaCodificada = new String();
            int k = 0;
            while (k < linha.length()){
                char letra = linha.charAt(k);
                for(int i=0; i < tabelaDeCodigos.size(); i++){
                    if(letra == tabelaDeCodigos.get(i).getCaracter()){
                        linhaCodificada = linhaCodificada + tabelaDeCodigos.get(i).getCodigo();
                        break;
                    }
                }
                
            
            k++;
            }
            System.out.println(linhaCodificada);
            gravaArq.println(linhaCodificada);
            
            linha = reader.readLine();
        }
    
        gravaArq.close();
        System.out.println("**********************************************************");
    }
        
    public static void decodificar() throws FileNotFoundException, IOException{
        
        // LENDO TABELADECODIGOS
        File file = new File(".\\CODIFICADO.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        
        FileWriter arq = new FileWriter(".\\TEXTODECODIFICADO.txt");
        gravaArq = new PrintWriter(arq);
        
        System.out.println("***** Texto DECodificado *****");
        
        String linha = reader.readLine(); // le a primeira linha
        while (linha != null) {
            String linhaDECodificada = new String();
            int k = 0;
                                
            while (k < linha.length()){
              
                char letra = linha.charAt(k);
                String temp = new String();
                temp = temp + letra;
                boolean characterAdicionado = false;
                
                while(characterAdicionado == false){
                     
                    for(int i=0; i < tabelaDeCodigos.size(); i++){
                        if(temp.equals(tabelaDeCodigos.get(i).getCodigo())){
                            linhaDECodificada = linhaDECodificada + tabelaDeCodigos.get(i).getCaracter();
                            characterAdicionado = true;
                            break;
                        }
                    }
                    if(characterAdicionado){
                        break;
                    }
                    if(k >= linha.length()){
                        System.out.println("Decodificacao nao encontrada para o codigo: " + temp);
                        break;
                    }
                    
                    k++;
                    if(k < linha.length()){
                        letra = linha.charAt(k);
                        temp = temp + letra;
                    }
                }
               
            k++;
            }
            System.out.println(linhaDECodificada);
            gravaArq.println(linhaDECodificada);
            
            linha = reader.readLine();
        }
        System.out.println("***********************************************************");
        gravaArq.close();
        
        
    }
    
    public static void lerTabelaDeCodigos() throws FileNotFoundException, IOException{
        // LENDO TABELADECODIGOS
        File file = new File(".\\TABELADECODIGOS.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        
        String linha = reader.readLine(); // le a primeira linha
        while (linha != null) {
        
            if(linha.charAt(0) != '-'){
                String[] split = linha.split("-");
                tabelaDeCodigos.add(new Codigo(split[1], split[0].charAt(0)));
                
            } else {
               String[] split = linha.split("--");
               tabelaDeCodigos.add(new Codigo(split[1], '-'));
            }
            
        linha = reader.readLine();
        }
    }
    
    public static String buscarCodigo(double freq, No raiz, String codigo){
        if(raiz != null){
            if(raiz.getFreq() == freq){
                if(tabelaDeCodigo.size() == 0){
                    //if(raiz.getCharacter() == null){
                    //    System.out.println("aqui");
                    //}
                    System.out.println(raiz.getCharacter()+ "-" + codigo);
                    gravaArq.println(raiz.getCharacter()+ "-" + codigo);
                    
                    No novoNo = new No(raiz.getCharacter(), Integer.parseInt(codigo));
                    tabelaDeCodigo.add(novoNo);
                    
                    return codigo;
                } else {
                    boolean jaFoiAnotado = false;
                        for(int k=0; k < tabelaDeCodigo.size(); k++){
                            if(tabelaDeCodigo.get(k).getCharacter()== raiz.getCharacter()){
                                jaFoiAnotado = true;
                                break;
                            }
                        }
                        if(jaFoiAnotado == false){
                            if(raiz.getCharacter() != null){
                            System.out.println(raiz.getCharacter()+ "-" + codigo);
                            gravaArq.println(raiz.getCharacter()+ "-" + codigo);
                            tabelaDeCodigo.add(new No(raiz.getCharacter(), Integer.parseInt(codigo)));
                            return codigo;
                            }
                        }
                }
                
                
            } else {
                buscarCodigo(freq, raiz.getFilhoEsquerdo(), codigo + "0");
                buscarCodigo(freq, raiz.getFilhoDireito(), codigo + "1");
            }
        }
        return codigo;
    }
    
    public static No ArvoreDeHuffman(No[] L, int m){
        No raiz = new No();
        No z = new No();
        No x = new No();
        No y = new No();
        
        
            for(int i=1;i<m;i++){
                if (L.length > 2){
                    z = new No();
                    x = new No();
                    y = new No();
                    
                    x = L[0];
                    
                    y = L[1];
                        
                    z.setFreq((int) (x.getFreq() + y.getFreq()));
                    
                    z.setFilhoEsquerdo(x);
                    z.setFilhoDireito(y);
                    
                    L = InserirNoEmOrdem(L, z);
        
                    
        
                
                } else {
                    x = L[0];
                    y = L[1];
                raiz.setFreq((int) (x.getFreq() + y.getFreq()));
                raiz.setFilhoEsquerdo(x);
                raiz.setFilhoDireito(y);
                }    
            }
            
            return raiz;
    }
    
    public static No[] InserirNoEmOrdem(No[] L, No z){
        No AUX[] = new No[L.length - 1];
        for(int g=0; g<AUX.length; g++){
            AUX[g] = new No();
        }
        
        
        boolean jaFoiInserido = false;
        int k = 2;
       
        for(int j=0;j<AUX.length;j++){
            if(L.length > 3){
                if(k >= L.length){
                    AUX[j] = z;
                    jaFoiInserido = true;
                } else {
                        if(L[k].getFreq() < z.getFreq() || jaFoiInserido){
                            AUX[j] = L[k];
                            k++;
                        } else {
                            AUX[j] = z;
                            jaFoiInserido = true;
                        } 
                    }
            } else {
                if(L[2].getFreq() < z.getFreq()){
                    AUX[j] = L[2];
                    AUX[j+1] = z;
                    j++;
                } else {
                    AUX[j] = z;
                    AUX[j+1] = L[2];
                    j++;
                } 
            }
        }
        
        return AUX;
    }
}
