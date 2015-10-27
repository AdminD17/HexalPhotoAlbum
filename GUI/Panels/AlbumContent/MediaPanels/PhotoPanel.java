package HexalPhotoAlbum.GUI.Panels.AlbumContent.MediaPanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import ExtraClass.ImageResizer;
import HexalPhotoAlbum.Data.LibraryItem;

/**
 * Panel donde se mostrataran imagenes 
 * 
 * @author David Giordana
 *
 */
public class PhotoPanel extends JPanel implements ComponentListener{

	private static final long serialVersionUID = 5140752227182009778L;

	//label donde se mostrará la imagen
	private JLabel label;

	//Imagen a mostrar
	private ImageIcon image;
	
	//contiene la instancia de la clase
	private static PhotoPanel ins;

	/**
	 * Obtiene una instancia unica de la clase
	 * @return intancia de la clase
	 */
	public static PhotoPanel getInstance(){
		if(ins == null){
			ins = new PhotoPanel();
		}
		return ins;
	}

	/**
	 * Constructor de la clase
	 */
	private PhotoPanel(){
		//Instancia los componentes de la clase
		label = new JLabel();

		//Setea los parametros del panel
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		this.addComponentListener(this);
		this.setBackground(Color.decode("#4A4A4A"));

		//Agrega los componentes al panel
		this.setLayout(new BorderLayout());
		this.add(label , BorderLayout.CENTER);
	}
		
	/**
	 * Setea el item a mostrar
	 * @param item Item a mostrar
	 */
	public void setItem(LibraryItem item){
		this.image = item == null ? null : new ImageIcon(item.getAbsoluteFilePath());
	}

	/**
	 * Muestra la imagen en pantalla
	 */
	public void showMedia(){
		label.setIcon(resize());
	}

	/**
	 * REdimensiona una la imagen al tamaño óptimo
	 * @return Imagen redimensionada
	 */
	public ImageIcon resize(){
		//En caso de no ser necesario redimensionar
		Dimension panelSize = this.getSize();
		Dimension imageSize = image == null ? new Dimension(1 , 1) : new Dimension(image.getIconWidth() , image.getIconHeight());
		if(image != null && image.getIconHeight() <= panelSize.height && image.getIconWidth() <= panelSize.width){
			return image;
		}

		//Crea las variables temporales
		int w = imageSize.width;
		int h = imageSize.height;

		// Comprueba si es encesario escalar el ancho
		if (imageSize.width > panelSize.width) {
			w = panelSize.width;
			h = (w * imageSize.height) / imageSize.width;
		}

		// Comprueba si es encesario escalar el alto
		if (h > panelSize.height) {
			h = panelSize.height;
			w = (h * imageSize.width) / imageSize.height;
		}
		
		//Redimensiona y retorna la imagen
		return ImageResizer.resize(image, w, h);		
	}

	@Override
	public void componentResized(ComponentEvent e) {
		showMedia();
	}

	@Override
	public void componentMoved(ComponentEvent e) {}

	@Override
	public void componentShown(ComponentEvent e) {}

	@Override
	public void componentHidden(ComponentEvent e) {}

}
