package HexalPhotoAlbum.GUI.Panels;

import java.awt.CardLayout;

import javax.swing.JPanel;

import HexalPhotoAlbum.GUI.Panels.AlbumContent.AlbumContentPanel;
import HexalPhotoAlbum.GUI.Panels.AlbumGrid.AlbumsGrid;

/**
 * Panel encargado de mostrar los albumes de la libreria
 * 
 * @author David Giordana
 *
 */
public class AlbumsPanel extends JPanel{

	private static final long serialVersionUID = -2019169534971480302L;

	//Etiquetas de paneles
	public static final String GRID_PANEL = "GridPanel"; 
	public static final String ALBUM_CONTENT = "AlbumContentPanel";

	/**
	 * ---- ATTRIBUTES
	 */

	//grilla de albumes
	private AlbumsGrid albumGrid;

	//panel de albumes
	private AlbumContentPanel albumContentPanel;

	//layout del panel
	private CardLayout cl;
	
	//varaibles de controls
	boolean isGrid;
	boolean isContent;

	//contiene la instancia de la clase
	private static AlbumsPanel ins;

	/**
	 * Retorna una instacia unica de la clase
	 * @return instancia de la clase
	 */
	public static AlbumsPanel getInstance(){
		if(ins == null){
			ins = new AlbumsPanel();
		}
		return ins;
	}

	/**
	 * ---- CONTRUCTOR
	 */

	/**
	 * Constructor del panel
	 */
	private AlbumsPanel() {
		//Instancia los componentes de la clase
		cl = new CardLayout();
		albumContentPanel = AlbumContentPanel.getInstance();
		albumGrid = AlbumsGrid.getInstance();

		//Agrega los coponentes al panel
		this.setLayout(cl);
		this.add(albumGrid , GRID_PANEL);
		this.add(albumContentPanel , ALBUM_CONTENT);
	}

	/**
	 * Muestra el panel de cotenido de album
	 * @param album nombre del album
	 */
	public void showAlbum(String album){
		albumContentPanel.setAlbum(album);
		cl.show(this, ALBUM_CONTENT);
		isGrid = false;
		isContent = true;
	}

	/**
	 * Muestra la cuadricula de albumes
	 */
	public void showGrid(){
		cl.show(this, GRID_PANEL);
		albumGrid.setSelected(null);
		isGrid = true;
		isContent = false;
	}

	/**
	 * Agrega un album a las listas
	 * @param Nombre del album
	 */
	public void addAlbum(String name) {
		albumGrid.addAlbum(name);
	}
	
	/**
	 * Elimina un album
	 * @param name Nombre del album a eliminar
	 */
	public void removeAlbum(String name){
		albumGrid.removeAlbum(name);
	}
	
	/**
	 * Indica si el panel mostrado actualmente es la grilla
	 * @return true si se está mostrando la grilla de albumes
	 */
	public boolean isGridShow(){
		return isGrid;
	}
	
	/**
	 * Indica si se está mostrando el panel de contenidos
	 * @return true si se está mostrando el panel de contenidos
	 */
	public boolean isContentShow(){
		return isContent;
	}
	
}
