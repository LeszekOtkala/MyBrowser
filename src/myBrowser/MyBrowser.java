package myBrowser;
/*
 * Program My Browser to prosta przegladarka stron www (plików html). 
 * Aby zobaczyć stronę należy wpisać adres www razem z http:// a następnie nacisnąć przycisk połącz.
 * po wpisaniu adresu można także wybrać z menu Połączenie pozycję połącz.
 * Program pozwala również na otwieranie plików html i tekstowych z dysku. W tym celu należy z menu Plik wybrać pozycję otwórz
 * a następnie wkazać właściwy plik.
 * @autor Leszek Otkała
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import java.net.URL;


import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;



public class MyBrowser extends JFrame implements ActionListener {

	JMenuBar menuBar;
	JMenu menuHelp, menuFile, menuConnection;
	JMenuItem itemOpen,itemClose, itemConnect, itemAbout;
	JTextArea obszarTekstu;
	File file;
	String adr ;
	JTextField adressField; 
	JEditorPane browserEditorPane;
	JButton buttonConnect;
	
	public MyBrowser(){
		
		//ustawienie wymiarow, ustawienie na ekranie, ustawienie operacji przy zamknieciu
		
		Dimension frameSize = new Dimension(800,450);			
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 			//pobranie rozdzielczosci pulpitu SPRAWDZIC DOKLADNIE OPIS!!!
		if(frameSize.height > screenSize.height) frameSize.height = screenSize.height;	// sprawdzenie czy wielkosc ramki nie jest wieksza niz wielkosc pulpitu
		if(frameSize.width > screenSize.width) frameSize.width = screenSize.width;      // i zamiana wymiarow do wielkości pulpitu jeżeli jest mniejszy
		setSize(frameSize);	
		setLocation((screenSize.width-frameSize.width)/2, 
					(screenSize.height-frameSize.height)/2); 	// ustawienie okna na środku ekranu
		setResizable(true);									// blokowanie skalowalowalności okna
		
		
		makeMenu();
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			// domyślna operacja przy zamnknięciu okna
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);		
		
		// dodanie słuchacza i wywołanie funkcji zamykającej alikację
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				closeOper();
			}
			});
		JPanel adressPanel = new JPanel();
		add(adressPanel, BorderLayout.NORTH);
			adressField= new JTextField("Wpisz adres",60);
			adressPanel.add(adressField);
			buttonConnect=new JButton("Połącz");
			adressPanel.add(buttonConnect, BorderLayout.LINE_END);
			buttonConnect.addActionListener(this);
		browserEditorPane= new JEditorPane();
		browserEditorPane.setEditable(false);
		browserEditorPane.setText("Aby przejść na stronę intrnetową wpisz adres w pełnej postaci, np http://www.tu.koszalin.pl");
		obszarTekstu = new JTextArea(30,70);					//utworzenie obszaru tekstowego
		JScrollPane scrlPanel=new JScrollPane(browserEditorPane);	//utworzenie panelu przewijalnego do umieszczenia obszaru tekstowego
		browserEditorPane.addHyperlinkListener(
			new HyperlinkListener() {
									
				@Override
				public void hyperlinkUpdate(HyperlinkEvent e) {
					if(e.getEventType()==HyperlinkEvent.EventType.ACTIVATED) {
							connect(e.getURL().toString());
					}
					
				}
			});
		add(scrlPanel, BorderLayout.CENTER);											//dodanie panelu do ramki
								//pobranie domyslnego jezyka i ustawienie dla programu
		setTitle("My Browser");										// tytuł okna
		setVisible(true);										// ustawienie widocznosci
		
	
}	

/**
 * metoda tworzaca menu
 */
public void makeMenu () {
	menuBar=new JMenuBar(); 			//utworzenie paska menu
	
	menuBar.add(menuFile=createJMenu("Plik", true));		// wywolanie metody tworzacej menu, przypisanie wyniku jej dzialania do obiektu i dodanie do paska menuBar
			
			//wywołanie metody tworzącej elementy menu i dodanie elementów do menu
			
	menuFile.add(itemOpen=createJMenuItem("Otworz..",true));
				itemOpen.addActionListener(this);
	menuFile.addSeparator();
	menuFile.add(itemClose=createJMenuItem("Zakończ", true));
	itemClose.addActionListener(this);		
				
	menuBar.add(menuHelp=createJMenu("Pomoc", true));
			menuHelp.add(itemAbout=createJMenuItem("O programie",true));
				itemAbout.addActionListener(this);
	
	menuBar.add(menuConnection=createJMenu("Połączenie", true));
				menuConnection.add(itemConnect=createJMenuItem("Połącz",true));
					itemConnect.addActionListener(this);	
	this.setJMenuBar(menuBar); //dodanie paska menu
	
	
}


/**
 * metoda tworzaca elementy glowne menu
 * @param name nazwa menu
 * @param enable	czy ma byc aktywne
 * @return
 */
public JMenu createJMenu(String name, boolean enable) {
	JMenu jMenu = new JMenu(name);
	jMenu.setEnabled(enable);
	return jMenu;
}

public JMenuItem createJMenuItem(String name, boolean enable) {
	JMenuItem jMenuItem = new JMenuItem(name);
	jMenuItem.setEnabled(enable);
	return jMenuItem;
}


/**
 * metoda otworz, pozwalajaca wybrac i otworzyc plik
 */
public void otworz() {
					

	JFileChooser choFile=new JFileChooser();
	if(choFile.showOpenDialog(this)==JFileChooser.APPROVE_OPTION)
	{
		file=choFile.getSelectedFile();
				
		try {
			browserEditorPane.setText(file.getPath());
			connect("file:///"+file.getPath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}



public void connect(String address){
	URL url;
	
	try {
		url = new URL(address);
		adressField.setText(address);
		browserEditorPane.setText("WCZYTYWANIE....PROSZĘ CZEKAĆ");
		browserEditorPane.setPage(url);
		
	} catch (Exception e) {
		
		browserEditorPane.setText("Wystąpił błąd, prawdodobnie nieprawidlowy adres (pamiętaj o http:// )");
	}
	
}
public static void closeOper() {
	int value = JOptionPane.showOptionDialog(null,"Czy chcesz zamknac okno? ", "Uwaga",
			JOptionPane.YES_NO_CANCEL_OPTION,
			JOptionPane.WARNING_MESSAGE, null,
			new String[] {
					"Tak",
					"Nie"
			}, "Tak");	
	if(value==JOptionPane.YES_OPTION) {
		//dispose();
		System.exit(0);
		}
	}
 
@Override
public void actionPerformed(ActionEvent e) {
	
	Object akcja=e.getSource();
	if(akcja==itemOpen) 
		otworz();
	else if(akcja==itemClose) 
	closeOper();
	
	else if(akcja==itemAbout) JOptionPane.showMessageDialog(this,"Autor: Leszek Otkała", "O programie", JOptionPane.INFORMATION_MESSAGE);
	
	else if(akcja==itemConnect||akcja==buttonConnect) {
		adr=adressField.getText();
		connect(adr);
	}
}

public static void main(String[] args){
		   new MyBrowser();								//uruchomienie
		   
	   }
			
}

