package HexalPhotoAlbum.Data;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

/**
 * Clase encargada de almacenar la informacion de un elemento de la 
 * libreria de la aplicacion
 * 
 * @author David Giordana
 *
 */
public class LibraryItem {

	/**
	 * ---- ATTRIBUTES
	 */

	//Arreglo de extensiones de imagenes
	public static final String[] IMAGE_FORMATS = {
		"gif" ,
		"png" ,
		"jpeg" ,
		"bmp" ,
		"jpg" ,
		"wbmp"
	};

	//Arreglo de extensiondes de videos soportados
	public static final String[] VIDEO_FORMATS = {
		"mp4" ,
		"mov" ,
		"3gp"
	};

	//tipo de dato no soportado
	public static final int UNSUPPORTED_FORMAT = -1;

	//tipo de dato imagen
	public static final int IMAGE_TYPE = 0;

	//tipo de dato video
	public static final int VIDEO_TYPE = 1;

	//nombre del item
	private String name;

	//extension del archivo
	private String extension;

	//album de pertenencia
	private String albumName;

	//tipo de dato
	private int dataType;

	/**
	 * ---- CONSTRUCTORS
	 */

	/**
	 * Cosntructor de la clase
	 * @param file Archivo asociado al item
	 * @param album nombre de album de pertenencia , null en caso de no pertenecer a ninguno
	 */
	public LibraryItem(File file , String album){
		this.name = FilenameUtils.getBaseName(file.getName());
		this.extension = FilenameUtils.getExtension(file.getName());
		this.setAlbumName(album);
		this.dataType = calculeType(file);
	}

	/**
	 * ---- GETTERS & SETTERS
	 */

	/**
	 * setea el nombre del album
	 * @param albumName nombre del album
	 */
	public void setAlbumName(String albumName){
		this.albumName = albumName;
	}

	/**
	 * retorna el nombre del objeto asociadio
	 * @return nombre de objeto asociado
	 */
	public String getName(){
		return name;
	}

	/**
	 * retorna la extension del archivo asociado
	 * @return extension del archivo asociado
	 */
	public String getExtension(){
		return extension;
	}

	/**
	 * retorna el nombre del album
	 * @return nombre del album
	 */
	public String getAlbumName(){
		return albumName;
	}

	/**
	 * retorna el indice de tipo de dato
	 * @return indice de tipo de dato
	 */
	public int getDataType(){
		return dataType;
	}

	/**
	 * ---- METHODS
	 */

	/**
	 * Retorna la ruta absoluta del archivo asociado
	 * @return ruta absoluta del archivo asociado
	 */
	public String getAbsoluteFilePath(){		
		String alb = albumName == null ? "" : albumName + File.separator;
		String ret = DataController.MEDIA_FOLDER + 
				alb +
				getName() +
				FilenameUtils.EXTENSION_SEPARATOR_STR +
				getExtension();
		return ret;
	}

	/**
	 * Retorna el archivo asociado al item de libraría
	 * @return Archivo asociado al item de librería
	 */
	public File getFile(){
		return new File(getAbsoluteFilePath());
	}

	/**
	 * retorna el tipo de dato
	 * @param file Archivo a abalizar
	 * @return tipo de dato
	 */
	public static int calculeType(File file){
		String ext = FilenameUtils.getExtension(file.getName()).toLowerCase();

		//Analiza si es imagen soportado
		for(String str : IMAGE_FORMATS){
			if(str.equals(ext)){
				return IMAGE_TYPE;
			}
		}

		//Analiza si es video soportado
		for(String str : VIDEO_FORMATS){
			if(str.equals(ext)){
				return VIDEO_TYPE;
			}
		}

		//Archivo no soportado
		return UNSUPPORTED_FORMAT;
	}

	/**
	 * Retorna true en caso de que el formato sea aceptado
	 * @param file archivo a analizar
	 * @return true si se acepta el formato
	 */
	public static boolean isSupportedFile(File file){
		return file.isDirectory() ? false : UNSUPPORTED_FORMAT != calculeType(file);
	}

}
