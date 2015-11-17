package HexalPhotoAlbum.GUI.Panels.AlbumGrid;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import HexalPhotoAlbum.Data.DataController;
import HexalPhotoAlbum.Data.ImportWorker;
import HexalPhotoAlbum.GUI.Images.ImageGUILoader;
import net.iharder.dnd.FileDrop;

/**
 * Panel de albumes con capacidad de recibir archivos via drag and drop
 * 
 * @author David Giordana
 *
 */
public class AlbumsGrid extends JPanel implements FileDrop.Listener , ActionListener{

	private static final long serialVersionUID = 9221855484611127388L;

	//brecha entre iconos
	private final int GAP = 10;

	//controlador de datos
	private DataController dc;

	//Grilla de albumes
	private JPanel grid;

	//boton agregar album
	private JButton add;

	//Scroll de la grilla
	private JScrollPane scroll;
	
	//Contiene el icono seleccionado
	private AlbumIcon selected;

	//Contiene la instancia de la calse
	private static AlbumsGrid ins;

	/**
	 * ---- CONSTRUCTOR
	 */

	/**
	 * Retorna una instancia unica de la clase
	 * @return Instancia de la clase
	 */
	public static AlbumsGrid getInstance(){
		if(ins == null)
			ins = new AlbumsGrid();
		return ins;
	}

	/**
	 * Constructor de la grilla
	 */
	private AlbumsGrid(){
		//Instancia los componentes de la clase
		this.dc = DataController.getInstance();
		this.add = new JButton();
		this.grid = new JPanel();
		this.scroll = new JScrollPane(grid);

		//Configuraciones de apariencia
		this.grid.setLayout(new WrapLayout(FlowLayout.CENTER , GAP , GAP));
		this.scroll.setBorder(null);
		this.scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.add.setToolTipText("Crear album");
		this.add.setIcon(ImageGUILoader.getImage("add.png"));
		this.grid.setBackground(Color.decode("#DBDDDE"));
		this.add.setBackground(Color.decode("#DBDDDE"));

		//Agrega los componentes al panel
		this.setLayout(new BorderLayout());
		this.add(scroll , BorderLayout.CENTER);
		this.add(add , BorderLayout.SOUTH);
		
		//Agrega los listeners
		this.add.addActionListener(this);
		new FileDrop(this.grid , this);
		this.addMouseListener(new MouseAdapter(){
			
			@Override
			public void mouseClicked(MouseEvent e) {
				setSelected(null);
			}
			
		});
	}

	/**
	 * ---- METHODS
	 */

	/**
	 * Agrega un album a la grilla
	 * @param albunName nombre del album a agregar
	 */
	public void addAlbum(String albunName){
		AlbumIcon tempIcon = null;
		if(albunName == null){
			tempIcon = new AlbumIcon(null, AlbumIcon.DISORDERED_ICON);
		}
		else{
			tempIcon = new AlbumIcon(albunName , AlbumIcon.ALBUM_ICON);
		}
		this.grid.add(tempIcon);
		this.grid.revalidate();
	}

	/**
	 * Elimina un icono de la grilla
	 * @param album icono del album a quitar
	 */
	public void removeAlbum(AlbumIcon album){
		grid.remove(album);
		grid.revalidate();
	}
	
	/**
	 * Elimina un icono de la grilla
	 * @param album Nombre del album a quitar
	 */
	public void removeAlbum(String album){
		for(int i = grid.getComponentCount()  - 1; i >= 0 ; i--){
			AlbumIcon ai = (AlbumIcon)grid.getComponent(i);
			if(ai.getAlbumName() == album){
				ai.setVisible(false);
				removeAlbum(ai);
			}
		}
	}
	
	/**
	 * Setea el album seleccionado
	 * @param icon Album a setear
	 */
	public void setSelected(AlbumIcon icon){
		//Quita el icono anterior
		if(selected != null){
			selected.setSelected(false);
		}
		//Setea el nuevo icono
		this.selected = icon;
		if(selected != null){
			selected.setSelected(true);
		}
	}

	/**
	 * Retorna el album seleccionado
	 * @return Album selecionado, null en caso de no haber ninguno
	 */
	public AlbumIcon getSelected(){
		return selected;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		dc.createAlbumDialog();
	}

	@Override
	public void filesDropped(File[] arg0) {
		ImportWorker.getWorker(arg0, null).start();
	}

}
