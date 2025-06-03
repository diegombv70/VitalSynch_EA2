
package emergencias;
public class Recurso {
    private static int contador = 0;
    private final int id;
    private int x;
    private int y;
    private boolean disponible;

    public Recurso(int x, int y) {
        this.id = ++contador;
        this.x = x;
        this.y = y;
        this.disponible = true;
    }

    public int getId() {
        return id;
    }

    public synchronized boolean estaDisponible() {
        return disponible;
    }

    public synchronized void asignar() {
        disponible = false;
    }

    public synchronized void liberar() {
        disponible = true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void moverA(int nuevoX, int nuevoY) {
        this.x = nuevoX;
        this.y = nuevoY;
    }

    public double distanciaA(Emergencia e) {
        int dx = e.getX() - this.x;
        int dy = e.getY() - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return String.format("Recurso #%d [%s] en (%d,%d)", id,
                estaDisponible() ? "Disponible" : "Ocupado", x, y);
    }

    double distanciaA(CentroMedico centroCercano) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
