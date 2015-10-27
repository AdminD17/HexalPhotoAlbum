package HexalPhotoAlbum;

import HexalPhotoAlbum.GUI.PhotoAlbumFrame;

/**
 * Lanza el programa de administración de fotos
 * 
 * @author David Giordana
 *
 */
public class HexalPhotoAlbum_Main {

	/**
	 * Metodo para lanzar el programa
	 * @param args Argumentos pasados por consola
	 */
	public static void main(String[] args) {
		PhotoAlbumFrame frame = PhotoAlbumFrame.getInstance();
		frame.showApp();
	}

}
