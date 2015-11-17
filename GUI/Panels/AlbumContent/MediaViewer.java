package HexalPhotoAlbum.GUI.Panels.AlbumContent;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import HexalPhotoAlbum.Data.LibraryItem;
import HexalPhotoAlbum.GUI.OptionsClass;
import HexalPhotoAlbum.GUI.Panels.AlbumContent.MediaPanels.AddPanel;
import HexalPhotoAlbum.GUI.Panels.AlbumContent.MediaPanels.PhotoPanel;
import HexalPhotoAlbum.GUI.Panels.AlbumContent.MediaPanels.VideoPanel;


/**
 * Panel para mostrar imagen o video de un album
 *
 * @author David Giordana
 *
 */
public class MediaViewer extends JPanel implements ActionListener{

	private static final long serialVersionUID = -4248443481445815428L;

	//etiquetas para mostrar paneles
	private static final String IMAGE = "image";
	private static final String VIDEO = "video";
	private static final String NONE = "Node";
	
	//Indices para menu contextual
	public static final int[] PHOTO_CONTEXTUAL_MENU = {
			OptionsClass.REMOVE_FROM_ALBUM,
			OptionsClass.DELETE_PHOTO,
			-1,
			OptionsClass.CREATE_ALBUM,
			OptionsClass.TRANSFER_TO_ALBUM,
			-1,
			OptionsClass.ROTATE_LEFT,
			OptionsClass.ROTATE_RIGHT
	};
	public static final int[] VIDEO_CONTEXTUAL_MENU = {
			OptionsClass.REMOVE_FROM_ALBUM,
			OptionsClass.DELETE_PHOTO,
			-1,
			OptionsClass.CREATE_ALBUM,
			OptionsClass.TRANSFER_TO_ALBUM
	};
	public static final int[] ADD_CONTEXTUAL_MENU = {
			
	};

	//Paneles para mostrar contenido
	private PhotoPanel image;
	private VideoPanel video;
	private AddPanel none;

	//Botones adelantar, retroceder y panel central
	private AdvanceButton back , next;
	private JPanel central;

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
		image = PhotoPanel.getInstance();
		video = VideoPanel.getInstance();
		none = new AddPanel();
		central = new JPanel();
		back = new AdvanceButton();
		next = new AdvanceButton();
		cl = new CardLayout();
		type = -1;

		//Setea los parametros de los componentes
		this.setLayout(new BorderLayout());
		back.addActionListener(this);
		back.setToolTipText("Atras");
		next.addActionListener(this);
		next.setToolTipText("Siguiente");

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
	 * ---- EXTRA CLASS
	 */
	
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
	}

}

