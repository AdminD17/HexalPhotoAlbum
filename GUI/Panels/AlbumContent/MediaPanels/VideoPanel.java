package HexalPhotoAlbum.GUI.Panels.AlbumContent.MediaPanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ExtraClass.OSDirectories;
import HexalPhotoAlbum.Data.LibraryItem;
import HexalPhotoAlbum.GUI.Images.ImageGUILoader;

/**
 * Panel para mostrar videos
 * 
 * @author David Giordana
 *
 */
public class VideoPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = -7091809550851895929L;

	//Boton de reproducción
	private JButton play;

	//Etiqueta de advertencia
	private JLabel lab;

	//Contiene el item de la librería
	private LibraryItem li;

	//Clase para conocimiento del sistema operativo
	private OSDirectories os;

	//contiene la instancia de la clase
	private static VideoPanel ins;

	/**
	 * Obtiene una instancia unica de la clase
	 * @return intancia de la clase
	 */
	public static VideoPanel getInstance(){
		if(ins == null){
			ins = new VideoPanel();
		}
		return ins;
	}

	/**
	 * Constructor de la clase
	 */
	private VideoPanel(){
		//instancia los componetnes de la clase
		play = new JButton(ImageGUILoader.getImage("play.png"));
		lab = new JLabel();
		os = OSDirectories.getInstance();

		//Setea los componentes del panel
		this.setLayout(new BorderLayout());
		play.setBorder(null);
		play.setToolTipText("Reproducir");
		
		play.addActionListener(this);
		lab.setText("En la actual versión la aplicacíon no tiene soporte nativo para videos, esta función será incorporada en futuras versiones");
		
		//Crea el panel para el boton reproducir
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill= GridBagConstraints.NONE;
		panel.add(play , gbc);
		panel.setBackground(Color.decode("#4A4A4A"));

		//Agrega los componentes al panel
		this.add(panel, BorderLayout.CENTER);
		this.add(lab, BorderLayout.SOUTH);
	}

	/**
	 * Muestra un video en base un icono de archivo
	 * @param item Item de librería a mostrar 
	 */
	public void showMedia(LibraryItem item) {
		li = item;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String com = "";
		/*if(os.isWindows()){}
		else*/ if(os.isMac()){
			com = "open ";
		}
		else if(os.isUnix()){
			com = "xdg-open ";
		}
		try {
			Runtime.getRuntime().exec(com + li.getAbsoluteFilePath());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
