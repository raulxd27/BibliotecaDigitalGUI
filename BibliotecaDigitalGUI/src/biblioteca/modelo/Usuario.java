package biblioteca.modelo;

import java.util.ArrayList;

public class Usuario {
    private String dni;
    private String nombre;
    private ArrayList<Prestamo> historial = new ArrayList<>();

    public Usuario(String dni, String nombre) {
        this.dni = dni;
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Prestamo> getHistorial() {
        return historial;
    }

    public void agregarPrestamo(Prestamo p) {
        historial.add(p);
    }

    @Override
    public String toString() {
        return nombre + " (" + dni + ")";
    }

    // Clase anidada opcional
    public class Actividad {
        private int prestamosRealizados = 0;

        public void registrarPrestamo() {
            prestamosRealizados++;
        }

        public int getPrestamosRealizados() {
            return prestamosRealizados;
        }
    }
}
