package service;

import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;


public class NotePadService extends JFrame implements ActionListener, WindowListener {

    // Text area to add or edit the text from the user
    JTextArea textArea = new JTextArea();

    // field to store the text file
    File fileNameContainer;

    public NotePadService()  {
        //Initialising font for Note pad
        Font defaultFont = new Font("Arial", Font.PLAIN,16);

        //Container for the NotePad
        Container container = getContentPane();

        // Menu Bar
        JMenuBar topMenu =  new JMenuBar();

        // File Menu
        JMenu  fMenu = new JMenu("File");

        // Edit Menu
        JMenu  eMenu = new JMenu("Edit");

        // Help Menu
        JMenu  hMenu = new JMenu("Help");

        //Layout of the container
        container.setLayout(new BorderLayout());

        // scroll pane to make the text area scrollable one it exceeds the container size
        JScrollPane scrollPane =  new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setVisible(true);

        // Setting text area font, and wrap style and line wrap as true
        textArea.setFont(defaultFont);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        // setting scroll pane to container
        container.add(scrollPane);

        // Creating options in the file head menu
        createMenuItem(fMenu, "New");
        createMenuItem(fMenu, "Open");
        createMenuItem(fMenu, "Save");
        fMenu.addSeparator();
        createMenuItem(fMenu, "Exit");

        // Creating options in the edit head menu
        createMenuItem(eMenu, "Cut");
        createMenuItem(eMenu, "Copy");
        createMenuItem(eMenu, "Paste");

        // Creating options in the help head menu
        createMenuItem(hMenu, "About Notepad");

        //Adding all the menus to the menu bar
        topMenu.add(fMenu);
        topMenu.add(eMenu);
        topMenu.add(hMenu);

        //setting the value of the JMenubar
        setJMenuBar(topMenu);

        // setting  icon for the app
        setIconImage(Toolkit.getDefaultToolkit().getImage("notepad.git"));

        // adding a listener to listen to the window events
        addWindowListener(this);
        // size of the notepad frame
        setSize(500,500);
        // title of the newly created notepad frame
        setTitle("Untitled.txt");
        setVisible(true);

    }

    // method to add options to the menu items
    public void createMenuItem(JMenu jMenu,String text){
        JMenuItem menuItem=new JMenuItem(text);
        menuItem.addActionListener(this);
        jMenu.add(menuItem);
    }

    // THIS METHOD KEEPS A TRACK OF ALL THE ACTION EVENTS PERFORMED AND TAKES ASSIGNED STEPS
    @Override
    public void actionPerformed(ActionEvent e) {

        // Initialising a file chooser to choose a file
        JFileChooser fileChooser = new JFileChooser();
        // if the user clicks on new , create a new notepad
        if (e.getActionCommand().equals("New")) {
            this.setTitle("Untitled.txt Notepad");
            textArea.setText("");
            fileNameContainer = null;
            // if the user clicks on Open , open the file the user selects
        } else if (e.getActionCommand().equals("Open")) {
            int ret = fileChooser.showDialog(null, "Select");
            if (ret == JFileChooser.APPROVE_OPTION) {
                try {
                    // store the selected file in this variable
                    File file1 = fileChooser.getSelectedFile();
                    // open the file by getting its absolute path
                    OpenFile(file1.getAbsolutePath());
                    // set the title in the notepad application using the name it was saved with
                    this.setTitle(file1.getName() + " - Notepad");
                    // the file is now the selected file of the user
                    fileNameContainer  = file1;
                }catch (IOException exception) {
                    // print stack strace of any exception
                    exception.printStackTrace();
                }
            }
            // if the user selects the save option and the file is not null
        } else if (e.getActionCommand().equals("Save")) {
            if (fileNameContainer != null) {
                // if the directory value is not a directory or is null , then by default it chooses the parent directory as the place to save the file
                fileChooser.setCurrentDirectory(null);
                // sets the files to be saved as the current file
                fileChooser.setSelectedFile(fileNameContainer);
            }
        } else {
            // if the user selects the save option and the file is null, create a new file and save it
            fileChooser.setSelectedFile(new File("Untitled.txt"));
        }

        int ret = fileChooser.showDialog(null, null);
        if (ret == JFileChooser.APPROVE_OPTION) {
            try {
                // get the chosen file to be saved
                File file2 = fileChooser.getSelectedFile();
                // save the file by getting its absolute path
                SaveFile(file2.getAbsolutePath());
                // set the title of the notepad whatever the file name is
                this.setTitle(file2.getName() + " - Notepad");
                fileNameContainer = file2;
            } catch (IOException exception) {
                // print stack strace of any exception
                exception.printStackTrace();
            }
            // call exit method if user wants to exit
        } else if (e.getActionCommand().equals("Exit")) {
            Exit();
            // copy the selected text using the copy method
        } else if (e.getActionCommand().equals("Copy")) {
            textArea.copy();
            // copy the selected text using the paste method
        } else if (e.getActionCommand().equals("Paste")) {
            textArea.paste();
            // Show information about the notepad
        } else if (e.getActionCommand().equals("About Notepad")) {
            JOptionPane.showMessageDialog(this, "Created by Oindrila", "Notepad", JOptionPane.INFORMATION_MESSAGE);
            // copy the selected text using the copy method
        }else if (e.getActionCommand().equals("Cut")) {
            textArea.cut();
        }
    }

    private void SaveFile(String absolutePath) throws IOException{
        setCursor(new Cursor(Cursor.WAIT_CURSOR)); // set cursor to wait
        DataOutputStream outputStream= new DataOutputStream(new FileOutputStream(absolutePath)); // initialising the output stream
        outputStream.writeBytes(textArea.getText()); //output the stream of text
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    // exit method for system exit
    public void Exit() {
        System.exit(0);
    }

    private void OpenFile(String absolutePath) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(absolutePath))); // initialising the buffer reader to read the file
        String txt;
        textArea.setText("");
        setCursor(new Cursor(Cursor.WAIT_CURSOR)); // set cursor to wait
        while ((txt = reader.readLine()) != null) { // while the end of the file hasn't reached
            textArea.setText(textArea.getText() + txt + "\r\n"); // add the text in the text area
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        reader.close(); // close reader
    }



    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
    Exit();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
