package TextEditor;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.InsetsUIResource;

public class Ehh implements ActionListener, DocumentListener, WindowListener, CaretListener{

    private static String applicationName = "Ehhditor-By Sujal Uprety";

    private JFrame frame;
    private MyTextPane textPane;
    private JScrollPane scrollPane;

    private MyTextPaneLineNumbers textPaneLineNumbers;
    private SimpleAttributeSet textPaneLineNumbersAttributeSet;

    private JMenuBar menuBar;
    private JMenu file;
    private JMenu view;

    private JMenuItem New;
    private JMenuItem open;
    private JMenuItem save;
    private JMenuItem saveAs;
    private JMenuItem exit;
    
    private MyThemesMenu themes;

    private File currentFile;

    public Ehh() {
        //initializing all the components
        frame = new JFrame(applicationName);
        textPane = new MyTextPane();
        scrollPane = new JScrollPane();

        textPaneLineNumbers = new MyTextPaneLineNumbers();
        textPaneLineNumbersAttributeSet = new SimpleAttributeSet();
        
        menuBar = new JMenuBar();
        file = new JMenu("File");
        view = new JMenu("View");
        
        New = new JMenuItem("New");
        open = new JMenuItem("Open");
        save = new JMenuItem("Save");
        saveAs = new JMenuItem("Save As");
        exit = new JMenuItem("Exit");

        themes = new MyThemesMenu(frame);


        //adding components to frame
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(scrollPane);
        
        //setting up frame
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        //setting up text pane
        textPane.setVisible(true);
        textPane.setEditable(true);
        textPane.addCaretListener(this);

        /*
         * Setting up the text pane line numbers attributes
         * As the text pane line numbers and text pane were not synced so 
         * adding line spacing by 0.1249f(measured by my finger) so that
         * it will sync with the text pane and 
         * now the line numbers vertical position will not go up
        */
        StyleConstants.setLineSpacing(textPaneLineNumbersAttributeSet, 0.1249f);
        StyleConstants.setForeground(textPaneLineNumbersAttributeSet, Color.gray);
        

        //setting up text pane line numbers
        textPaneLineNumbers.setText(" 1 ");
        textPaneLineNumbers.setVisible(true);
        textPaneLineNumbers.setEditable(false);
        textPaneLineNumbers.setBackground(Color.DARK_GRAY);
        textPaneLineNumbers.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        textPaneLineNumbers.setParagraphAttributes(textPaneLineNumbersAttributeSet, true);
        textPaneLineNumbers.setMargin(new InsetsUIResource(1, 0, 2, 2));
        

        //setting up scroll pane and adding the line numbers count as row header
        scrollPane.setVisible(true);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setViewportView(textPane);
        scrollPane.setRowHeaderView(textPaneLineNumbers);
        

        //setting up menu bar and it's items
        menuBar.setVisible(true);
        menuBar.add(file);
        menuBar.add(view);
        
        New.setVisible(true);
        open.setVisible(true);
        save.setVisible(true);
        saveAs.setVisible(true);
        exit.setVisible(true);
        
        file.setVisible(true);
        file.add(New);
        file.add(open);
        file.add(save);
        file.add(saveAs);
        file.add(exit);

        view.setVisible(true);
        view.add(themes);

        //setting up mnemonics of menu items
        file.setMnemonic(KeyEvent.VK_F);
        New.setMnemonic(KeyEvent.VK_N);
        open.setMnemonic(KeyEvent.VK_O);
        save.setMnemonic(KeyEvent.VK_S);
        saveAs.setMnemonic(KeyEvent.VK_A);
        exit.setMnemonic(KeyEvent.VK_X);

        view.setMnemonic(KeyEvent.VK_V);
        

        //setting up hot keys for menu items
        New.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,KeyEvent.CTRL_DOWN_MASK));
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
        

        //adding event listeners to the required components
        frame.addWindowListener(this);
        
        textPane.getDocument().addDocumentListener(this);
        
        New.addActionListener(this);
        open.addActionListener(this);
        save.addActionListener(this);
        saveAs.addActionListener(this);
        exit.addActionListener(this);
        
    }
    
    //Overriden from class ActionListener
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == New) {
            CreateNewFile();
        }
        else if(e.getSource() == open) {
            OpenFile();
        }
        else if(e.getSource() == save){ 
            SaveFile();
        }       
        else if(e.getSource() == saveAs){
            SaveFileAs();
        }
        else if(e.getSource() == exit){
            ExitApplication();
        }
    }

    
    //Overriden from class DocumentListener
    @Override
    public void insertUpdate(DocumentEvent e) {
        OnDocumentUpdate();
    }
  
    //Overriden from class DocumentListener 
    @Override
    public void removeUpdate(DocumentEvent e) {
        OnDocumentUpdate();  
    }

    //Overriden from class DocumentListener
    @Override
    public void changedUpdate(DocumentEvent e) {
        OnDocumentUpdate();
    }
    
    /**
     * Invokes after any changes in documents
    */
    private void OnDocumentUpdate() {
        textPaneLineNumbers.setText(GetNumberOfLinesOfTextPane());
        textPaneLineNumbers.ChangeCurrentLineColor(textPane.GetLineAtCaret());
        if(currentFile == null) {
            frame.setTitle((textPane.getText().equals("") ? "" : "*") + applicationName);       
            return;
        }
        UpdateFrameTitle(currentFile.getName(), IsFileSaved());

    }

    
    //Overriden from class Caret Listener
    @Override
    public void caretUpdate(CaretEvent c) {
        textPaneLineNumbers.ChangeCurrentLineColor(textPane.GetLineAtCaret());
    }
    
    
    //Overriden from class WindowListener
    @Override
    public void windowActivated(WindowEvent w) {}
    
    //Overriden from class WindowListener
    @Override
    public void windowOpened(WindowEvent w) {}
    
    //Overriden from class WindowListener
    @Override
    public void windowIconified(WindowEvent w) {}
    
    //Overriden from class WindowListener
    @Override
    public void windowDeiconified(WindowEvent w) {}
    
    //Overriden from class WindowListener
    @Override
    public void windowClosing(WindowEvent w) {
        ExitApplication();
    }
    
    //Overriden from class WindowListener
    @Override
    public void windowDeactivated(WindowEvent w) {}
    
    //Overriden from class WindowListener
    @Override
    public void windowClosed(WindowEvent w) {}

    
    /**
     * Invoked to create New File when 
     * New File option is selected from Menu List "File".
     * <p>
     * First checks if the current file is saved or not.
     * If not saved then save file warning pops up.
    */
    private void CreateNewFile() {
        if(!SaveFileWarningDialogBox()) return;
        
        currentFile = null;
        textPane.setText("");
        frame.setTitle(applicationName);
    }
    

    /**
     * Invoked to open the existing file when
     * Open File is selected from Menu List "File".
     * <p>
     * First checks if the current file is saved or not.
     * If not saved then save file warning pops up.
    */
    private void OpenFile() {            
        if(!SaveFileWarningDialogBox()) return;
        
        JFileChooser fileChooser = new JFileChooser();
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Only .txt", "txt");
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(true);
        
        int option = fileChooser.showOpenDialog(null);
        
        if(option == JFileChooser.APPROVE_OPTION) {
            try{    
                File openFile = new File(fileChooser.getSelectedFile().getPath());
                if(openFile.exists()) {
                    Scanner scanner = new Scanner(openFile);
                    if(scanner.hasNext()){
                        scanner.useDelimiter("\\Z");
                        textPane.setText(scanner.next());
                    }
                    scanner.close();
                    
                    currentFile = openFile;
                    UpdateFrameTitle(currentFile.getName(), true);
                }
                else
                    JOptionPane.showMessageDialog(fileChooser, "File does not exist", "Error", JOptionPane.ERROR_MESSAGE);
                }
            catch(FileNotFoundException f) {
                JOptionPane.showMessageDialog(fileChooser, f.getStackTrace(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    

    /**
     * Invoked to save file when
     * Save option is selected in File Menu <p>
     * Checks if a file is opened or not. If not opened then
     * it will call SaveFileAs() method.
    */
    private void SaveFile() {
        if(currentFile == null) SaveFileAs();
        else {
            try {
                FileWriter fileWriter = new FileWriter(currentFile);
                fileWriter.write(textPane.getText());
                fileWriter.flush();
                fileWriter.close();

                UpdateFrameTitle(currentFile.getName(), true);
            }
            catch(IOException e) {
                System.out.println(e.getStackTrace());
            }
        }
    }
    

    /**
     * Invoked to save as new file when
     * Save As option is selected from file menu<p>
     * Also called when save option is selected 
     * and no file is opened 
    */
    private void SaveFileAs() {
        JFileChooser fileChooser = new JFileChooser();
        
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Only .txt", "txt");
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(true);
        
        int option = fileChooser.showSaveDialog(null);
        
        if(option == JFileChooser.APPROVE_OPTION) {
            try {
                File saveFile;
                
                if(fileChooser.getSelectedFile().getName().endsWith(".txt") || fileChooser.getFileFilter() != filter){
                    saveFile = new File(fileChooser.getSelectedFile().getPath());
                }
                else{
                    saveFile = new File(fileChooser.getSelectedFile().getPath() + ".txt");
                }
                if(saveFile.createNewFile()) {
                    FileWriter fileWriter = new FileWriter(saveFile);
                    fileWriter.write(textPane.getText());
                    fileWriter.flush();
                    fileWriter.close();
                    
                    currentFile = saveFile;
                    UpdateFrameTitle(currentFile.getName(), true);
                }
                
                else{
                    JOptionPane.showMessageDialog(fileChooser, "File already exists", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(IOException e) {
                JOptionPane.showMessageDialog(fileChooser, e.getStackTrace(), "Error", JOptionPane.ERROR_MESSAGE);             
            }
        }
    }


    /**
     * Invoked to exit the application when
     * Exit option is selected from file menu. <p>
     * Also checks if the file is saved or not 
     * with SaveFileWarningDialogBox() method.
    */
    private void ExitApplication() {
        if(!SaveFileWarningDialogBox()) return;
    
        System.exit(0);
    }


    /**
     * To set the number of lines in 
     * textPaneLineNumbers
    */
    private String GetNumberOfLinesOfTextPane() {
        int docLength = textPane.getDocument().getLength();
        Element root = textPane.getDocument().getDefaultRootElement();
        String text = " 1 ";
        for(int i = 2; i < root.getElementIndex(docLength) + 2; i++){
            text += System.getProperty("line.separator")+ " " + i + " ";
        }
        return text;
    }


    /**
     * To update the name of frame when a file is opened,
     * changed, or created a new file.
    */
    private void UpdateFrameTitle(String title, boolean isFileSaved) {
        if(isFileSaved){
            frame.setTitle(title + "-" + applicationName);
        }
        else{
            frame.setTitle("*" + title + "-" + applicationName);
        }
    }

    /**
     * To check if the current file opened is 
     * saved or not. <p>
     * Returns true if file is saved and 
     * false if the file is not saved. 
    */
    private boolean IsFileSaved() {
        try{
            Scanner scanner = new Scanner(currentFile);
            if(scanner.hasNext()){
                scanner.useDelimiter("\\Z");
                
                if(textPane.getText().equals(scanner.next())) {
                    scanner.close();
                    return true;
                }
                else{
                    scanner.close();
                    return false;
                }
            }
            else {
                if(textPane.getText().equals("")){
                    scanner.close();
                    return true;
                }
                else{
                    scanner.close();
                    return false;
                }
            }
        }
        catch(FileNotFoundException f) {
            f.printStackTrace();
            return false;
        }
    }
    

    /**
     * Invoked when user wants to create new file,
     * open new file, exit the application if the 
     * file is not saved. <p>
     * 
     * Returns true to continue to operation and 
     * false to terminate the operation selected by user.
    */
    private boolean SaveFileWarningDialogBox() {
        if(currentFile != null){ 
            if(IsFileSaved()){
                return true;
            }
        }
        if(currentFile == null){
            if(textPane.getText().equals("")){
                return true;
            }
        }
            
        Object[] options = {"Save", "Don't Save", "Cancel"};
        int choosedOption = JOptionPane.showOptionDialog(frame, "All the changes have been made will be lost", "Save File?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[2]);

        switch(choosedOption) {
            case 0: SaveFile(); break;
            case 1: return true;
            case 2: return false;
        }
        return false;
    }
    
}