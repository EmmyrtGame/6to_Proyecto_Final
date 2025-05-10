import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.FlowLayout;

import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ModalAgregar extends JDialog {

	private static final long serialVersionUID = 1L;
	private JTextField txtNombre;
	private JTextField txtPrecio;
	private JTextField txtProveedor;
	private JTextField txtCategoria;
	private JTextField txtCantidad;
	private JTextField txtCodigo;
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
		
		txtPrecio = new JTextField();
		txtPrecio.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		txtPrecio.setColumns(10);
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
		
		txtCantidad = new JTextField();
		txtCantidad.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		txtCantidad.setColumns(10);
		txtCantidad.setBounds(152, 203, 123, 20);
		pnlDatos.add(txtCantidad);
		
		JLabel lblCodigo = new JLabel("Código: ");
		lblCodigo.setFont(new Font("Century Gothic", Font.BOLD, 16));
		lblCodigo.setBounds(20, 314, 137, 20);
		pnlDatos.add(lblCodigo);
		
		txtCodigo = new JTextField();
		txtCodigo.setFont(new Font("Century Gothic", Font.PLAIN, 16));
		txtCodigo.setColumns(10);
		txtCodigo.setBounds(152, 314, 245, 20);
		pnlDatos.add(txtCodigo);
		
		JPanel pnlImagen = new JPanel();
		pnlImagen.setBounds(10, 433, 614, 234);
		getContentPane().add(pnlImagen);
		pnlImagen.setLayout(null);
		pnlImagen.setBorder(BorderFactory.createTitledBorder( BorderFactory.createLineBorder(Color.BLACK), "Imagen", 1, 2, new Font("Century Gothic", Font.BOLD, 18)));
		
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
				
				boolean resultado = producto.insertar(nombreInput, descInput, precioInput, cantidadInput, proveedorInput, categoriaInput, codigoInput);
				
				if (resultado) {
		            JOptionPane.showMessageDialog(null, "Producto guardado con éxito",  "Éxito", JOptionPane.INFORMATION_MESSAGE);
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
}
