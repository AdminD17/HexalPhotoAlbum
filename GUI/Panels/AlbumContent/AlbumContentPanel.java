package HexalPhotoAlbum.GUI.Panels.AlbumContent;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

import HexalPhotoAlbum.Data.DataController;
import HexalPhotoAlbum.Data.ImportWorker;
import HexalPhotoAlbum.Data.LibraryItem;
import HexalPhotoAlbum.GUI.Images.ImageGUILoader;
import HexalPhotoAlbum.GUI.Panels.AlbumContent.MediaPanels.AddPanel;

/**
 * Panel para mostrar el contenido de un album
 *
 * @author David Giordana
 *
 */
public class AlbumContentPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1416365617623193987L;

	//Nombre del album
	private String album;

	//Panel de opciones
	private OptionsPanel options;

	//Panel para mostrar archivo en pantalla
	private MediaViewer screen;

	//Panel para agregar items a la lista
	private AddPanel addPanel;

	//Boton mostrar / ocultar panel de transferencias
	private JButton hide;

	//Panel de transferencias
	private JPanel transferPanel;
	private boolean showTransferPanel;

	//Lista con los elementos del album
	private ArrayList<LibraryItem> list;

	//Inidice de la lista
	private int index;

	//Contiene la instancia de la clase
	private static AlbumContentPanel ins;

	/**
	 * Retorna una instancia única de la clase
	 * @return Instancia de la clase
	 */
	public static AlbumContentPanel getInstance(){
		if(ins == null){
			ins = new AlbumContentPanel();
		}
		return ins;
	}

	/**
	 * Constructor de la clase
	 */
	private AlbumContentPanel(){
		//instancia los componentes de la clase
		screen = MediaViewer.getInstance();
		options = OptionsPanel.getInstance();
		hide = new JButton(ImageGUILoader.getImage("add.png"));
		addPanel = new AddPanel(){

			private static final long serialVersionUID = -3494959988977160597L;

			@Override
			public Dimension getPreferredSize(){
				return new Dimension(200 , 200);
			}

			@Override
			public Dimension getMinimumSize(){
				return new Dimension(200 , 200);
			}

		};
		transferPanel = new JPanel(new BorderLayout());

		//Setea parametros de la clase
		this.setLayout(new BorderLayout());
		this.hide.addActionListener(this);
		this.hide.setToolTipText("Morstrar / Ocultar");
		this.setTransferPanelVisible(false);

		//Crea el panel inferior
		transferPanel.add(hide , BorderLayout.NORTH);
		transferPanel.add(addPanel, BorderLayout.SOUTH);

		//agrega los componentes al panel
		this.add(options, BorderLayout.NORTH);
		this.add(screen, BorderLayout.CENTER);
		this.add(transferPanel, BorderLayout.SOUTH);
	}

	/**
	 * Setea el contenido del album a mostrar
	 * @param s nombre del album
	 */
	public void setAlbum(String s){
		album = s;
		list = DataController.getInstance().getAlbumContent(s);
		index = -1;
		updateUIData();
		setTransferPanelVisible(false);
	}

	/**
	 * Valída el índice de la lista
	 */
	private void validateIndex(){
		if(list.isEmpty()){
			index = -1;
		}
		else if(list.size() <= index){
			index = list.size() -1;
		}
		else if(index == -1){
			index = 0;
		}
	}

	/**
	 * Actualiza los datos de interfaz gráfica
	 */
	public void updateUIData(){
		validateIndex();
		updateCounters();
		screen.validateButtons();

		if(list.isEmpty()){
			screen.showMedia(null);
		}
		//Muestra el elemento correspondiente
		else{
			screen.showMedia(list.get(index));
		}

	}

	/**
	 * Actualiza los contadores
	 */
	private void updateCounters(){
		OptionsPanel.getInstance().setCounters(list.size(), index + 1);
	}

	/**
	 * Hace avanzar la lista
	 * @param direction Posotivo para avanzar, negativo para retroceder
	 */
	public void advance(int direction){
		if(direction < 0){
			index--;
			updateUIData();
		}
		else{
			index++;
			updateUIData();
		}
	}

	/**
	 * Indica si se puede avanzar en la lista
	 * @return True si se puede avanzar
	 */
	public boolean hasNext(){
		return index < list.size() -1;
	}

	/**
	 * Indica si se puede retroceder en la lista
	 * @return True si se puede retroceder
	 */
	public boolean hasBack(){
		return index > 0;
	}

	/**
	 * Actualiza los contadores
	 * @param arr Lista de archivos a agregar
	 */
	public void importDnDData(File[] arr){
		ImportWorker.getWorker(arr, album).start();
	}

	/**
	 * Transfiere el objeto actual al album indicado
	 * @param album Nombre del album al que se trnasferira el archivo
	 */
	public void transferItem(String album){
		LibraryItem li = list.get(index);
		DataController.getInstance().copyToAlbum(album, li);
		updateUIData();
		screen.validateButtons();
	}

	/**
	 * Elimina el objeto actual de la librería
	 */
	public void deleteItem(){
		LibraryItem li = list.get(index);
		DataController.getInstance().removeLibraryItem(li);
		updateUIData();
		screen.validateButtons();
	}

	/**
	 * Retorna la cantidad de items del album
	 * @return cantidad de items del album
	 */
	public int getItemsCount(){
		return list.size();
	}

	/**
	 * Muestra u oculta el panel de transferencia
	 * @param b true para mostrar el panel, falso para ocultarlo
	 */
	public void setTransferPanelVisible(boolean b){
		showTransferPanel = b;
		addPanel.setVisible(b);
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		setTransferPanelVisible(!showTransferPanel);
	}

}
