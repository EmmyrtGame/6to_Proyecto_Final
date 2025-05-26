import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

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
		pnlDatos.setBounds(10, 61, 614, 383);
		getContentPane().add(pnlDatos);
		pnlDatos.setLayout(null);
		pnlDatos.setBorder(BorderFactory.createTitledBorder( BorderFactory.createLineBorder(Color.BLACK), "Datos del Producto", 1, 2, new Font("Century Gothic", Font.BOLD, 18)));
		
		JLabel lblErrorNombre = new JLabel("New label");
		lblErrorNombre.setForeground(new Color(128, 0, 0));
		lblErrorNombre.setFont(new Font("Century Gothic", Font.BOLD, 11));
		lblErrorNombre.setBounds(152, 53, 441, 14);
		pnlDatos.add(lblErrorNombre);
		
		JLabel lblErrorDescripcion = new JLabel("New label");
		lblErrorDescripcion.setForeground(new Color(128, 0, 0));
		lblErrorDescripcion.setFont(new Font("Century Gothic", Font.BOLD, 11));
		lblErrorDescripcion.setBounds(152, 164, 441, 14);
		pnlDatos.add(lblErrorDescripcion);
		
		JLabel lblErrorProveedor = new JLabel("New label");
		lblErrorProveedor.setForeground(new Color(128, 0, 0));
		lblErrorProveedor.setFont(new Font("Century Gothic", Font.BOLD, 11));
		lblErrorProveedor.setBounds(152, 276, 332, 14);
		pnlDatos.add(lblErrorProveedor);
		
		JLabel lblErrorCategoria = new JLabel("New label");
		lblErrorCategoria.setForeground(new Color(128, 0, 0));
		lblErrorCategoria.setFont(new Font("Century Gothic", Font.BOLD, 11));
		lblErrorCategoria.setBounds(152, 316, 332, 14);
		pnlDatos.add(lblErrorCategoria);
		
		JLabel lblErrorCodigo = new JLabel("New label");
		lblErrorCodigo.setForeground(new Color(128, 0, 0));
		lblErrorCodigo.setFont(new Font("Century Gothic", Font.BOLD, 11));
		lblErrorCodigo.setBounds(152, 358, 334, 14);
		pnlDatos.add(lblErrorCodigo);
		
		JLabel lblErrorPrecio = new JLabel("New label");
		lblErrorPrecio.setForeground(new Color(128, 0, 0));
		lblErrorPrecio.setFont(new Font("Century Gothic", Font.BOLD, 11));
		lblErrorPrecio.setBounds(285, 192, 308, 14);
		pnlDatos.add(lblErrorPrecio);
		
		JLabel lblErrorCantidad = new JLabel("New label");
		lblErrorCantidad.setForeground(new Color(128, 0, 0));
		lblErrorCantidad.setFont(new Font("Century Gothic", Font.BOLD, 11));
		lblErrorCantidad.setBounds(285, 220, 308, 14);
		pnlDatos.add(lblErrorCantidad);
		
		lblErrorNombre.setVisible(false);
		lblErrorDescripcion.setVisible(false);
		lblErrorProveedor.setVisible(false);
		lblErrorCategoria.setVisible(false);
		lblErrorCodigo.setVisible(false);
		lblErrorPrecio.setVisible(false);
		lblErrorCantidad.setVisible(false);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setFont(new Font("Century Gothic", Font.BOLD, 16));
		lblNombre.setBounds(20, 37, 83, 14);
		pnlDatos.add(lblNombre);
		
		txtNombre = new JTextField();
		txtNombre.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				validarCampo(txtNombre.getText(), lblErrorNombre, "nombre");
			}
		});
		txtNombre.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		txtNombre.setBounds(152, 34, 440, 20);
		pnlDatos.add(txtNombre);
		txtNombre.setColumns(10);
		
		JLabel lblDescripcion = new JLabel("Descripción: ");
		lblDescripcion.setFont(new Font("Century Gothic", Font.BOLD, 16));
		lblDescripcion.setBounds(20, 78, 137, 26);
		pnlDatos.add(lblDescripcion);
		
		JTextArea txtDescripcion = new JTextArea();
		txtDescripcion.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				validarCampo(txtDescripcion.getText(), lblErrorDescripcion, "descripcion");
			}
		});
		txtDescripcion.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_TAB) {
		            e.consume();
		            if (e.isShiftDown()) {
		                txtDescripcion.transferFocusBackward();
		            } else {
		                txtDescripcion.transferFocus();
		            }
		        }
			}
		});
		txtDescripcion.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		txtDescripcion.setBounds(152, 65, 440, 96);
		txtDescripcion.setLineWrap(true);
		txtDescripcion.setWrapStyleWord(true);
		txtDescripcion.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		JScrollPane scrollPane = new JScrollPane(txtDescripcion);
		scrollPane.setBounds(152, 79, 440, 85);
		pnlDatos.add(scrollPane);
		
		JLabel lblPrecio = new JLabel("Precio: ");
		lblPrecio.setFont(new Font("Century Gothic", Font.BOLD, 16));
		lblPrecio.setBounds(20, 189, 137, 14);
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
		txtPrecio.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				validarCampo(txtPrecio.getText(), lblErrorPrecio, "precio");
			}
		});
		txtPrecio.setText("0.00");
		txtPrecio.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		txtPrecio.setBounds(152, 186, 123, 20);
		pnlDatos.add(txtPrecio);
		
		JLabel lblProveedor = new JLabel("Proveedor:");
		lblProveedor.setFont(new Font("Century Gothic", Font.BOLD, 16));
		lblProveedor.setBounds(20, 263, 137, 14);
		pnlDatos.add(lblProveedor);
		
		txtProveedor = new JTextField();
		txtProveedor.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				validarCampo(txtProveedor.getText(), lblErrorProveedor, "proveedor");
			}
		});
		txtProveedor.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		txtProveedor.setColumns(10);
		txtProveedor.setBounds(152, 260, 333, 20);
		pnlDatos.add(txtProveedor);
		
		txtCategoria = new JTextField();
		txtCategoria.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				validarCampo(txtCategoria.getText(), lblErrorCategoria, "categoria");
			}
		});
		txtCategoria.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		txtCategoria.setColumns(10);
		txtCategoria.setBounds(152, 301, 333, 20);
		pnlDatos.add(txtCategoria);
		
		JLabel lblCategoria = new JLabel("Categoria: ");
		lblCategoria.setFont(new Font("Century Gothic", Font.BOLD, 16));
		lblCategoria.setBounds(20, 301, 137, 20);
		pnlDatos.add(lblCategoria);
		
		JLabel lblCantidad = new JLabel("Cantidad: ");
		lblCantidad.setFont(new Font("Century Gothic", Font.BOLD, 16));
		lblCantidad.setBounds(20, 214, 137, 20);
		pnlDatos.add(lblCantidad);
		
		
		NumberFormatter qtyFormatter = new NumberFormatter(NumberFormat.getIntegerInstance());
		qtyFormatter.setValueClass(Integer.class);
		qtyFormatter.setMinimum(0);
		qtyFormatter.setCommitsOnValidEdit(true);
		txtCantidad = new JFormattedTextField(qtyFormatter);
		txtCantidad.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				validarCampo(txtCantidad.getText(), lblErrorCantidad, "cantidad");
			}
		});
		txtCantidad.setText("1");
		txtCantidad.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		txtCantidad.setBounds(152, 217, 123, 20);
		pnlDatos.add(txtCantidad);
		
		JLabel lblCodigo = new JLabel("Código: ");
		lblCodigo.setFont(new Font("Century Gothic", Font.BOLD, 16));
		lblCodigo.setBounds(20, 341, 137, 20);
		pnlDatos.add(lblCodigo);
		
		MaskFormatter maskCodigo;
		try {
		    maskCodigo = new MaskFormatter("######"); // 6 dígitos obligatorios
		    txtCodigo = new JFormattedTextField(maskCodigo);
		    txtCodigo.addFocusListener(new FocusAdapter() {
		    	@Override
		    	public void focusLost(FocusEvent e) {
		    		validarCampo(txtCodigo.getText(), lblErrorCodigo, "codigo");
		    	}
		    });
		} catch (ParseException e) {
		    e.printStackTrace();
		    txtCodigo = new JTextField();
		}
		txtCodigo.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		txtCodigo.setBounds(152, 341, 333, 20);
		pnlDatos.add(txtCodigo);
		
		JPanel pnlImagen = new JPanel();
		pnlImagen.setBounds(10, 445, 614, 226);
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
		previewPanel.setBounds(149, 76, 300, 140);
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
				String precioInput = txtPrecio.getText();
				String cantidadInput = txtCantidad.getText();
				String proveedorInput = txtProveedor.getText();
				String categoriaInput = txtCategoria.getText();
				String codigoInput = txtCodigo.getText();
				
				boolean validaciones[] = new boolean[7];
				validaciones[0] = validarCampo(nombreInput, lblErrorNombre, "nombre");
				validaciones[1] = validarCampo(descInput, lblErrorDescripcion, "descripcion");
				validaciones[2] = validarCampo(precioInput, lblErrorPrecio, "precio");
				validaciones[3] = validarCampo(cantidadInput, lblErrorCantidad, "cantidad");
				validaciones[4] = validarCampo(proveedorInput, lblErrorProveedor, "proveedor");
				validaciones[5] = validarCampo(categoriaInput, lblErrorCategoria, "categoria");
				validaciones[6] = validarCampo(codigoInput, lblErrorCodigo, "codigo");
				
				boolean valido = true;
				for (boolean validacion : validaciones) {
					if (!validacion) {
						valido = false;
					}
				}
				if (!valido) { return; }
				
				String imagenPath = null;
		        if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
		            imagenPath = guardarImagen(selectedImagePath, codigoInput);
		        }
		        
		        // Parseamos los valores numéricos
		        double precio = Double.parseDouble(precioInput);
		        int cantidad = Integer.parseInt(cantidadInput);
				
				boolean resultado = producto.insertar(nombreInput, descInput, precio, cantidad, proveedorInput, categoriaInput, codigoInput, imagenPath);
				
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
	 * Método auxiliar para validar campos
	 */
	private boolean validarCampo(String campo, JLabel errorLabel, String tipoValidacion) {
	    String resultado = "";
	    
	    switch (tipoValidacion) {
	        case "nombre":
	            resultado = Validator.validarNombre(campo);
	            break;
	        case "descripcion":
	            resultado = Validator.validarDescripcion(campo);
	            break;
	        case "precio":
	            resultado = Validator.validarPrecio(campo);
	            break;
	        case "cantidad":
	            resultado = Validator.validarCantidad(campo);
	            break;
	        case "proveedor":
	            resultado = Validator.validarProveedor(campo);
	            break;
	        case "categoria":
	            resultado = Validator.validarCategoria(campo);
	            break;
	        case "codigo":
	            resultado = Validator.validarCodigo(campo);
	            // Validar código duplicado (solo en ModalAgregar)
	            if (resultado.equals("correcto")) {
	                if (producto.existeCodigo(campo.replaceAll("_", ""))) {
	                    resultado = "Este código ya existe";
	                }
	            }
	            break;
	    }
	    
	    if (!resultado.equals("correcto")) {
	        errorLabel.setText(resultado);
	        errorLabel.setVisible(true);
	        return false;
	    } else {
	        errorLabel.setVisible(false);
	        return true;
	    }
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
