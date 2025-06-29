package biblioteca.modelo;

import java.util.ArrayList;

public class Reporte {

    // Devuelve la cantidad total de préstamos
    public static int totalLibrosPrestados(ArrayList<Prestamo> prestamos) {
        return prestamos.size();
    }

    // Muestra los libros prestados con detalles
    public static void mostrarLibrosPrestados(ArrayList<Prestamo> prestamos) {
        if (prestamos.isEmpty()) {
            System.out.println("No hay préstamos registrados.");
            return;
        }

        for (Prestamo p : prestamos) {
            System.out.println("Libro: " + p.getLibro().getTitulo() +
                               " | Prestado a: " + p.getUsuario().getNombre() +
                               " (DNI: " + p.getUsuario().getDni() + ")" +
                               " | Devuelve el: " + p.getFechaDevolucion());
        }
    }
}


