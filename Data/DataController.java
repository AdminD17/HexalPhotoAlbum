package HexalPhotoAlbum.Data;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import ExtraClass.GUI.JTextFieldHint;
import HexalPhotoAlbum.GUI.Panels.AlbumsPanel;

/**
 * Contiene la lista de estructuras PhotoData
 * para la administracion de los elementos de la libraria
 * 
 * @author David Giordana
 *
 */
public class DataController {

	/**
	 * ---- CONSTANTS
	 */

	//nombre de la carpeta de la aplicación
	public static final String APP_NAME = "HexalPhoto";

	//nombre de la carpeta de archivos de imagen
	public static final String MEDIA_FOLDER = 
			System.getProperty("user.home") + File.separator +
			"Hexalsoft" + File.separator + 
			APP_NAME + File.separator + 
			"Library" + File.separator +
			"Media" + File.separator;

	/**
	 * ---- ATTRIBUTES
	 */

	//lista de objetos que no estan en ningun album
	private ArrayList<LibraryItem> disorderedMedia;

	//tabla de albumes
	private Hashtable<String , ArrayList<LibraryItem>> albumsTable;

	//lista de nombre de albumes
	private ArrayList<String> albums;

	/**
	 * ---- CONSTRUCTOR
	 */

	//almacena la instancia de la clase
	private static DataController INS;

	/**
	 * retorna una instancia unica de la clase
	 * @return instancia de la clase
	 */
	public static DataController getInstance(){
		if(INS == null){
			INS = new DataController();
		}
		return INS;
	}

	/**
	 * Constructor de la clase
	 */
	private DataController(){
		disorderedMedia = new ArrayList<LibraryItem>();
		albumsTable = new Hashtable<String , ArrayList<LibraryItem>>();
		albums = new ArrayList<String>();
	}

	/**
	 * ---- GETTERS & SETTERS
	 */

	/**
	 * Retorna la lista de fotos desclasificadas
	 * @return lista de fotos desclasificadas
	 */
	public ArrayList<LibraryItem> getDisorderedAlbum(){
		return disorderedMedia;
	}

	/**
	 * retorna la lista de nombres de albumes creados
	 * @return lista de albumes
	 */
	public ArrayList	<String> getAlbumsList(){
		return albums;
	}

	/**
	 * Retorna la lista de albumes
	 * @return lista de albumes
	 */
	public Hashtable <String , ArrayList<LibraryItem>> getAlbumsTable(){
		return albumsTable;
	}

	/**
	 * Retorna la lista de items de libreria de un album en particular
	 * @param albumName nombre del album
	 * @return lista de items de un album
	 */
	public ArrayList<LibraryItem> getAlbumContent(String albumName){
		if(albumName == null){
			return disorderedMedia;
		}
		ArrayList <LibraryItem> lib = getAlbumsTable().get(albumName);
		return lib == null ? disorderedMedia : lib;
	}

	/**
	 * ---- METHODS
	 */

	/**
	 * Hace las tareas basicas de inicializacion de la aplicacion
	 */
	public void checkInstall(){
		//Crea las carpetas necesarias para le ejecucion del programa
		checkCreate(MEDIA_FOLDER);

		//Abre la librería
		openLibrary();
	}

	/**
	 * Comprueba si una carpeta existe, en caso de no existir la crea
	 * @param path Ruta de la carpeta a comprobar
	 */
	private void checkCreate(String path){
		File f = new File(path);
		if(!f.exists()){
			f.mkdirs();
		}
	}

	/**
	 * Abre la libraría de imágenes
	 */
	public void openLibrary(){
		//Carga la librería
		createAlbum(null);
		File[] library = new File(MEDIA_FOLDER).listFiles();
		for(File libFile : library){
			if(libFile.isDirectory()){
				String album = libFile.getName();
				createAlbum(album);
				File[] albumFiles = libFile.listFiles();
				for(File albFile : albumFiles){
					if(LibraryItem.isSupportedFile(albFile)){
						getAlbumContent(album).add(new LibraryItem(albFile, album));
					}
				}
			}
			else if(LibraryItem.isSupportedFile(libFile)){
				disorderedMedia.add(new LibraryItem(libFile, null));
			}
		}
	}

	/**
	 * ---- PHOTOS & ALBUM METHODS
	 */

	/**
	 * Permite saber si el nombre de album ingresado está disponible
	 * @param str nombre del album a comprobar
	 * @return true si el nombre esta disponible, false en caso contrario
	 */
	public boolean isAvailableAlbum(String str){
		if(str == null || str == ""){ return false; }
		return !this.getAlbumsList().contains(str);	
	}

	/**
	 * Genera el nombre de un archivo para agregarlo
	 * @return nombre generado
	 */
	private String generateName(){
		return "HPA-" + System.currentTimeMillis();
	}

	/**
	 * Agrega un objeto a la libreria
	 * @param f archivo a agregar
	 * @param album nombre del album
	 * @param keepOriginal Indica si se mantiene el archivo original
	 */
	public void addPhoto(File file , String album , boolean keepOriginal){
		//En caso de recibir una carpeta
		if(file.isDirectory()){
			File[] list = file.listFiles();
			for(File f : list){
				addPhoto(f , album , keepOriginal);
			}
		}

		//En caso de trabajar con un archivo, comprueba de poder procesarlo y procede
		else if(LibraryItem.isSupportedFile(file)){
			try{
				//Copia archivo a libraría
				String alb = album == null ? "" : (album + File.separator);
				String folderPath = MEDIA_FOLDER + alb;
				FileUtils.copyFileToDirectory(file, new File(folderPath));

				//Renombra archivo y lo agrega a la librería en ejecución
				File f = new File(folderPath + file.getName());
				getAlbumContent(album).add(new LibraryItem(renameFile(f), album));

				//Remueve el archivo original en caso de ser necesario
				if(!keepOriginal){
					FileUtils.deleteQuietly(file);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	/**
	 * Renombra un archivo
	 * @param file Archivo a Renombrar
	 * @return Archivo renombrado
	 */
	private File renameFile(File file){
		File ret = null;
		try{
			String newName = generateName() + FilenameUtils.EXTENSION_SEPARATOR_STR + FilenameUtils.getExtension(file.getName());
			File newFile = new File(file.getParent() + File.separator + newName);
			file.renameTo(newFile);
			ret = newFile;
		}catch(Exception e){
			System.err.println("No existe el archivo " + file.getAbsolutePath());
		}
		return ret;
	}

	/**
	 * Agrega una lista de objetos a la libreria
	 * @param arr archivo a agregar
	 * @param album nombre del album
	 * @param keepOriginal Indica si se mantiene el archivo original
	 */
	public void addPhotos(File[] arr , String album , boolean keepOriginal){
		for(File f : arr){
			addPhoto(f , album , keepOriginal);
		}
	}

	/**
	 * Agrega una lista de objetos a la libreria
	 * @param l archivo a agregar
	 * @param album nombre del album
	 * @param keepOriginal Indica si se mantiene el archivo original
	 */
	public void addPhotos(List<File> l , String album , boolean keepOriginal){
		for(File f : l){
			addPhoto(f , album , keepOriginal);
		}
	}

	/**
	 * Copia un elemento de un album a otro
	 * @param destiny nombre del album destino
	 * @param li Item de librería a trasladar
	 */
	public void copyToAlbum(String destiny , LibraryItem li){
		//Evita copiar en caso de no ser necesario
		if(li.getAlbumName() == destiny){
			return;
		}

		//Mueve el archivo
		File originalFile = li.getFile();
		String dPath = MEDIA_FOLDER + (destiny == null ? "" : destiny + File.separator);		
		try{
			//Transfiere el archivo
			FileUtils.copyFileToDirectory(li.getFile(), new File(dPath));
			FileUtils.deleteQuietly(originalFile);		

			//Actualiza la informacion del programa
			ArrayList<LibraryItem> list = getAlbumContent(li.getAlbumName());
			li.setAlbumName(destiny);
			list.remove(li);
			list = getAlbumContent(destiny);
			list.add(li);
		}catch(Exception e){
			System.err.println(e.getMessage() + "- Error al mover archivo");
		}
	}

	/**
	 * Cierra un album vaciando y eliminando la entrada sin afectar a los items
	 * @param album nombre del album a cerrar
	 */
	public void closeAlbum(String album){
		ArrayList<LibraryItem> list = getAlbumContent(album);
		while(!list.isEmpty())
			copyToAlbum(null , list.get(0));		
		FileUtils.deleteQuietly(new File(MEDIA_FOLDER + album));
		this.getAlbumsTable().remove(album);
		this.getAlbumsList().remove(album);
	}

	/**
	 * Elimina un item de la librería
	 * @param li Elemento a eliminar
	 */
	public void removeLibraryItem(LibraryItem li){
		ArrayList<LibraryItem> list = getAlbumContent(li.getAlbumName());
		list.remove(li);
		li.getFile().delete();
	}

	/**
	 * Elimina un album junto con todos sus elementos, enviandolos a la carpeta de eliminados
	 * (Actualmente se eliminan completamente)
	 * @param album nombre del album a eliminar
	 */
	public void removeAlbum(String album){
		ArrayList<LibraryItem> list = getAlbumContent(album);
		while(!list.isEmpty()){
			removeLibraryItem(list.get(0));
		}
		FileUtils.deleteQuietly(new File(MEDIA_FOLDER + album));
		this.getAlbumsTable().remove(album);
		this.getAlbumsList().remove(album);
	}

	/**
	 * Agrega nuevos items a la librería
	 * @param album Nombre del album
	 *
	public void addFilesToLibraryDialog(String album){
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setMultiSelectionEnabled(true);
		int selected = jfc.showOpenDialog(null);
		if(selected == JFileChooser.APPROVE_OPTION){
			File[] list = jfc.getSelectedFiles();
			for(File f : list){
				this.addPhoto(f, album, true*askKeep()*);
			}
		}
	}*/

	boolean temp789;

	/**
	 * Crea un album
	 * @return true si se logró crear el album, false en caso contrario
	 */
	public boolean createAlbumDialog(){
		JPanel panel = new JPanel(new BorderLayout());
		JTextFieldHint tf = new JTextFieldHint();
		String in = null;
		temp789 = false;
		tf.setHint("Nobre del album");
		tf.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(((JTextFieldHint)e.getSource()).getContent().trim().length() > 0){
					SwingUtilities.getWindowAncestor(tf).dispose();
					temp789 = true;
				}
			}
		});
		panel.add(new JLabel("Ingrese el nombre del album"), BorderLayout.NORTH);
		panel.add(tf, BorderLayout.SOUTH);
		int selection = JOptionPane.showOptionDialog(
				null, 
				panel , 
				"Crear album", 
				JOptionPane.YES_NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, 
				null ,
				new String[] {"Crear Album" , "Cancelar"}, 
				"Crear Albums");
		in = tf.getContent();
		//cancelacion de entrada
		if(selection != JOptionPane.OK_OPTION && !temp789){
			return false;
		}
		//nombre no disponible
		if(!this.isAvailableAlbum(in)){
			JOptionPane.showMessageDialog(
					null,
					"El nombre \"" + in + "\" No puede ser usado, intente con otro nombre.", 
					"Error de entrada", 
					JOptionPane.ERROR_MESSAGE,
					null);
			return false;
		}
		//crea el album
		else{
			createAlbum(in);
			return true;
		}
	}

	/**
	 * Crea un album
	 * @param name Nombre de la librería
	 */
	private void createAlbum(String name){
		if(name != null){
			this.checkCreate(MEDIA_FOLDER + name); 
			this.getAlbumsTable().put(name, new ArrayList<LibraryItem>());
			this.getAlbumsList().add(name);
		}
		AlbumsPanel.getInstance().addAlbum(name);
	}

}
