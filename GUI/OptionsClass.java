package HexalPhotoAlbum.GUI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import HexalPhotoAlbum.GUI.Panels.AlbumContent.AlbumContentPanel;
import HexalPhotoAlbum.GUI.Panels.AlbumContent.MediaViewer;
import HexalPhotoAlbum.GUI.Panels.AlbumContent.MediaPanels.PhotoPanel;
import HexalPhotoAlbum.GUI.Panels.AlbumGrid.AlbumsGrid;

/**
 * Clase con todas las opciones de menu contextual y botones básicas de la interfaz
 * 
 * @author David Giordana
 *
 */
public class OptionsClass {

	/**
	 * ---- CONSTANTS
	 */

	//Crear album
	public static final int CREATE_ALBUM = 0;

	//Cerrar Album
	public static final int CLOSE_ALBUM = 1;

	//Eliminar album
	public static final int DELETE_ALBUM = 2;

	//Quitar del album
	public static final int REMOVE_FROM_ALBUM = 3;

	//Eliminar fotografía
	public static final int DELETE_PHOTO = 4;

	//Transferir foto a album
	public static final int TRANSFER_TO_ALBUM = 5;

	//rotar izquierda
	public static final int ROTATE_LEFT = 6;

	//Rotar derecha
	public static final int ROTATE_RIGHT = 7;

	//copia de archivo a locacion especifica
	public static final int COPY_FILE_TO_LOCATION = 8;

	/**
	 * ---- ATTRIBUTES
	 */

	//Lista de ActionListener
	private ArrayList<ActionListener> actionListenerList;

	//Lista de items de librería
	private ArrayList<JMenuItem> menuItemList;

	//Contiene la instancia de la clase
	private static OptionsClass ins;

	/**
	 * Retorna una instancia unica de la clase
	 * @return instancia de la clase
	 */
	public static OptionsClass getInstance(){
		if(ins == null){
			ins = new OptionsClass();
		}
		return ins;
	}

	/**
	 * Constructor de la clase
	 */
	private OptionsClass(){
		//Crea los Action Listener
		this.actionListenerList = new ArrayList<ActionListener>();
		this.actionListenerList.add(createCreateAlbumListener());
		this.actionListenerList.add(createCloseAlbumListener());
		this.actionListenerList.add(createDeleteAlbumListener());
		this.actionListenerList.add(createRemoveFromAlbumListener());
		this.actionListenerList.add(createDeletePhotoListener());
		this.actionListenerList.add(createTransferToAlbumListener());
		this.actionListenerList.add(createRotateLListener());
		this.actionListenerList.add(createRotateRListener());
		this.actionListenerList.add(createCopyFileToLocation());

		//Crea los items de menu 
		this.menuItemList = new ArrayList<JMenuItem>();
		this.menuItemList.add(createMenuItem("Crear album", CREATE_ALBUM));
		this.menuItemList.add(createMenuItem("Cerrar album", CLOSE_ALBUM));
		this.menuItemList.add(createMenuItem("Eliminar album", DELETE_ALBUM));
		this.menuItemList.add(createMenuItem("Quitar del album", REMOVE_FROM_ALBUM));
		this.menuItemList.add(createMenuItem("Eliminar archivo", DELETE_PHOTO));
		this.menuItemList.add(createMenuItem("Mover archivo a album", TRANSFER_TO_ALBUM));
		this.menuItemList.add(createMenuItem("Rotar a la izquierda", ROTATE_LEFT));
		this.menuItemList.add(createMenuItem("Rotar a la deracha", ROTATE_RIGHT));
		this.menuItemList.add(createMenuItem("Copiar archivo", COPY_FILE_TO_LOCATION));

	}

	/**
	 * Retorna un menu popup
	 * @param index Arreglo con indices de items a agregar al menu
	 * @return Menu Contextual relleno
	 */
	public JPopupMenu getMenu(int[] index){
		JPopupMenu ret = new JPopupMenu();
		for(int i : index){
			if(i < 0){
				ret.addSeparator();
			}
			else{
				ret.add(menuItemList.get(i));
			}
		}
		return ret;
	}

	/**
	 * Agrega un mouse listener a un componente para lanzar un menu contextual
	 * @param parent Componente padre
	 * @param menu Menu a agregar
	 */
	public void addMenuToComponentML(Component parent, JPopupMenu menu){		
		parent.addMouseListener(new MouseAdapter(){

			@Override
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e)){
					menu.show(e.getComponent(), e.getX(), e.getY());
				}
			}

		});
	}

	/**
	 * Retorna un listener de la lista
	 * @param index Indice del listener
	 * @return ActionListener solicitado
	 */
	public ActionListener getActionListener(int index){
		return this.actionListenerList.get(index);
	}

	/**
	 * Activa un item de menu
	 * @param index indice del item a activar
	 */
	public void enableMenuOptions(int index){
		menuItemList.get(index).setEnabled(true);
	}

	/**
	 * Activa un item de menu
	 * @param index arreglo de indices de items a activar
	 */
	public void enableMenuOptions(int[] index){
		for(int i : index){
			enableMenuOptions(i);
		}
	}

	/**
	 * Desactiva un item de menu
	 * @param index indice del item a desactivar
	 */
	public void disableMenuOptions(int index){
		menuItemList.get(index).setEnabled(false);
	}

	/**
	 * Desactiva un item de menu
	 * @param index arreglo de indices a desactivar
	 */
	public void disableMenuOptions(int[] index){
		for(int i : index){
			disableMenuOptions(i);
		}
	}

	/**
	 * Crea un item de menu
	 * @param title Titulo del item
	 * @param listener ActionListener para el item
	 * @return Item creado
	 */
	private JMenuItem createMenuItem(String title, ActionListener listener){
		JMenuItem ret = new JMenuItem(title);
		ret.addActionListener(listener);
		return ret;
	}

	/**
	 * Crea un item de menu
	 * @param title Titulo del item
	 * @param index Indice de ActionListener para el item
	 * @return Item creado
	 */
	private JMenuItem createMenuItem(String title, int index){
		return createMenuItem(title, getActionListener(index));
	}

	/**
	 * Retorna un item de menu
	 * @param index Indice del item
	 * @return Item de menu solicitado
	 */
	public JMenuItem getMenuItem(int index){
		return this.menuItemList.get(index);
	}

	/**
	 * ---- OBJECTS CONSTRUCTOR
	 */

	/**
	 * Crea el listener para crear album
	 * @return Listener para creat album
	 */
	private ActionListener createCreateAlbumListener(){
		return new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				DataController.getInstance().createAlbumDialog();
			}

		};
	}

	/**
	 * Crea el listener para cerrar album
	 * @return Listener para cerrer album
	 */
	private ActionListener createCloseAlbumListener(){
		return new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				AlbumsGrid ag = AlbumsGrid.getInstance();
				int selection = JOptionPane.showOptionDialog(
						null, 
						"¿Está seguro que quiere cerrar el album?" , 
						"Cerrar album", 
						JOptionPane.YES_NO_OPTION,
						JOptionPane.PLAIN_MESSAGE, 
						null ,
						new String[] {"Si" , "No"}, 
						"No");
				if(selection == JOptionPane.OK_OPTION){
					DataController.getInstance().closeAlbum(ag.getSelected().getAlbumName());
					ag.removeAlbum(ag.getSelected().getAlbumName());
				}
			}

		};
	}

	/**
	 * Crea el listener para eliminar album
	 * @return Listener para eliminar album
	 */
	private ActionListener createDeleteAlbumListener(){
		return new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				AlbumsGrid ag = AlbumsGrid.getInstance();
				int selection = JOptionPane.showOptionDialog(
						null, 
						"¿Está seguro que quiere eliminar el album?" , 
						"Eliminar album", 
						JOptionPane.YES_NO_OPTION,
						JOptionPane.PLAIN_MESSAGE, 
						null ,
						new String[] {"Si" , "No"}, 
						"No");
				if(selection == JOptionPane.OK_OPTION){
					DataController.getInstance().closeAlbum(ag.getSelected().getAlbumName());
					ag.removeAlbum(ag.getSelected().getAlbumName());
				}
			}

		};
	}

	/**
	 * Crea el listener para quitar item de album
	 * @return Listener para quitar item de album
	 */
	private ActionListener createRemoveFromAlbumListener(){
		return new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				int selection = JOptionPane.showOptionDialog(
						null, 
						"¿Está seguro que quiere quitar el archivo del album?" , 
						"Quitar archivo", 
						JOptionPane.YES_NO_OPTION,
						JOptionPane.PLAIN_MESSAGE, 
						null ,
						new String[] {"Si" , "No"}, 
						"No");
				if(selection == JOptionPane.OK_OPTION)
					AlbumContentPanel.getInstance().transferItem(null);
			}

		};
	}

	/**
	 * Crea el listener para eliminar item de album
	 * @return Listener para eliminar item de album
	 */
	private ActionListener createDeletePhotoListener(){
		return new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				int selection = JOptionPane.showOptionDialog(
						null, 
						"¿Está seguro que quiere eliminar el archivo?" , 
						"Eliminar archivo", 
						JOptionPane.YES_NO_OPTION,
						JOptionPane.PLAIN_MESSAGE, 
						null ,
						new String[] {"Si" , "No"}, 
						"No");
				if(selection == JOptionPane.OK_OPTION)
					AlbumContentPanel.getInstance().deleteItem();
			}

		};
	}

	/**
	 * Crea el listener para transferir elemento a album
	 * @return Listener para transferir elemento a album
	 */
	private ActionListener createTransferToAlbumListener(){
		return new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
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

		};
	}

	/**
	 * Crea el listener para rotar a la izquierda una foto
	 * @return Listener para rotar a la izquierda una foto
	 */
	private ActionListener createRotateLListener(){
		return new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				PhotoPanel.getInstance().rotateImege(-1);
			}

		};
	}
	/**
	 * Crea el listener para rotar a la derecha una foto
	 * @return Listener para rotar a la derecha una foto
	 */
	private ActionListener createRotateRListener(){
		return new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				PhotoPanel.getInstance().rotateImege(1);
			}

		};
	}

	/**
	 * Crea el listener par copiar archivo a una locacion especifica
	 * @return listener par copiar archivo a una locacion especifica
	 */
	private ActionListener createCopyFileToLocation(){
		return new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				//Obtiene el item de librería
				LibraryItem li = MediaViewer.getInstance().getActualLibraryItem();
				if(li == null){
					System.err.println("Archivo nulo no puede ser copiado");
					return;
				}

				//Copia el archivo
				DataController.getInstance().copyFileToExtern(li);
			}

		};
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

}
