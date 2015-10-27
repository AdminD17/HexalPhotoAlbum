package HexalPhotoAlbum.GUI.Panels.AlbumContent.MediaPanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import ExtraClass.ImageResizer;
import HexalPhotoAlbum.GUI.Images.ImageGUILoader;
import HexalPhotoAlbum.GUI.Panels.AlbumContent.AlbumContentPanel;
import net.iharder.dnd.FileDrop;

/**
 * Panel para agregar contenido a la librería arrastrando y soltando los archivos
 * 
 * @author David Giordana
 *
 */
public class AddPanel extends JPanel implements FileDrop.Listener{

	private static final long serialVersionUID = 6372031253490664047L;

	//Contiene la imagen de fondo
	private ImageIcon image;

	/**
	 * Constructor de la clase
	 */
	public AddPanel(){
		super();
		image = ImageGUILoader.getImage("addDND.png");

		new FileDrop(this , this);
		this.setBackground(Color.decode("#898C90"));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		ImageIcon i = getResized();
		if(i == null){
			return;
		}
		int x = (this.getSize().width - i.getIconWidth()) / 2;
		int y = (this.getSize().height - i.getIconHeight()) / 2;
		g.drawImage(i.getImage(), x, y, null);
	}

	/**
	 * Retorna la imagen redimensionada para asetear el fondo
	 * @return Imagen redimensionada
	 */
	private ImageIcon getResized(){
		if(image == null){
			return null;
		}
		Dimension d = this.getSize();
		if(d.height >= image.getIconHeight() || d.width >= image.getIconWidth()){
			return image;
		}
		return ImageResizer.resize(image, d.width, d.height);
	}

	@Override
	public void filesDropped(File[] arg0) {
		AlbumContentPanel.getInstance().importDnDData(arg0);	
	}

}
