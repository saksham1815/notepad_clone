import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class AdvancedNotepad extends JFrame {

    private JTextArea textArea;
    private JFileChooser fileChooser;

    public AdvancedNotepad() {
        setTitle("Advanced Notepad");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        fileChooser = new JFileChooser();

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");

        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });

        saveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        JMenu formatMenu = new JMenu("Format");
        JMenuItem fontItem = new JMenuItem("Font");
        JMenuItem fontSizeItem = new JMenuItem("Font Size");
        JMenuItem boldItem = new JMenuItem("Bold");
        JMenuItem italicItem = new JMenuItem("Italic");

        fontItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectFont();
            }
        });

        fontSizeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectFontSize();
            }
        });

        boldItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setBold();
            }
        });

        italicItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setItalic();
            }
        });

        formatMenu.add(fontItem);
        formatMenu.add(fontSizeItem);
        formatMenu.addSeparator();
        formatMenu.add(boldItem);
        formatMenu.add(italicItem);
        menuBar.add(formatMenu);

        setJMenuBar(menuBar);
    }

    private void openFile() {
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                textArea.setText("");
                while ((line = reader.readLine()) != null) {
                    textArea.append(line + "\n");
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveFile() {
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write(textArea.getText());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void selectFont() {
        Font currentFont = textArea.getFont();
        Font font = FontChooser.showDialog(this, "Choose Font", currentFont);
        if (font != null) {
            textArea.setFont(font);
        }
    }

    private void selectFontSize() {
        String sizeStr = JOptionPane.showInputDialog(this, "Enter font size:");
        if (sizeStr != null && !sizeStr.isEmpty()) {
            int size = Integer.parseInt(sizeStr);
            Font currentFont = textArea.getFont();
            textArea.setFont(currentFont.deriveFont((float) size));
        }
    }

    private void setBold() {
        Font currentFont = textArea.getFont();
        int style = currentFont.getStyle();
        if ((style & Font.BOLD) == Font.BOLD) {
            textArea.setFont(currentFont.deriveFont(style & ~Font.BOLD));
        } else {
            textArea.setFont(currentFont.deriveFont(style | Font.BOLD));
        }
    }

    private void setItalic() {
        Font currentFont = textArea.getFont();
        int style = currentFont.getStyle();
        if ((style & Font.ITALIC) == Font.ITALIC) {
            textArea.setFont(currentFont.deriveFont(style & ~Font.ITALIC));
        } else {
            textArea.setFont(currentFont.deriveFont(style | Font.ITALIC));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AdvancedNotepad().setVisible(true);
            }
        });
    }
}

class FontChooser extends JDialog {
    private Font selectedFont;

    public FontChooser(Frame parent, Font defaultFont) {
        super(parent, "Choose Font", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        selectedFont = defaultFont;

        final JList<String> fontList = new JList<>(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        fontList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fontList.setSelectedValue(defaultFont.getFamily(), true);

        JScrollPane fontScrollPane = new JScrollPane(fontList);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedFontName = fontList.getSelectedValue();
                if (selectedFontName != null) {
                    selectedFont = new Font(selectedFontName, selectedFont.getStyle(), selectedFont.getSize());
                }
                setVisible(false);
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(fontScrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
    }

    public Font getSelectedFont() {
        return selectedFont;
    }

    public static Font showDialog(Frame parent, String title, Font defaultFont) {
        FontChooser dialog = new FontChooser(parent, defaultFont);
        dialog.setTitle(title);
        dialog.setVisible(true);
        return dialog.getSelectedFont();
    }
}
