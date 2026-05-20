/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package matrices;

import java.awt.Dimension;
import java.util.Random;

/**
 *
 * @author galvez
 */
public class Matriz {
    private int[][]datos;
    private Random rnd = new Random();
    
    public Matriz(int filas, int columnas, boolean inicializarAleatorio){
        datos = new int[columnas][];
        for(int i=0; i<columnas; i++){
            datos[i] = new int[filas];
            if (inicializarAleatorio)
                for(int j=0; j<filas; j++)
                    datos[i][j] = rnd.nextInt(100);
        }
    }

    public Matriz(int[][] valores) {
        if (valores == null || valores.length == 0 || valores[0] == null) {
            throw new IllegalArgumentException("La matriz no puede ser vacía");
        }
        int filas = valores.length;
        int columnas = valores[0].length;
        datos = new int[columnas][filas];
        for (int fila = 0; fila < filas; fila++) {
            if (valores[fila] == null || valores[fila].length != columnas) {
                throw new IllegalArgumentException("Todas las filas deben tener la misma longitud");
            }
            for (int columna = 0; columna < columnas; columna++) {
                datos[columna][fila] = valores[fila][columna];
            }
        }
    }

    public Matriz(Matriz otra) {
        this(otra.getFilas(), otra.getColumnas(), false);
        for (int columna = 0; columna < getColumnas(); columna++) {
            for (int fila = 0; fila < getFilas(); fila++) {
                datos[columna][fila] = otra.datos[columna][fila];
            }
        }
    }

    public Matriz(Dimension d, boolean inicializarAleatorio){
        this(d.height, d.width, inicializarAleatorio);
    }
    
    public Dimension getDimension(){
        return new Dimension(getColumnas(), getFilas());
    }

    public int getFilas() {
        return datos.length == 0 ? 0 : datos[0].length;
    }

    public int getColumnas() {
        return datos.length;
    }

    public int getValor(int fila, int columna) {
        validarIndices(fila, columna);
        return datos[columna][fila];
    }

    public void setValor(int fila, int columna, int valor) {
        validarIndices(fila, columna);
        datos[columna][fila] = valor;
    }

    public boolean esCuadrada() {
        return getFilas() == getColumnas();
    }
    
    public static Matriz sumarDosMatrices(Matriz a, Matriz b) throws DimensionesIncompatibles { 
        if(! a.getDimension().equals(b.getDimension())) throw new DimensionesIncompatibles("La suma de matrices requiere matrices de las mismas dimensiones");        
        Matriz matrizResultante = new Matriz(a.getFilas(), a.getColumnas(), false);
        for (int columna = 0; columna < a.getColumnas(); columna++) {
            for (int fila = 0; fila < a.getFilas(); fila++) {
                matrizResultante.datos[columna][fila] = a.datos[columna][fila] + b.datos[columna][fila];
            } 
        } 
        return matrizResultante; 
    } 

    public static Matriz restarDosMatrices(Matriz a, Matriz b) throws DimensionesIncompatibles {
        if (!a.getDimension().equals(b.getDimension())) {
            throw new DimensionesIncompatibles("La resta de matrices requiere matrices de las mismas dimensiones");
        }
        Matriz matrizResultante = new Matriz(a.getFilas(), a.getColumnas(), false);
        for (int columna = 0; columna < a.getColumnas(); columna++) {
            for (int fila = 0; fila < a.getFilas(); fila++) {
                matrizResultante.datos[columna][fila] = a.datos[columna][fila] - b.datos[columna][fila];
            }
        }
        return matrizResultante;
    }

    public static Matriz multiplicarDosMatrices(Matriz a, Matriz b) throws DimensionesIncompatibles {
        if (a.getColumnas() != b.getFilas()) {
            throw new DimensionesIncompatibles("La multiplicación de matrices requiere que el número de columnas de A coincida con el número de filas de B");
        }
        Matriz resultado = new Matriz(a.getFilas(), b.getColumnas(), false);
        for (int columna = 0; columna < resultado.getColumnas(); columna++) {
            for (int fila = 0; fila < resultado.getFilas(); fila++) {
                int acumulado = 0;
                for (int k = 0; k < a.getColumnas(); k++) {
                    acumulado += a.datos[k][fila] * b.datos[columna][k];
                }
                resultado.datos[columna][fila] = acumulado;
            }
        }
        return resultado;
    }

    public static Matriz multiplicarPorEscalar(Matriz matriz, int escalar) {
        Matriz resultado = new Matriz(matriz.getFilas(), matriz.getColumnas(), false);
        for (int columna = 0; columna < matriz.getColumnas(); columna++) {
            for (int fila = 0; fila < matriz.getFilas(); fila++) {
                resultado.datos[columna][fila] = matriz.datos[columna][fila] * escalar;
            }
        }
        return resultado;
    }

    public static Matriz calcularTranspuesta(Matriz matriz) {
        int filas = matriz.getFilas();
        int columnas = matriz.getColumnas();
        Matriz transpuesta = new Matriz(columnas, filas, false);

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                transpuesta.datos[i][j] = matriz.datos[j][i];
            }
        }
        return transpuesta;
    }

    private void validarIndices(int fila, int columna) {
        if (fila < 0 || fila >= getFilas() || columna < 0 || columna >= getColumnas()) {
            throw new IndexOutOfBoundsException("Índice de matriz fuera de rango");
        }
    }

    @Override
    public String toString(){
        StringBuilder ret = new StringBuilder();
        ret.append("[\n");
        for (int i = 0; i < getColumnas(); i++) {
            ret.append("(");
            for (int j = 0; j < getFilas(); j++) {  
                ret.append(String.format("%3d", datos[i][j])); 
                if (j != getFilas() - 1) ret.append(", ");
            } 
            ret.append(")");
            if (i != getColumnas() - 1) ret.append(",");
            ret.append("\n");
        } 
        ret.append("]\n");
        return ret.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Matriz)) {
            return false;
        }
        Matriz otra = (Matriz) obj;
        if (!getDimension().equals(otra.getDimension())) {
            return false;
        }
        for (int columna = 0; columna < getColumnas(); columna++) {
            for (int fila = 0; fila < getFilas(); fila++) {
                if (datos[columna][fila] != otra.datos[columna][fila]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + getFilas();
        hash = 31 * hash + getColumnas();
        for (int columna = 0; columna < getColumnas(); columna++) {
            for (int fila = 0; fila < getFilas(); fila++) {
                hash = 31 * hash + datos[columna][fila];
            }
        }
        return hash;
    }
}
