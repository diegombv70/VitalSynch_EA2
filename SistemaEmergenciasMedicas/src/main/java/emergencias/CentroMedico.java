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

    public synchronized void notificarLlegada(Recurso recurso, Emergencia emergencia) {
        // Aquí puedes manejar la lógica de lo que sucede cuando un recurso llega
        System.out.printf("🚑 Recurso #%d ha llegado al centro médico #%d con la emergencia #%d\n",
                recurso.getId(), this.id, emergencia.getId());

        // Asignar equipo médico
        EquipoMedico equipoAsignado = asignarEquipo();
        if (equipoAsignado != null) {
            int tiempoAtencion = obtenerTiempoPorGravedad(emergencia.getGravedad());
            System.out.printf("🩺 Equipo médico #%d asignado para atender emergencia #%d\n", equipoAsignado.getId(), emergencia.getId());
            // Simular tiempo de atención
            try {
                Thread.sleep(tiempoAtencion); // Simular tiempo de atención
            } catch (InterruptedException e) {
                System.out.println("❌ Error durante la atención de la emergencia: " + e.getMessage());
            }
            liberarEquipo(equipoAsignado);
            recurso.liberar();
            System.out.printf("🔄 Recurso #%d liberado tras atender emergencia #%d y equipo médico #%d liberado\n",
                    recurso.getId(), emergencia.getId(), equipoAsignado.getId());
        } else {
            System.out.printf("⏳ No hay equipos médicos disponibles en el centro médico #%d para emergencia #%d\n", this.id, emergencia.getId());
            recurso.liberar(); // Liberar la ambulancia si no hay equipo médico
        }
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

    private int obtenerTiempoPorGravedad(Emergencia.Gravedad gravedad) {
        switch (gravedad) {
            case CRITICO:
                return 15000; // 15 segundos
            case GRAVE:
                return 12000; // 12 segundos
            case MODERADO:
                return 10000; // 10 segundos
            case LEVE:
                return 5000;  // 5 segundos
            default:
                return 5000; // valor por defecto
        }
    }
}
