package biblioteca.gui;

import javax.swing.*;
import biblioteca.servicio.Biblioteca;
import biblioteca.modelo.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private Biblioteca biblioteca = new Biblioteca();

    public VentanaPrincipal() {
        setTitle("📚 Biblioteca Digital");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());

        JTabbedPane pestañas = new JTabbedPane();
        pestañas.addTab("Libros", crearPanelLibros());
        pestañas.addTab("Usuarios", crearPanelUsuarios());
        pestañas.addTab("Préstamos", crearPanelPrestamos());
        pestañas.addTab("Reportes", crearPanelReportes());

        getContentPane().add(pestañas, BorderLayout.CENTER);
        setVisible(true);
    }

    private JPanel crearPanelLibros() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField txtTitulo = new JTextField();
        JTextField txtAutor = new JTextField();
        JButton btnAgregar = new JButton("Agregar Libro");
        JButton btnVer = new JButton("Ver Disponibles");

        btnAgregar.addActionListener(e -> {
            biblioteca.registrarLibro(txtTitulo.getText(), txtAutor.getText());
            JOptionPane.showMessageDialog(this, "Libro agregado.");
            txtTitulo.setText("");
            txtAutor.setText("");
        });

        btnVer.addActionListener(e -> {
            List<Libro> libros = biblioteca.obtenerLibrosDisponibles();
            if (libros.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay libros disponibles.");
                return;
            }
            StringBuilder mensaje = new StringBuilder("📚 Libros disponibles:\n");
            for (Libro l : libros) {
                mensaje.append("Título: ").append(l.getTitulo())
                       .append(" | Autor: ").append(l.getAutor()).append("\n");
            }
            JOptionPane.showMessageDialog(this, mensaje.toString());
        });

        panel.add(new JLabel("Título :"));
        panel.add(txtTitulo);
        panel.add(new JLabel("Autor :"));
        panel.add(txtAutor);
        panel.add(btnAgregar);
        panel.add(btnVer);
        return panel;
    }

    private JPanel crearPanelUsuarios() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField txtDni = new JTextField();
        JTextField txtNombre = new JTextField();
        JButton btnRegistrar = new JButton("Registrar Usuario");

        btnRegistrar.addActionListener(e -> {
            biblioteca.registrarUsuario(txtDni.getText(), txtNombre.getText());
            JOptionPane.showMessageDialog(this, "Usuario registrado.");
            txtDni.setText("");
            txtNombre.setText("");
        });

        panel.add(new JLabel("DNI:"));
        panel.add(txtDni);
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(btnRegistrar);
        return panel;
    }

    private JPanel crearPanelPrestamos() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));

        JComboBox<String> comboLibrosPrestar = new JComboBox<>();
        JComboBox<String> comboUsuarios = new JComboBox<>();
        JComboBox<String> comboLibrosDevolver = new JComboBox<>();

        JButton btnPrestar = new JButton("Prestar Libro");
        JButton btnDevolver = new JButton("Devolver Libro");

        // Acción: Prestar libro
        btnPrestar.addActionListener(e -> {
            try {
                String libro = (String) comboLibrosPrestar.getSelectedItem();
                String usuario = (String) comboUsuarios.getSelectedItem();
                biblioteca.prestarLibro(libro, usuario);
                JOptionPane.showMessageDialog(this, "✅ Libro prestado.");
                actualizarCombos(comboLibrosPrestar, comboUsuarios, comboLibrosDevolver);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
            }
        });

        // Acción: Devolver libro
        btnDevolver.addActionListener(e -> {
            String libro = (String) comboLibrosDevolver.getSelectedItem();
            biblioteca.devolverLibro(libro);
            JOptionPane.showMessageDialog(this, "📚 Libro devuelto.");
            actualizarCombos(comboLibrosPrestar, comboUsuarios, comboLibrosDevolver);
        });

        // Actualizar combos al cambiar de pestaña
        panel.addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                actualizarCombos(comboLibrosPrestar, comboUsuarios, comboLibrosDevolver);
            }
        });

        panel.add(new JLabel("📖 Libro a prestar:"));
        panel.add(comboLibrosPrestar);
        panel.add(new JLabel("👤 Usuario (DNI):"));
        panel.add(comboUsuarios);
        panel.add(btnPrestar);

        panel.add(new JLabel("📕 Libro a devolver:"));
        panel.add(comboLibrosDevolver);
        panel.add(new JLabel(""));
        panel.add(btnDevolver);

        return panel;
    }

    private void actualizarCombos(JComboBox<String> comboLibrosPrestar, JComboBox<String> comboUsuarios, JComboBox<String> comboLibrosDevolver) {
        comboLibrosPrestar.removeAllItems();
        comboUsuarios.removeAllItems();
        comboLibrosDevolver.removeAllItems();

        for (Libro l : biblioteca.obtenerLibrosDisponibles()) {
            comboLibrosPrestar.addItem(l.getTitulo());
        }

        for (Prestamo p : biblioteca.obtenerPrestamos()) {
            comboLibrosDevolver.addItem(p.getLibro().getTitulo());
        }

        for (Usuario u : biblioteca.obtenerUsuarios()) {
            comboUsuarios.addItem(u.getDni());
        }
    }

    private JPanel crearPanelReportes() {
        JPanel panel = new JPanel(new BorderLayout());
        JButton btnVerReporte = new JButton("Ver Detalles de Préstamos");

        btnVerReporte.addActionListener(e -> {
            List<Prestamo> prestamos = biblioteca.obtenerPrestamos();
            if (prestamos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay préstamos registrados.");
                return;
            }

            StringBuilder reporte = new StringBuilder("📋 Préstamos registrados:\n\n");
            for (Prestamo p : prestamos) {
                reporte.append("Libro: ").append(p.getLibro().getTitulo())
                       .append(" | Usuario: ").append(p.getUsuario().getNombre())
                       .append(" (DNI: ").append(p.getUsuario().getDni()).append(")")
                       .append(" | Devolver hasta: ").append(p.getFechaDevolucion()).append("\n");
            }

            JTextArea area = new JTextArea(reporte.toString());
            area.setEditable(false);
            JScrollPane scroll = new JScrollPane(area);
            scroll.setPreferredSize(new Dimension(650, 300));
            JOptionPane.showMessageDialog(this, scroll, "📚 Reporte de Préstamos", JOptionPane.INFORMATION_MESSAGE);
        });

        panel.add(btnVerReporte, BorderLayout.NORTH);
        return panel;
    }

    public static void main(String[] args) {
        new VentanaPrincipal();
    }
}








