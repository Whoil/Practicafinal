package Estructuras;

public class Alumno implements Comparable<Alumno> {
        private String dni;
        private String nombre;
        private double notaMedia;

        public Alumno(String dni, String nombre, double notaMedia) {
            this.dni = dni;
            this.nombre = nombre;
            this.notaMedia = notaMedia;
        }

        public String getDni() {
            return dni;
        }

        public String getNombre() {
            return nombre;
        }

        public double getNotaMedia() {
            return notaMedia;
        }

        public void setDni(String dni) {
            this.dni = dni;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public void setNotaMedia(double notaMedia) {
            this.notaMedia = notaMedia;
        }

        @Override
        public int compareTo(Alumno otro) {
            return this.dni.compareTo(otro.dni);
        }

        @Override
        public String toString() {
            return "Estructuras.Alumno{dni='" + dni + "', nombre='" + nombre + "', notaMedia=" + notaMedia + "}";
        }
}
