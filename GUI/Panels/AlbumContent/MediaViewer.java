package HexalPhotoAlbum.GUI.Panels.AlbumContent;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import ExtraClass.GUI.ScrollableList;
import HexalPhotoAlbum.Data.DataController;
import HexalPhotoAlbum.Data.LibraryItem;
import HexalPhotoAlbum.GUI.Panels.AlbumContent.MediaPanels.AddPanel;
import HexalPhotoAlbum.GUI.Panels.AlbumContent.MediaPanels.PhotoPanel;
import HexalPhotoAlbum.GUI.Panels.AlbumContent.MediaPanels.VideoPanel;


/**
 * Panel para mostrar imagen o video de un album
 *
 * @author David Giordana
 *
 */
public class MediaViewer extends JPanel implements ActionListener , MouseListener{

	private static final long serialVersionUID = -4248443481445815428L;

	//etiquetas para mostrar paneles
	private static final String IMAGE = "image";
	private static final String VIDEO = "video";
	private static final String NONE = "Node";

	//Paneles para mostrar contenido
	private PhotoPanel image;
	private VideoPanel video;
	private AddPanel none;

	//Controlador de datos
	private DataController dc;

	//Botones adelantar, retroceder y panel central
	private AdvanceButton back , next;
	private JPanel central;

	//Menu contextual
	private JPopupMenu menu;
	private JMenuItem remove;
	private JMenuItem delete;
	private JMenuItem createAlbum;
	private JMenuItem transferToAlbum;

	//Indica el tipo del item actual
	private int type;

	//layout del panel
	private CardLayout cl;

	//Contiene la instancia de la clase
	private static MediaViewer INS;

	/**
	 * Obtiene una instancia unica de la calse
	 * @return instancia de la clase
	 */
	public static MediaViewer getInstance(){
		if(INS == null){
			INS = new MediaViewer();
		}
		return INS;
	}

	/**
	 * Constructor de la clase
	 */
	private MediaViewer(){
		//instancia los componentes de la clase
		dc = DataController.getInstance();
		image = PhotoPanel.getInstance();
		video = VideoPanel.getInstance();
		none = new AddPanel();
		central = new JPanel();
		back = new AdvanceButton();
		next = new AdvanceButton();
		cl = new CardLayout();
		type = -1;
		createContextualMenu();

		//Setea los parametros de los componentes
		this.setLayout(new BorderLayout());
		back.addActionListener(this);
		back.setToolTipText("Atras");
		next.addActionListener(this);
		next.setToolTipText("Siguiente");
		image.addMouseListener(this);
		video.addMouseListener(this);

		//agrega los componentes al panel central
		this.central.setLayout(cl);
		this.central.add(image, IMAGE);
		this.central.add(video, VIDEO);
		this.central.add(none , NONE);

		//Agrega los componentes al panel
		this.add(back , BorderLayout.WEST);
		this.add(central , BorderLayout.CENTER);
		this.add(next , BorderLayout.EAST);
	}

	/**
	 * Crea el menu contextual de la imagen
	 */
	private void createContextualMenu(){
		menu = new JPopupMenu("Opciones");

		//Opcion quitar del album
		remove = new JMenuItem("Quitar del album");
		remove.addActionListener(this);
		menu.add(remove);

		//Opcion eliminar elemento
		delete = new JMenuItem("Eliminar Fotografía");
		delete.addActionListener(this);
		menu.add(delete);

		//Crea un album
		createAlbum = new JMenuItem("Crear album");
		createAlbum.addActionListener(this);
		menu.add(createAlbum);

		//Transfiere contenido a album
		transferToAlbum = new JMenuItem("Transferir Fotografia a album");
		transferToAlbum.addActionListener(this);
		menu.add(transferToAlbum);
	}

	/**
	 * ---- EXTRA CLASS
	 */

	/**
	 * Panel para seleccionar el destino de las transferencais
	 *
	 * @author David Giordana
	 *
	 */
	private class AlbumsPanel extends JPanel implements ActionListener{

		private static final long serialVersionUID = -4543011701247328288L;

		//Lista de albumes
		private ScrollableList albums;

		//Voton para crear albumes
		private JButton createAlbum;

		//Controlador de datos
		private DataController dc;

		/**
		 * Constructor de la clase
		 */
		public AlbumsPanel(){
			//Instancia los componentes
			albums = new ScrollableList();
			createAlbum = new JButton("Crear Album");
			dc = DataController.getInstance();

			//Setea los componentes
			this.createAlbum.addActionListener(this);
			this.setLayout(new BorderLayout());
			fillList();

			//Agrega los componentes al panel
			this.add(albums, BorderLayout.CENTER);
			this.add(createAlbum , BorderLayout.SOUTH);
		}

		/**
		 * rellena la lista de albumes
		 */
		private void fillList(){
			albums.getModel().addElement("Fotos Desordenadas");
			ArrayList<String> list = dc.getAlbumsList();
			for(String str : list){
				albums.getModel().addElement(str);
			}
			albums.getList().setSelectedIndex(0);
		}
		
		/**
		 * Retorna el album seleccionado en la lista
		 * @return Album seleccionado
		 */
		public String getAlbum(){
			return albums.getList().getSelectedIndex() == 0 ? null : albums.getList().getSelectedValue();
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource().equals(createAlbum)){
				int cant = dc.getAlbumsList().size();
				boolean b = dc.createAlbumDialog();
				if(b && cant != dc.getAlbumsList().size()){
					albums.getModel().addElement(dc.getAlbumsList().get(cant));
				}
			}
		}

	}

	/**
	 * Clase que extiende de JButton usada para no tener que setear varias veces los botones
	 *
	 * @author David Giordana
	 *
	 */
	private class AdvanceButton extends JButton{

		private static final long serialVersionUID = -119526374495366758L;
		private static final int W = 25;

		/**
		 * Constructor de la clase
		 */
		public AdvanceButton(){
			this.setBorderPainted(false);
			this.setBackground(Color.decode("#2B2B2B"));
			this.setOpaque(true);
			this.setText("...");
			this.setForeground(Color.decode("#C7C7CC"));
		}

		@Override
		public Dimension getPreferredSize(){
			Dimension d = super.getPreferredSize();
			return new Dimension(W , d.height);
		}

		@Override
		public Dimension getMinimumSize(){
			Dimension d = super.getMinimumSize();
			return new Dimension(W , d.height);
		}

	}


	/**
	 * ---- CLASS METHODS
	 */

	/**
	 * Muestra el archivo en pantalla
	 * @param item Item de librería a mostrar
	 */
	public void showMedia(LibraryItem item){
		if(item == null){
			type = -1;
			OptionsPanel.getInstance().setCounters(0 , 0);
			cl.show(this.central , NONE);
		}
		else if(item.getDataType() == LibraryItem.IMAGE_TYPE){
			if(type != LibraryItem.IMAGE_TYPE){
				type = item.getDataType();
				image.setItem(item);
				image.showMedia();
				cl.show(this.central , IMAGE);
			}
			else{
				image.setItem(item);
				image.showMedia();
			}
		}
		else if(item.getDataType() == LibraryItem.VIDEO_TYPE){
			if(type != LibraryItem.VIDEO_TYPE){
				type = item.getDataType();
				cl.show(this.central, VIDEO);
				video.showMedia(item);
			}
			else
				video.showMedia(item);
		}
	}

	/**
	 * Valida la botonera
	 */
	public void validateButtons(){
		back.setEnabled(AlbumContentPanel.getInstance().hasBack());
		next.setEnabled(AlbumContentPanel.getInstance().hasNext());
	}

	/**
	 * ---- CONTEXTUAL MENU METHODS
	 */

	/**
	 * LLamado a la hora de remover item de album
	 */
	private void removeFromAlbum(){
		AlbumContentPanel.getInstance().transferItem(null);
	}

	/**
	 * Elimina archivo de kubrería
	 */
	private void deleteItem(){
		AlbumContentPanel.getInstance().deleteItem();
	}

	/**
	 * Crea un album
	 */
	private void createAlbum(){
		dc.createAlbumDialog();
	}

	/**
	 * Transfiere el elemento actual a otro album
	 */
	private void transferDataToAlbum(){
		AlbumsPanel albumPanel = new AlbumsPanel();
		String name;
		int selection = JOptionPane.showOptionDialog(
				null,
				albumPanel, 
				"Seleccione el album", 
				JOptionPane.YES_NO_OPTION , 
				JOptionPane.PLAIN_MESSAGE ,
				null ,
				new String[] {"Transferir" , "Cancelar"} ,
				"Transferir"
				);
		if(selection != JOptionPane.OK_OPTION)
			return;
		name = albumPanel.getAlbum();
		AlbumContentPanel.getInstance().transferItem(name);
	}

	/**
	 * ---- INTERFACE METHODS
	 */

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(back)){
			AlbumContentPanel.getInstance().advance(-1);
			validateButtons();
		}
		else if(e.getSource().equals(next)){
			AlbumContentPanel.getInstance().advance(1);
			validateButtons();
		}
		else if(e.getSource().equals(remove)){
			removeFromAlbum();
		}
		else if(e.getSource().equals(delete)){
			deleteItem();
		}
		else if(e.getSource().equals(createAlbum)){
			createAlbum();
		}
		else if(e.getSource().equals(transferToAlbum)){
			transferDataToAlbum();
		}
	}


	@Override
	public void mouseClicked(MouseEvent e) {}


	@Override
	public void mousePressed(MouseEvent e) {
		if((e.getSource().equals(image) || e.getSource().equals(video)) && SwingUtilities.isRightMouseButton(e)){
			menu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {}


	@Override
	public void mouseEntered(MouseEvent e) {}


	@Override
	public void mouseExited(MouseEvent e) {}

}

