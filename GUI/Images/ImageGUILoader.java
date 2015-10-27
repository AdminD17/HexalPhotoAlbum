package HexalPhotoAlbum.GUI.Images;

import javax.swing.ImageIcon;

import ExtraClass.ImageResizer;

/**
 * Clase encargada de cargar imágenes de la interfaz gráfoca de la aplicación
 * 
 * @author David Giordana
 *
 */
public class ImageGUILoader {

	//Ruta de la Carpeta de imagenes
	private static final String PATH = "/HexalPhotoAlbum/GUI/Images/";

	/**
	 * Retorna una imagen de la GUI
	 * @param image Nombre de la imagen a retornar (incluye extension)
	 * @return Imagen solicitada, null en caso de no existir
	 */
	public static ImageIcon getImage(String image){
		try{
			return new ImageIcon(ImageGUILoader.class.getResource(PATH + image));
		}catch(Exception e){
			System.err.println(e.getMessage());
			return null;
		}
	}

	/**
	 * Retorna una imagen de la interfaz redimensionada
	 * @param image Nombre de la imagen a retornar (incluye extension)
	 * @param width Ancho deseado
	 * @param height Alto deseado
	 * @return Imagen solicitada con el tamaño cambiado, null en caso de no existir
	 */
	public static ImageIcon getResizedImage(String image ,  int width , int height){
		ImageIcon i = getImage(image);
		return i == null ? i : ImageResizer.resize(i , width , height);
	}

}
