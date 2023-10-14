//QUIMISELL
package Lexi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MiAnalizadorLexico extends JFrame {
    private JTextArea inputTextArea;
    private JTable tokenTable;
    private JTable classTable;
    private DefaultTableModel tokenTableModel;

    public MiAnalizadorLexico() {
        setTitle("Mi Analizador Léxico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new BorderLayout());
        JLabel inputLabel = new JLabel("Input:");
        inputTextArea = new JTextArea(10, 40);
        inputTextArea.setLineWrap(true);
        JScrollPane inputScrollPane = new JScrollPane(inputTextArea);
        inputPanel.add(inputLabel, BorderLayout.NORTH);
        inputPanel.add(inputScrollPane, BorderLayout.CENTER);

        JPanel tokenPanel = new JPanel(new BorderLayout());
        JLabel tokenLabel = new JLabel("Tokens:");
        tokenTableModel = new DefaultTableModel(new Object[][]{}, new String[]{"Lexema", "Pattern", "Patrón"});
        tokenTable = new JTable(tokenTableModel);
        JScrollPane tokenScrollPane = new JScrollPane(tokenTable);
        tokenPanel.add(tokenLabel, BorderLayout.NORTH);
        tokenPanel.add(tokenScrollPane, BorderLayout.CENTER);

        JPanel classPanel = new JPanel(new BorderLayout());
        JLabel classLabel = new JLabel("Información de Clase");
        DefaultTableModel classTableModel = new DefaultTableModel(new Object[][]{}, new String[]{"Nombre de Clase", "Accesibilidad", "Tipo de Dato"});
        classTable = new JTable(classTableModel);
        JScrollPane classScrollPane = new JScrollPane(classTable);
        classPanel.add(classLabel, BorderLayout.NORTH);
        classPanel.add(classScrollPane, BorderLayout.CENTER);

        JButton openFileButton = new JButton("Abrir Archivo");
        openFileButton.addActionListener(e -> openFile());

        JButton analyzeButton = new JButton("Analizar");
        analyzeButton.addActionListener(e -> analyze());

        JButton clearButton = new JButton("Limpiar");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(openFileButton);
        buttonPanel.add(analyzeButton);
        buttonPanel.add(clearButton);

        add(inputPanel, BorderLayout.NORTH);
        add(tokenPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(classPanel, BorderLayout.EAST);

        pack();
               setVisible(true);
    }

    private void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFile = fileChooser.getSelectedFile();
            try {
                java.util.Scanner scanner = new java.util.Scanner(selectedFile);
                StringBuilder fileContent = new StringBuilder();
                while (scanner.hasNextLine()) {
                    fileContent.append(scanner.nextLine()).append("\n");
                }
                inputTextArea.setText(fileContent.toString());
            } catch (java.io.IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al abrir el archivo", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void analyze() {
        String input = inputTextArea.getText();
        DefaultTableModel classModel = (DefaultTableModel) classTable.getModel();
        classModel.setRowCount(0);

        String classPattern = "(public|private)\\s+class\\s+([a-zA-Z][a-zA-Z0-9_]*)\\s*\\{";
        Pattern pattern = Pattern.compile(classPattern);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            String accessibility = matcher.group(1);
            String className = matcher.group(2);
            classModel.addRow(new Object[]{className, accessibility, "Class"});
        }

        tokenTableModel.setRowCount(0);

        List<TokenPattern> tokenPatterns = new ArrayList<>();
        tokenPatterns.add(new TokenPattern("Identifier", "([a-zA-Z][a-zA-Z0-9_]*)", "Nombre de variable o identificador", "Identificador"));
        tokenPatterns.add(new TokenPattern("Access Modifier", "(public|private)", "Modificador de acceso", "Modificador de acceso"));
        tokenPatterns.add(new TokenPattern("Data Type", "(int|text|decimal)", "Tipo de dato", "Tipo de dato"));
        tokenPatterns.add(new TokenPattern("Number", "\\d+", "Número entero", "Número entero"));
        tokenPatterns.add(new TokenPattern("Decimal Number", "\\d+\\.\\d+", "Número decimal", "Número decimal"));
        tokenPatterns.add(new TokenPattern("String", "\"[^\"]*\"", "Cadena de texto", "Cadena de texto"));
        tokenPatterns.add(new TokenPattern("Operator", "[+\\-*/]", "Operador aritmético", "Operador aritmético"));
        tokenPatterns.add(new TokenPattern("Left Brace", "\\{", "Llave de Apertura", "Llave de Apertura"));
        tokenPatterns.add(new TokenPattern("Right Brace", "\\}", "Llave de Cierre", "Llave de Cierre"));
        tokenPatterns.add(new TokenPattern("Left Parenthesis", "\\(", "Paréntesis de Apertura", "Paréntesis de Apertura"));
        tokenPatterns.add(new TokenPattern("Right Parenthesis", "\\)", "Paréntesis de Cierre", "Paréntesis de Cierre"));
 

        for (TokenPattern tokenPattern : tokenPatterns) {
            Pattern tokenRegexPattern = Pattern.compile(tokenPattern.getRegex());
            Matcher tokenMatcher = tokenRegexPattern.matcher(input);

            while (tokenMatcher.find()) {
                String lexeme = tokenMatcher.group();
                String patternName = tokenPattern.getType();
                String patron = tokenPattern.getPatron();

                if (!patternName.equals("Whitespace")) {
                    tokenTableModel.addRow(new Object[]{lexeme, patternName, patron});
                }
            }
        }
    }

    private void clear() {
        inputTextArea.setText("");
        DefaultTableModel classModel = (DefaultTableModel) classTable.getModel();
        classModel.setRowCount(0);
        tokenTableModel.setRowCount(0);
    }

  
    private static class TokenPattern {
        private String type;
        private String regex;
        private String attribute;
        private String patron;

        public TokenPattern(String type, String regex, String attribute, String patron) {
            this.type = type;
            this.regex = regex;
            this.attribute = attribute;
            this.patron = patron;
        }

        public String getType() {
            return type;
        }

        public String getRegex() {
            return regex;
        }

        public String getAttribute() {
            return attribute;
        }

        public String getPatron() {
            return patron;
        }
    }
}
