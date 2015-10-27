package HexalPhotoAlbum.GUI.Panels.AlbumContent;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import HexalPhotoAlbum.GUI.Panels.AlbumsPanel;

/**
 * Panel de opciones para aplicar sobre la imagen o video que se
 * esta visualizando
 * 
 * @author David Giordana
 *
 */
public class OptionsPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 219571461207098908L;

	//boton de retroceso
	private JButton back;

	//Label indicador de paginas
	private JLabel page;

	//Contiene la instancia de la clase
	private static OptionsPanel INS;

	/**
	 * Retorna una instancia única de la clase
	 * @return instancia de la clase
	 */
	public static OptionsPanel getInstance(){
		if(INS == null){
			INS = new OptionsPanel();
		}
		return INS;
	}

	/**
	 * Constructor de la clase
	 * @param ov Panel para mostrar objeto en pantalla
	 */
	private OptionsPanel(){
		//Instancia los componentes
		createBackButon();
		createPageLabel();

		//Setea los componentes
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setBorder(new LineBorder(Color.GRAY));
		this.setBackground(Color.decode("#D7D7D7"));
	}

	/**
	 * Crea el boton retroceder
	 */
	private void createBackButon(){
		back = new JButton("<-");
		back.setToolTipText("Volver");
		back.addActionListener(this);
		this.add(back);
	}

	/**
	 * Crea el label indicador de página
	 */
	private void createPageLabel(){
		page = new JLabel();
		page.setFont(new Font("Arial" , Font.BOLD , 14));
		this.add(page);
	}

	/**
	 * Setea los contadores de multimedia
	 * @param total Cantidad de elementos del album
	 * @param index Indice del elemento mostrado {1...n}
	 */
	public void setCounters(int total , int index){
		page.setText("Item " + index + " de " + total);
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(back)){
			AlbumsPanel.getInstance().showGrid();
		}
	}

}
