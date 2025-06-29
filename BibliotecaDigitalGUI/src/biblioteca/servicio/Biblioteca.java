package biblioteca.servicio;

import biblioteca.modelo.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Biblioteca {

    private ArrayList<Libro> librosDisponibles = new ArrayList<>();
    private HashMap<String, Usuario> usuarios = new HashMap<>();
    private ArrayList<Prestamo> prestamos = new ArrayList<>();

    // Registrar un nuevo libro
    public void registrarLibro(String titulo, String autor) {
        librosDisponibles.add(new Libro(titulo, autor));
    }

    // Registrar un nuevo usuario
    public void registrarUsuario(String dni, String nombre) {
        usuarios.put(dni, new Usuario(dni, nombre));
    }

    // Listar libros disponibles
    public ArrayList<Libro> obtenerLibrosDisponibles() {
        ArrayList<Libro> disponibles = new ArrayList<>();
        for (Libro l : librosDisponibles) {
            if (l.estaDisponible()) {
                disponibles.add(l);
            }
        }
        return disponibles;
    }

    // Obtener todos los usuarios
    public ArrayList<Usuario> obtenerUsuarios() {
        return new ArrayList<>(usuarios.values());
    }

    // Prestar libro
    public void prestarLibro(String titulo, String dni) throws LibroNoDisponibleException {
        Libro libro = buscarLibroDisponible(titulo);
        Usuario usuario = usuarios.get(dni);

        if (libro == null) {
            throw new LibroNoDisponibleException("Libro no disponible.");
        }
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no registrado.");
        }

        libro.prestar();
        Prestamo p = new Prestamo(libro, usuario);
        prestamos.add(p);
        usuario.agregarPrestamo(p);
    }

    // Devolver libro
    public void devolverLibro(String titulo) {
        for (Libro libro : librosDisponibles) {
            if (libro.getTitulo().equalsIgnoreCase(titulo) && !libro.estaDisponible()) {
                libro.devolver();
                break;
            }
        }

        // Eliminar el préstamo correspondiente
        prestamos.removeIf(p -> p.getLibro().getTitulo().equalsIgnoreCase(titulo));
    }

    // Buscar libro disponible por título
    private Libro buscarLibroDisponible(String titulo) {
        for (Libro libro : librosDisponibles) {
            if (libro.getTitulo().equalsIgnoreCase(titulo) && libro.estaDisponible()) {
                return libro;
            }
        }
        return null;
    }

    // Obtener todos los préstamos
    public ArrayList<Prestamo> obtenerPrestamos() {
        return prestamos;
    }
}

