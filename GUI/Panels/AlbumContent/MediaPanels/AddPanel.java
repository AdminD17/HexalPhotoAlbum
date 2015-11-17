package HexalPhotoAlbum.GUI.Panels.AlbumContent.MediaPanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ExtraClass.ImageResizer;
import HexalPhotoAlbum.GUI.OptionsClass;
import HexalPhotoAlbum.GUI.Images.ImageGUILoader;
import HexalPhotoAlbum.GUI.Panels.AlbumContent.AlbumContentPanel;
import HexalPhotoAlbum.GUI.Panels.AlbumContent.MediaViewer;
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
		//Instancia los componentes
		super();
		image = ImageGUILoader.getImage("addDND.png");
		
		//Agrega los listeners
		new FileDrop(this , this);
		MouseListener ml = new MouseAdapter(){

			@Override
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e)){
					OptionsClass.getInstance().getMenu(MediaViewer.ADD_CONTEXTUAL_MENU).show(e.getComponent(), e.getX(), e.getY());
				}
			}

		};
		this.addMouseListener(ml);
		
		//Setea los componentes de la clase
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
