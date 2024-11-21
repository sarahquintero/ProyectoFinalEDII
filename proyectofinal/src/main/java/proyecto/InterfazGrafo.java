package proyecto;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.DefaultTableModel;

public class InterfazGrafo extends JFrame {
    private Grafo grafo;
    private JComboBox<String> tipoGrafoComboBox;
    private JTextField nodoOrigenTextField, nodoDestinoTextField, pesoTextField;
    private JTextArea resultadosTextArea;
    private JTable tablaGrafo;
    private JScrollPane scrollPaneTabla;
    private JLabel tituloTabla;

    public InterfazGrafo() {

        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
            UIManager.put("control", new Color(43, 43, 43));
            UIManager.put("info", new Color(43, 43, 43));
            UIManager.put("nimbusBase", new Color(18, 30, 49));
            UIManager.put("nimbusAlertYellow", new Color(248, 187, 0));
            UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
            UIManager.put("nimbusFocus", new Color(115, 164, 209));
            UIManager.put("nimbusGreen", new Color(176, 179, 50));
            UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
            UIManager.put("nimbusLightBackground", new Color(18, 30, 49));
            UIManager.put("nimbusOrange", new Color(191, 98, 4));
            UIManager.put("nimbusRed", new Color(169, 46, 34));
            UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
            UIManager.put("nimbusSelectionBackground", new Color(104, 93, 156));
            UIManager.put("text", new Color(230, 230, 230));
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();

        }
        // Pedir al usuario el tamaño del grafo
        String tamanoStr = JOptionPane.showInputDialog(this, "Ingrese el tamaño del grafo (número de nodos):",
                "Número de Nodos", JOptionPane.QUESTION_MESSAGE);
        int tamano = Integer.parseInt(tamanoStr);

        // Pedir al usuario si el grafo es dirigido o no
        int respuesta = JOptionPane.showConfirmDialog(this, "¿El grafo es dirigido?", "Tipo de Grafo",
                JOptionPane.YES_NO_OPTION);
        boolean dirigido = (respuesta == JOptionPane.YES_OPTION);

        // Inicializar el grafo con los valores dados por el usuario
        grafo = new Grafo(tamano, dirigido);

        setTitle("Administrador de Grafos");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new FlowLayout());

        tipoGrafoComboBox = new JComboBox<>(new String[] { "Dirigido", "No Dirigido" });
        nodoOrigenTextField = new JTextField(3);
        nodoDestinoTextField = new JTextField(3);
        pesoTextField = new JTextField(3);

        JButton agregarAristaButton = new JButton("Agregar Arista");
        JButton cargarArchivoButton = new JButton("Cargar Archivo");
        JButton guardarResultadosButton = new JButton("Guardar Resultados");
        JButton ejecutarDijkstraButton = new JButton("Ejecutar Dijkstra");
        JButton ejecutarFloydWarshallButton = new JButton("Ejecutar Floyd-Warshall");

        resultadosTextArea = new JTextArea(15, 50);
        resultadosTextArea.setEditable(false);
        resultadosTextArea.setBackground(new Color(43, 43, 43));
        resultadosTextArea.setForeground(new Color(230, 230, 230));
        JScrollPane scrollPaneResultados = new JScrollPane(resultadosTextArea);

        // panelSuperior.add(new JLabel("Tipo de Grafo:"));
        // panelSuperior.add(tipoGrafoComboBox);
        panelSuperior.add(new JLabel("Nodo Origen:"));
        panelSuperior.add(nodoOrigenTextField);
        panelSuperior.add(new JLabel("Nodo Destino:"));
        panelSuperior.add(nodoDestinoTextField);
        panelSuperior.add(new JLabel("Peso:"));
        panelSuperior.add(pesoTextField);
        panelSuperior.add(agregarAristaButton);
        panelSuperior.add(cargarArchivoButton);
        panelSuperior.add(guardarResultadosButton);
        panelSuperior.add(ejecutarDijkstraButton);
        panelSuperior.add(ejecutarFloydWarshallButton);

        tituloTabla = new JLabel("Matriz de Adyacencia");
        tituloTabla.setHorizontalAlignment(JLabel.CENTER);

        tablaGrafo = new JTable();
        scrollPaneTabla = new JScrollPane(tablaGrafo);
        JPanel panelTabla = new JPanel();
        panelTabla.setLayout(new BorderLayout());
        panelTabla.add(tituloTabla, BorderLayout.NORTH);
        panelTabla.add(scrollPaneTabla, BorderLayout.CENTER);

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollPaneResultados, BorderLayout.CENTER);
        add(panelTabla, BorderLayout.SOUTH);

        agregarAristaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarArista();
            }
        });

        cargarArchivoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarArchivo();
            }
        });

        guardarResultadosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarContenidoResultados();
            }
        });

        ejecutarDijkstraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutarDijkstra();
            }
        });

        ejecutarFloydWarshallButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutarFloydWarshall();
            }
        });

        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void agregarArista() {
        try {
            int origen = Integer.parseInt(nodoOrigenTextField.getText().trim());
            int destino = Integer.parseInt(nodoDestinoTextField.getText().trim());
            int peso = Integer.parseInt(pesoTextField.getText().trim());

            if (origen < 0 || destino < 0 || peso < 0) {
                throw new IllegalArgumentException("Los valores no pueden ser negativos");
            }

            grafo.agregarArista(origen, destino, peso);
            resultadosTextArea.append("Arista agregada: " + origen + " -> " + destino + " con peso " + peso + "\n");
            actualizarTablaGrafo();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingresa valores numéricos válidos.", "Error de Formato",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Valor", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ejecutarFloydWarshall() {
        int[][] distancias = grafo.floydWarshall(); // Ejecutar el algoritmo de Floyd-Warshall
        resultadosTextArea.append("Resultados de Floyd-Warshall:\n"); // Título en el JTextArea
    
        // Recorrer y mostrar los resultados en el JTextArea
        for (int i = 0; i < distancias.length; i++) {
            for (int j = 0; j < distancias[i].length; j++) {
                if (distancias[i][j] == Integer.MAX_VALUE) {
                    resultadosTextArea.append(String.format("%7s", "INF")); // Mostrar "INF" para distancias infinitas
                } else {
                    resultadosTextArea.append(String.format("%7d", distancias[i][j])); // Mostrar la distancia
                }
            }
            resultadosTextArea.append("\n"); // Nueva línea después de cada fila
        }
        
        resultadosTextArea.append("\n"); // Línea en blanco para separar los resultados de Floyd-Warshall de otros contenidos
    
    }
    

    private void ejecutarDijkstra() {
        int nodoOrigen = Integer.parseInt(JOptionPane.showInputDialog(this, "Ingrese el nodo origen para Dijkstra:"));
        int[] distanciasDijkstra = grafo.dijkstra(nodoOrigen);
        int[] predecesores = grafo.getPredecesores();

        resultadosTextArea.append("Resultados de Dijkstra desde el nodo " + nodoOrigen + ":\n");
        for (int i = 0; i < distanciasDijkstra.length; i++) {
            resultadosTextArea.append("Nodo " + nodoOrigen + " a Nodo " + i + " : " + distanciasDijkstra[i] + "\n");
        }

        resultadosTextArea.append("\nRecorridos más cortos:\n");
        for (int i = 0; i < distanciasDijkstra.length; i++) {
            if (i != nodoOrigen) {
                resultadosTextArea.append("Nodo " + nodoOrigen + " a Nodo " + i + " : ");
                imprimirRecorrido(nodoOrigen, i, predecesores);
                resultadosTextArea.append("\n");
            }
        }
    }

    private void cargarArchivo() {
        String nombreArchivo = JOptionPane.showInputDialog(this, "Ingrese el nombre del archivo a cargar:");
        grafo = new Grafo(10, tipoGrafoComboBox.getSelectedItem().equals("Dirigido"));
        grafo.cargarDesdeArchivo(nombreArchivo);
        resultadosTextArea.append("Grafo cargado desde archivo: " + nombreArchivo + "\n");
        actualizarTablaGrafo();
    }

    private void guardarContenidoResultados() {
        String nombreArchivo = JOptionPane.showInputDialog(this,
                "Ingrese el nombre del archivo para guardar los resultados:");
        if (nombreArchivo != null && !nombreArchivo.trim().isEmpty()) {
            try (FileWriter escritor = new FileWriter(nombreArchivo)) {
                resultadosTextArea.write(escritor);
                JOptionPane.showMessageDialog(this, "Resultados guardados en el archivo: " + nombreArchivo);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar los resultados en el archivo.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // Método para imprimir el recorrido desde el nodo origen hasta el destino
    // usando los predecesores
    private void imprimirRecorrido(int origen, int destino, int[] predecesores) {
        if (origen == destino) {
            resultadosTextArea.append(String.valueOf(origen));
        } else if (predecesores[destino] == -1) {
            resultadosTextArea.append("No hay camino");
        } else {
            imprimirRecorrido(origen, predecesores[destino], predecesores);
            resultadosTextArea.append(" -> " + destino);
        }
    }

    private void actualizarTablaGrafo() {
        String[] columnas = new String[grafo.getNumNodos()];
        for (int i = 0; i < grafo.getNumNodos(); i++) {
            columnas[i] = String.valueOf(i);
        }

        Object[][] data = new Object[grafo.getNumNodos()][grafo.getNumNodos()];
        for (int i = 0; i < grafo.getNumNodos(); i++) {
            for (int j = 0; j < grafo.getNumNodos(); j++) {
                data[i][j] = grafo.getMatrizAdyacencia()[i][j];
            }
        }

        DefaultTableModel modelo = new DefaultTableModel(data, columnas);
        tablaGrafo.setModel(modelo);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InterfazGrafo().setVisible(true);
            }
        });
    }
}
