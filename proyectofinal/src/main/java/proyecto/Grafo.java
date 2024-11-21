package proyecto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.JOptionPane;

public class Grafo {
    private int[][] matrizAdyacencia;
    private int numNodos;
    private boolean dirigido;
    private int[] predecesores; // Arreglo de predecesores para dijkstra

    public Grafo(int tamano, boolean dirigido) {
        this.matrizAdyacencia = new int[tamano][tamano];
        this.numNodos = tamano;
        this.dirigido = dirigido;
    }

    public void agregarArista(int origen, int destino, int peso) {
        matrizAdyacencia[origen][destino] = peso;
        if (!dirigido) {
            matrizAdyacencia[destino][origen] = peso;
        }
    }

    public void eliminarArista(int origen, int destino) {
        matrizAdyacencia[origen][destino] = 0;
        if (!dirigido) {
            matrizAdyacencia[destino][origen] = 0;
        }
    }

    public void mostrarMatrizAdyacencia() {
        for (int i = 0; i < numNodos; i++) {
            for (int j = 0; j < numNodos; j++) {
                System.out.print(matrizAdyacencia[i][j] + " ");
            }
            System.out.println();
        }
    }

    public int[] dijkstra(int inicio) {
        // Array para almacenar las distancias mínimas desde el nodo de inicio a cada nodo
        int[] distancias = new int[numNodos];
        // Array para almacenar el predecesor de cada nodo en el camino más corto
        predecesores = new int[numNodos];
        // Array para marcar los nodos que ya han sido visitados
        boolean[] visitados = new boolean[numNodos];
    
        // Inicializa todas las distancias con el valor máximo posible
        Arrays.fill(distancias, Integer.MAX_VALUE);
        // Inicializa todos los predecesores con -1 (indicando que no hay predecesor aún)
        Arrays.fill(predecesores, -1);
        // La distancia desde el nodo de inicio hasta sí mismo es 0
        distancias[inicio] = 0;
    
        // Repite para cada nodo en el grafo
        for (int i = 0; i < numNodos; i++) {
            // Encuentra el nodo con la distancia mínima que no ha sido visitado aún
            int nodoMin = minDistancia(distancias, visitados);
            // Marca el nodo como visitado
            visitados[nodoMin] = true;
    
            // Actualiza las distancias de los nodos adyacentes
            for (int j = 0; j < numNodos; j++) {
                // Si el nodo no ha sido visitado, hay un camino desde el nodo mínimo, y se encuentra una distancia más corta
                if (!visitados[j] && matrizAdyacencia[nodoMin][j] != 0 && distancias[nodoMin] != Integer.MAX_VALUE
                        && distancias[nodoMin] + matrizAdyacencia[nodoMin][j] < distancias[j]) {
                    // Actualiza la distancia al nodo j
                    distancias[j] = distancias[nodoMin] + matrizAdyacencia[nodoMin][j];
                    // Actualiza el predecesor del nodo j
                    predecesores[j] = nodoMin;
                }
            }
        }    
        // Devuelve el array de distancias mínimas desde el nodo de inicio a cada nodo
        return distancias;
    }
    

    private int minDistancia(int[] distancias, boolean[] visitados) {
        int min = Integer.MAX_VALUE, minIndex = -1;

        for (int i = 0; i < numNodos; i++) {
            if (!visitados[i] && distancias[i] <= min) {
                min = distancias[i];
                minIndex = i;
            }
        }

        return minIndex;
    }

    public int[][] floydWarshall() {
        // Inicializa la matriz de distancias con el mismo tamaño que la matriz de adyacencia.
        int[][] dist = new int[numNodos][numNodos];
    
        // Recorre cada par de nodos para inicializar la matriz de distancias.
        for (int i = 0; i < numNodos; i++) {
            for (int j = 0; j < numNodos; j++) {
                if (i == j) {
                    // La distancia desde un nodo a sí mismo es 0.
                    dist[i][j] = 0;
                } else if (matrizAdyacencia[i][j] != 0) {
                    // Si hay una arista directa entre i y j, se usa su peso como distancia.
                    dist[i][j] = matrizAdyacencia[i][j];
                } else {
                    // Si no hay una arista directa, se establece la distancia como infinita.
                    dist[i][j] = Integer.MAX_VALUE;
                }
            }
        }
    
        // Aplica el algoritmo de Floyd-Warshall.
        // Recorre cada nodo intermedio k.
        for (int k = 0; k < numNodos; k++) {
            // Recorre cada nodo i.
            for (int i = 0; i < numNodos; i++) {
                // Recorre cada nodo j.
                for (int j = 0; j < numNodos; j++) {
                    // Si el camino a través de k es más corto que el camino directo de i a j, actualiza dist[i][j].
                    if (dist[i][k] != Integer.MAX_VALUE && dist[k][j] != Integer.MAX_VALUE &&
                            dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }
    
        // Retorna la matriz de distancias mínimas.
        return dist;
    }
    

    public void cargarDesdeArchivo(String nombreArchivo) {
        try (BufferedReader lector = new BufferedReader(new FileReader(nombreArchivo))) {
            String tipoGrafo = lector.readLine().trim();
            dirigido = tipoGrafo.equalsIgnoreCase("dirigido");
    
            // Primero, determinar el número de nodos
            int maxNodo = -1;
            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] partes = linea.split(" ");
                int origen = Integer.parseInt(partes[0]);
                int destino = Integer.parseInt(partes[1]);
                // Determinar el nodo máximo
                maxNodo = Math.max(maxNodo, Math.max(origen, destino));
            }
    
            // Inicializar la matriz de adyacencia con el número correcto de nodos
            numNodos = maxNodo + 1;
            matrizAdyacencia = new int[numNodos][numNodos];
    
            // Volver a leer el archivo para cargar las aristas
            try (BufferedReader lector2 = new BufferedReader(new FileReader(nombreArchivo))) {
                lector2.readLine(); // saltar la primera línea del tipo de grafo
    
                while ((linea = lector2.readLine()) != null) {
                    String[] partes = linea.split(" ");
                    int origen = Integer.parseInt(partes[0]);
                    int destino = Integer.parseInt(partes[1]);
                    int peso = Integer.parseInt(partes[2]);
    
                    agregarArista(origen, destino, peso);
                }
            }
    
            JOptionPane.showMessageDialog(null, "Grafo cargado desde archivo.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar el grafo desde el archivo.");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Formato de archivo incorrecto.");
            e.printStackTrace();
        }
    }
    

    public void guardarResultadosEnArchivo(String nombreArchivo, int[] distanciasDijkstra, int[][] distanciasFloydWarshall, int origenDijkstra) {
        try (FileWriter escritor = new FileWriter(nombreArchivo)) {
            // Guardar resultados de Dijkstra
            escritor.write("Resultados de Dijkstra desde el nodo " + origenDijkstra + ":\n");
            for (int i = 0; i < distanciasDijkstra.length; i++) {
                String linea = "Nodo " + origenDijkstra + " a Nodo " + i + " : " + distanciasDijkstra[i];
                escritor.write(linea + "\n");
            }
            escritor.write("\n"); // Línea en blanco para separación
    
            // Guardar resultados de Floyd-Warshall
            escritor.write("Matriz de distancias mínimas de Floyd-Warshall:\n");
            for (int i = 0; i < distanciasFloydWarshall.length; i++) {
                for (int j = 0; j < distanciasFloydWarshall[i].length; j++) {
                    String valor = (distanciasFloydWarshall[i][j] == Integer.MAX_VALUE) ? "INF " : distanciasFloydWarshall[i][j] + " ";
                    escritor.write(valor);
                }
                escritor.write("\n");
            }
            escritor.write("\n"); // Línea en blanco para separación
    
            JOptionPane.showMessageDialog(null, "Resultados guardados en el archivo.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar los resultados en el archivo.");
            e.printStackTrace();
        }
    }
    

    public int[][] getMatrizAdyacencia() {
        return matrizAdyacencia;
    }

    public void setMatrizAdyacencia(int[][] matrizAdyacencia) {
        this.matrizAdyacencia = matrizAdyacencia;
    }

    public int getNumNodos() {
        return numNodos;
    }

    public void setNumNodos(int numNodos) {
        this.numNodos = numNodos;
    }

    public boolean isDirigido() {
        return dirigido;
    }

    public void setDirigido(boolean dirigido) {
        this.dirigido = dirigido;
    }

    public int[] getPredecesores() {
        return predecesores;
    }
}
