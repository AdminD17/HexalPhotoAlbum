package HexalPhotoAlbum.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import HexalPhotoAlbum.Data.DataController;
import HexalPhotoAlbum.GUI.Panels.AlbumsPanel;


/**
 * Clase encargada de mostrar la ventana del administrador al usuario
 * 
 * @author David Giordana
 *
 */
public class PhotoAlbumFrame extends JFrame {

	private static final long serialVersionUID = -2849483909734192515L;

	/**
	 * ---- ATTRIBUTES
	 */

	//administrador de datos
	private DataController dc;
	
	//panel de albumes
	private AlbumsPanel albumPanel;
	
	//Contiene la instancia de la clase
	private static PhotoAlbumFrame ins;

	/**
	 * ---- CONSTRUCTOR
	 */

	/**
	 * Retorna una instancia de la clase
	 * @return Instancia de la clase
	 */
	public static PhotoAlbumFrame getInstance(){
		if(ins == null){
			ins = new PhotoAlbumFrame();
		}
		return ins;
	}
	
	/**
	 * Constructor de la ventana
	 */
	private PhotoAlbumFrame(){
		//instancia los componentes de la clase
		dc = DataController.getInstance();
		albumPanel = AlbumsPanel.getInstance();

		//agrega los componentes a la ventana
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(albumPanel, BorderLayout.CENTER);

		//setea los parametros de la ventana
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Hexal Photo");

		//realiza la inicializacion de los datos
		dc.checkInstall();
		albumPanel.showGrid();
	}

	/**
	 * Muestra la ventana de la aplicacion
	 */
	public void showApp(){
		this.setMinimumSize(new Dimension(1024,768));
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
}
