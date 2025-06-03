package emergencias;

public class EquipoMedico {
    private static int contador = 0;
    private final int id;
    private boolean disponible;

    public EquipoMedico() {
        this.id = ++contador;
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

    @Override
    public String toString() {
        return String.format("Equipo Médico #%d [%s]", id, estaDisponible() ? "Disponible" : "Ocupado");
    }
}
