package emergencias;

import java.util.ArrayList;
import java.util.List;

public class CentroMedico {
    private static int contador = 0;
    private final int id;
    private final int x;
    private final int y;
    private final List<EquipoMedico> equipos;
    private int emergenciasAtendidas;

    public CentroMedico(int x, int y) {
        this.id = ++contador;
        this.x = x;
        this.y = y;
        this.equipos = new ArrayList<>();
        this.emergenciasAtendidas = 0;

        // Inicializar equipos médicos
        for (int i = 0; i < 5; i++) {
            equipos.add(new EquipoMedico());
        }
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public synchronized boolean puedeAtender() {
        return emergenciasAtendidas < 6; // Límite de 6 emergencias
    }

    public synchronized EquipoMedico asignarEquipo() {
        for (EquipoMedico equipo : equipos) {
            if (equipo.estaDisponible()) {
                equipo.asignar();
                emergenciasAtendidas++; // Incrementar el contador de emergencias atendidas
                return equipo;
            }
        }
        return null; // No hay equipos disponibles
    }

    public synchronized void liberarEquipo(EquipoMedico equipo) {
        equipo.liberar();
        emergenciasAtendidas--; // Decrementar el contador al liberar el equipo
    }

    public double distanciaA(Emergencia e) {
        int dx = e.getX() - this.x;
        int dy = e.getY() - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return String.format("Centro Médico #%d en (%d,%d) - Emergencias atendidas: %d", id, x, y, emergenciasAtendidas);
    }
}
