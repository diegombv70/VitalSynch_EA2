package emergencias;
public class Emergencia implements Comparable<Emergencia> {
    public enum Gravedad {
        CRITICO, GRAVE, MODERADO, LEVE
    }

    private static int contador = 0;
    private final int id;
    private final Gravedad gravedad;
    private final int x;
    private final int y;
    private final long tiempo;
    private boolean atendida;

    public Emergencia(Gravedad gravedad, int x, int y) {
        this.id = ++contador;
        this.gravedad = gravedad;
        this.x = x;
        this.y = y;
        this.tiempo = System.currentTimeMillis();
        this.atendida = false;
    }

    public int getId() {
        return id;
    }

    public Gravedad getGravedad() {
        return gravedad;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public long getTiempo() {
        return tiempo;
    }

    public boolean estaAtendida() {
        return atendida;
    }

    public void marcarAtendida() {
        this.atendida = true;
    }

    @Override
    public int compareTo(Emergencia otra) {
        int porGravedad = otra.gravedad.ordinal() - this.gravedad.ordinal();
        if (porGravedad != 0) return porGravedad;
        return Long.compare(this.tiempo, otra.tiempo); // más antiguo primero
    }

    @Override
    public String toString() {
        return String.format("Emergencia #%d [Gravedad: %s, Posición: (%d,%d), Atendida: %s]",
                id, gravedad, x, y, atendida ? "Sí" : "No");
    }
}
