import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.InternationalFormatter;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import javax.swing.JFormattedTextField;
import java.text.ParseException;

import java.awt.Font;
import java.awt.Frame;
import java.awt.FlowLayout;

import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import javax.swing.ImageIcon;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.FileDialog;
import java.awt.Frame;
import javax.swing.SwingUtilities;
import java.io.File;
import java.io.FilenameFilter;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ModalAgregar extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextField txtNombre;
	private JTextField txtPrecio;
	private JTextField txtProveedor;
	private JTextField txtCategoria;
	private JTextField txtCantidad;
	private JTextField txtCodigo;
	private JLabel lblImagePreview;
	private JTextField txtImagePath;
	private String selectedImagePath;
	private String savedImageName;
	private ProductosDAO producto = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ModalAgregar dialog = new ModalAgregar();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setSize(650, 750);
					dialog.setResizable(false);
					dialog.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the dialog.
	 */
	public ModalAgregar() {
		producto = new ProductosDAO();
		setModal(true);
		setSize(650, 750);
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		JPanel pnlSuperior = new JPanel();
		pnlSuperior.setBackground(new Color(145, 15, 17));
		pnlSuperior.setBounds(0, 0, 634, 50);
		getContentPane().add(pnlSuperior);
		pnlSuperior.setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("Nuevo Producto\t\t\t");
		lblNewLabel.setFont(new Font("Century Gothic", Font.BOLD, 22));
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		pnlSuperior.add(lblNewLabel, BorderLayout.CENTER);
		
		JPanel pnlDatos = new JPanel();
		pnlDatos.setBounds(10, 61, 614, 361);
		getContentPane().add(pnlDatos);
		pnlDatos.setLayout(null);
		pnlDatos.setBorder(BorderFactory.createTitledBorder( BorderFactory.createLineBorder(Color.BLACK), "Datos del Producto", 1, 2, new Font("Century Gothic", Font.BOLD, 18)));
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setFont(new Font("Century Gothic", Font.BOLD, 16));
		lblNombre.setBounds(20, 37, 83, 14);
		pnlDatos.add(lblNombre);
		
		txtNombre = new JTextField();
		txtNombre.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		txtNombre.setBounds(152, 34, 245, 20);
		pnlDatos.add(txtNombre);
		txtNombre.setColumns(10);
		
		JLabel lblDescripcion = new JLabel("Descripción: ");
		lblDescripcion.setFont(new Font("Century Gothic", Font.BOLD, 16));
		lblDescripcion.setBounds(20, 62, 137, 26);
		pnlDatos.add(lblDescripcion);
		
		JTextArea txtDescripcion = new JTextArea();
		txtDescripcion.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		txtDescripcion.setBounds(152, 65, 440, 96);
		pnlDatos.add(txtDescripcion);
		
		JLabel lblPrecio = new JLabel("Precio: ");
		lblPrecio.setFont(new Font("Century Gothic", Font.BOLD, 16));
		lblPrecio.setBounds(20, 175, 137, 14);
		pnlDatos.add(lblPrecio);
		
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
		decimalFormat.setMinimumFractionDigits(2);
		decimalFormat.setMaximumFractionDigits(2);
		NumberFormatter priceFormatter = new NumberFormatter(decimalFormat);
		priceFormatter.setValueClass(Double.class);
		priceFormatter.setMinimum(0.0);
		priceFormatter.setAllowsInvalid(false);
		priceFormatter.setCommitsOnValidEdit(true);
		txtPrecio = new JFormattedTextField(priceFormatter);
		txtPrecio.setText("0.00");
		txtPrecio.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		txtPrecio.setBounds(152, 172, 123, 20);
		pnlDatos.add(txtPrecio);
		
		JLabel lblProveedor = new JLabel("Proveedor:");
		lblProveedor.setFont(new Font("Century Gothic", Font.BOLD, 16));
		lblProveedor.setBounds(20, 242, 137, 14);
		pnlDatos.add(lblProveedor);
		
		txtProveedor = new JTextField();
		txtProveedor.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		txtProveedor.setColumns(10);
		txtProveedor.setBounds(152, 239, 245, 20);
		pnlDatos.add(txtProveedor);
		
		txtCategoria = new JTextField();
		txtCategoria.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		txtCategoria.setColumns(10);
		txtCategoria.setBounds(152, 275, 245, 20);
		pnlDatos.add(txtCategoria);
		
		JLabel lblCategoria = new JLabel("Categoria: ");
		lblCategoria.setFont(new Font("Century Gothic", Font.BOLD, 16));
		lblCategoria.setBounds(20, 275, 137, 20);
		pnlDatos.add(lblCategoria);
		
		JLabel lblCantidad = new JLabel("Cantidad: ");
		lblCantidad.setFont(new Font("Century Gothic", Font.BOLD, 16));
		lblCantidad.setBounds(20, 200, 137, 20);
		pnlDatos.add(lblCantidad);
		
		
		NumberFormatter qtyFormatter = new NumberFormatter(NumberFormat.getIntegerInstance());
		qtyFormatter.setValueClass(Integer.class);
		qtyFormatter.setMinimum(0);
		qtyFormatter.setAllowsInvalid(false);
		qtyFormatter.setCommitsOnValidEdit(true);
		txtCantidad = new JFormattedTextField(qtyFormatter);
		txtCantidad.setText("1");
		txtCantidad.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		txtCantidad.setBounds(152, 203, 123, 20);
		pnlDatos.add(txtCantidad);
		
		JLabel lblCodigo = new JLabel("Código: ");
		lblCodigo.setFont(new Font("Century Gothic", Font.BOLD, 16));
		lblCodigo.setBounds(20, 314, 137, 20);
		pnlDatos.add(lblCodigo);
		
		MaskFormatter maskCodigo;
		try {
		    maskCodigo = new MaskFormatter("######"); // 6 dígitos obligatorios
		    txtCodigo = new JFormattedTextField(maskCodigo);
		} catch (ParseException e) {
		    e.printStackTrace();
		    txtCodigo = new JTextField(); // Fallback en caso de error
		}
		txtCodigo.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		txtCodigo.setBounds(152, 314, 245, 20);
		pnlDatos.add(txtCodigo);
		
		JPanel pnlImagen = new JPanel();
		pnlImagen.setBounds(10, 433, 614, 234);
		getContentPane().add(pnlImagen);
		pnlImagen.setLayout(null);
		pnlImagen.setBorder(BorderFactory.createTitledBorder( BorderFactory.createLineBorder(Color.BLACK), "Imagen", 1, 2, new Font("Century Gothic", Font.BOLD, 18)));
		
		JLabel lblRuta = new JLabel("Ruta:");
		lblRuta.setFont(new Font("Century Gothic", Font.BOLD, 16));
		lblRuta.setBounds(20, 40, 83, 20);
		pnlImagen.add(lblRuta);

		txtImagePath = new JTextField();
		txtImagePath.setEditable(false);
		txtImagePath.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		txtImagePath.setBounds(80, 40, 350, 25);
		pnlImagen.add(txtImagePath);

		JButton btnExaminar = new JButton("Examinar...");
		btnExaminar.setFont(new Font("Century Gothic", Font.BOLD, 14));
		btnExaminar.setBounds(450, 40, 120, 25);
		btnExaminar.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	seleccionarImagen();
		    }
		});
		pnlImagen.add(btnExaminar);

		// Panel para la vista previa de la imagen
		JPanel previewPanel = new JPanel();
		previewPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		previewPanel.setBounds(150, 80, 300, 140);
		previewPanel.setLayout(new BorderLayout());
		pnlImagen.add(previewPanel);

		lblImagePreview = new JLabel("Sin imagen seleccionada", SwingConstants.CENTER);
		lblImagePreview.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		previewPanel.add(lblImagePreview, BorderLayout.CENTER);
		
		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nombreInput = txtNombre.getText();
				String descInput = txtDescripcion.getText();
				Double precioInput = Double.parseDouble(txtPrecio.getText());
				int cantidadInput = Integer.parseInt(txtCantidad.getText());
				String proveedorInput = txtProveedor.getText();
				String categoriaInput = txtCategoria.getText();
				String codigoInput = txtCodigo.getText();
				
				String imagenPath = null;
		        if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
		            imagenPath = guardarImagen(selectedImagePath, codigoInput);
		        }
				
				boolean resultado = producto.insertar(nombreInput, descInput, precioInput, cantidadInput, proveedorInput, categoriaInput, codigoInput, imagenPath);
				
				if (resultado) {
		            JOptionPane.showMessageDialog(null, "Producto guardado con éxito",  "Éxito", JOptionPane.INFORMATION_MESSAGE);
		            dispose();
		        } else {
		            JOptionPane.showMessageDialog(null, "Error al guardar el producto", "Error", JOptionPane.ERROR_MESSAGE);
		        }
			}
		});
		btnGuardar.setFont(new Font("Century Gothic", Font.BOLD, 14));
		btnGuardar.setBounds(184, 678, 106, 23);
		getContentPane().add(btnGuardar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancelar.setFont(new Font("Century Gothic", Font.BOLD, 14));
		btnCancelar.setBounds(338, 678, 106, 23);
		getContentPane().add(btnCancelar);

		
		setVisible(true);
	}
	
	/**
	 * Método par abrir el explorador de archivos y seleccionar la imagen
	 */
	private void seleccionarImagen() {
	    Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);

	    FileDialog fileDialog = new FileDialog(parent, "Seleccionar Imagen", FileDialog.LOAD);
	    fileDialog.setDirectory(System.getProperty("user.home"));
	    fileDialog.setVisible(true);

	    String dir = fileDialog.getDirectory();
	    String file = fileDialog.getFile();
	    if (dir != null && file != null) {
	    	if (!file.toLowerCase().matches(".*\\.(jpg|jpeg|png|gif)$")) {
	            JOptionPane.showMessageDialog(null, "Formato no permitido (permitidos: .jpg, .jpeg, .png)", "Error", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
	        File selectedFile = new File(dir, file);
	        selectedImagePath = selectedFile.getAbsolutePath();
	        txtImagePath.setText(selectedImagePath);
	        mostrarVistaPrevia(selectedFile);
	    }
	}

	/**
	 * Método que muestra la vista previa de la imagen
	 */
	private void mostrarVistaPrevia(File file) {
	    try {
	        Image originalImage = ImageIO.read(file);
	        
	        if (originalImage != null) {
	            int previewWidth = 290;
	            int previewHeight = 130;
	            Image scaledImage = originalImage.getScaledInstance(previewWidth, previewHeight, Image.SCALE_SMOOTH);
	            
	            lblImagePreview.setIcon(new ImageIcon(scaledImage));
	            lblImagePreview.setText("");
	        }
	    } catch (IOException ex) {
	        lblImagePreview.setIcon(null);
	        lblImagePreview.setText("Error al cargar la imagen");
	        ex.printStackTrace();
	    }
	}
	
	/**
	 * Método para guardar la imagen y retorna su ruta
	 */
	private String guardarImagen(String rutaOrigen, String codigoProducto) {
	    try {
	        String directorioProyecto = System.getProperty("user.dir");
	        Path rutaCarpetaImagenes = Paths.get(directorioProyecto, "imagenes");
	        
	        if (!Files.exists(rutaCarpetaImagenes)) {
	            Files.createDirectory(rutaCarpetaImagenes);
	        }
	        
	        File archivoOrigen = new File(rutaOrigen);
	        String nombreArchivo = archivoOrigen.getName();
	        String extension = nombreArchivo.substring(nombreArchivo.lastIndexOf('.'));
	        
	        String nuevoNombre = "producto_" + codigoProducto + extension;
	        Path rutaDestino = Paths.get(rutaCarpetaImagenes.toString(), nuevoNombre);
	        
	        Files.copy(Paths.get(rutaOrigen), rutaDestino, StandardCopyOption.REPLACE_EXISTING);
	        
	        return "imagenes/" + nuevoNombre;
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Error al guardar la imagen: " + e.getMessage(), 
	                                     "Error", JOptionPane.ERROR_MESSAGE);
	        return null;
	    }
	}
}
