package HexalPhotoAlbum.Data;
import java.io.File;

import javax.swing.JOptionPane;

import HexalPhotoAlbum.GUI.Panels.AlbumsPanel;
import HexalPhotoAlbum.GUI.Panels.AlbumContent.AlbumContentPanel;

/**
 * Worker para importar archivos al sistema
 * 
 * @author David Giordana
 *
 */
public class ImportWorker extends Thread{

	//Informacion interna del worker
	private File[] files;
	private String album;

	/**
	 * Genera una instancia del worker
	 * @param files Arreglo de archivos a importar
	 * @param album Nombre del album a importar
	 * @return instancia de la worker
	 */
	public static ImportWorker getWorker(File[] files , String album){		
		return new ImportWorker(files , album);
	}

	/**
	 * Constructor de la clase
	 * @param files Arreglo de archivos a importar
	 * @param album Nombre del album a importar
	 * @return instancia de la worker
	 */
	private ImportWorker(File[] files ,String album){
		this.files = files;
		this.album = album;
	}

	@Override
	public void run(){
		//Importa los archivos
		boolean keep = askKeep();
		importMedia(keep);

		//Actualiza la informacion de la interfaz gráfica
		if(AlbumsPanel.getInstance().isContentShow()){
			AlbumContentPanel.getInstance().updateUIData();
		}
		
		//Finaliza el hilo
		try {
			files = null;
			album = null;
			this.join();
		} catch (InterruptedException e) {e.printStackTrace();}
	}

	/**
	 * Importa los archivos del usuario
	 * @param b india si los archivos deben ser conservados
	 */
	private void importMedia(boolean b){
		DataController.getInstance().addPhotos(files, album, b);
	}

	/**
	 * Pregunta si se deben mantener los archivos luego de la copia
	 * @return True si se deben mantener los archivos, False en caso de querer eliminarlos
	 */
	private boolean askKeep(){
		int selection = JOptionPane.showOptionDialog(
				null, 
				"¿Desea conservar los archivos originales?",
				"Seleccione una opcón", 
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null ,
				new Object[]{"Conservar Original" , "Eliminar original"} ,
				"Eliminar original");
		return selection == JOptionPane.YES_OPTION;
	}

}
