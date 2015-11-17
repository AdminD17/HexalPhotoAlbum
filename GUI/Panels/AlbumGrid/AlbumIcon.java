package HexalPhotoAlbum.GUI.Panels.AlbumGrid;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import HexalPhotoAlbum.Data.ImportWorker;
import HexalPhotoAlbum.GUI.OptionsClass;
import HexalPhotoAlbum.GUI.Images.ImageGUILoader;
import HexalPhotoAlbum.GUI.Panels.AlbumsPanel;
import net.iharder.dnd.FileDrop;

/**
 * Clase que representa visualmente el icono de un album
 * 
 * @author David Giordana
 *
 */
public class AlbumIcon extends JPanel implements FileDrop.Listener{

	private static final long serialVersionUID = -2460402491567579008L;

	//tamaño de cada icono
	public static final Dimension DIM = new Dimension(100,100);

	//Cantidad de letras permitidas por nombre
	private final int COLS = 18;

	// indice del modo fotos sin clasificar
	public static final int DISORDERED_ICON = 0;

	// indice de modo album
	public static final int ALBUM_ICON = 1;

	/**
	 * ----ATTRIBUES
	 */

	//nombre del album
	private String name;

	//tipo de album
	private int type;

	//label del icono
	private JLabel label;

	//Campo con el nombre del album
	private JTextField textField;

	//Bordes
	private LineBorder selectedBorder;
	private LineBorder unselectedBorder;

	//Controlador de opciones
	private OptionsClass oc;

	//COntiene una instancia de la grilla de albumes
	private AlbumsGrid ag;

	/**
	 * ---- CONSTRUCTOR
	 */

	/**
	 * Constructor de la clase
	 * @param name nombre del album
	 * @param iconType indice del icono
	 */
	public AlbumIcon(String name , int iconType){		
		//Instancia los componentes de la clase
		this.ag = AlbumsGrid.getInstance();
		this.type = iconType;
		this.name = name;
		this.label = new JLabel(){

			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredSize(){
				return DIM;
			}

			@Override
			public Dimension getMinimumSize(){
				return DIM;
			}

			@Override
			public Dimension getMaximumSize(){
				return DIM;
			}

		};
		this.textField = new JTextField();
		this.selectedBorder = new LineBorder(Color.BLACK , 2 , true);
		this.unselectedBorder = new LineBorder(new Color(0,0,0,0) , 2);
		this.oc = OptionsClass.getInstance();

		//Setea los componentes de la clase
		this.label.setHorizontalAlignment(SwingConstants.CENTER);
		this.label.setVerticalAlignment(SwingConstants.CENTER);
		this.textField.setColumns(/*COLS*/13);
		this.textField.setEditable(false);
		this.textField.setBorder(null);
		this.textField.setBackground(null);
		this.textField.setHorizontalAlignment(SwingConstants.CENTER);
		this.setBorder(unselectedBorder);
		setIcon(iconType);
		setAlbumName(name);

		//agrega los componentes al panel
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill= GridBagConstraints.BOTH;
		this.add(label , gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill= GridBagConstraints.HORIZONTAL;
		this.add(textField , gbc);

		//agrega los listeners del panel
		this.addMouseControl();
		new FileDrop(this , this);
	}

	/**
	 * ---- METHODS
	 */

	/**
	 * Renombra el icono del album
	 * @param index indice del icono
	 */
	private void setIcon(int index){
		if(index == ALBUM_ICON){
			label.setIcon(ImageGUILoader.getImage("Album.png"));
		}
		else if(index == DISORDERED_ICON){
			label.setIcon(ImageGUILoader.getImage("Disordered.png"));
		}
		else{
			label.setIcon(null);
		}
	}

	/**
	 * Rebinbra el icono del album
	 * @param newName nuevo nombre a setear
	 */
	public void setAlbumName(String name){
		//Caso fotos desordenadas
		if(type == DISORDERED_ICON){
			this.name = null;
			return;
		}

		//Caso album 
		String text = name;
		if(name.length() > COLS){
			text = name.substring(0, COLS - 3) + "...";
		}
		this.name = text;
		this.textField.setText(text);
	}

	/**
	 * Obtiene el nombre del icono
	 * @return nombre del icono
	 */
	public String getAlbumName(){
		return this.name;
	}

	/**
	 * Devuelve el tipo de album
	 * @return tipo de album
	 */
	public int getType(){
		return type;
	}

	/**
	 * Setea si el ícono está seleccionado
	 * @param b True en caso de estar seleccionado, falso en caso contrario
	 */
	public void setSelected(boolean b){
		if(b){
			this.setBorder(selectedBorder);
		}
		else{
			this.setBorder(unselectedBorder);
		}
	}

	/**
	 * Crea el menu contextual del icono
	 * @return Menu contextual
	 */
	private JPopupMenu createMenuPopUp(){
		//Crea el menu
		int[] arr = null;
		if(this.getType() == DISORDERED_ICON){
			arr = new int[]{
					OptionsClass.CREATE_ALBUM
			};
		}
		else if (this.getType() == ALBUM_ICON){
			arr = new int[]{
					OptionsClass.CREATE_ALBUM,
					-1,
					OptionsClass.DELETE_ALBUM,
					OptionsClass.CLOSE_ALBUM
			};
		}
		return oc.getMenu(arr);
	}

	/**
	 * Agrega los controles del mouse al icono
	 */
	private void addMouseControl(){
		AlbumIcon albumIcon = this;
		MouseListener ml = new MouseAdapter(){

			@Override
			public void mousePressed(MouseEvent e) {
				ag.setSelected(albumIcon);

				if(SwingUtilities.isRightMouseButton(e)){
					createMenuPopUp().show(e.getComponent(), e.getX(), e.getY());
				}
				else if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount() > 1){
					AlbumsPanel.getInstance().showAlbum(getAlbumName());
				}
			}

		};
		this.addMouseListener(ml);
		this.label.addMouseListener(ml);
		this.textField.addMouseListener(ml);
	}

	@Override
	public void filesDropped(File[] arg0) {
		ImportWorker.getWorker(arg0, name).start();
	}

}
