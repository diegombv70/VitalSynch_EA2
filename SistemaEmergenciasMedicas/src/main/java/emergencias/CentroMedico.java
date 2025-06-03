package emergencias;

import java.util.ArrayList;
import java.util.List;

public class CentroMedico {
    private static int contador = 0;
    private final int id;
    private final int x;
    private final int y;
    private final List<EquipoMedico> equipos;
    private int emergenciasAtendidas; // En tu lógica original, cuenta los equipos ocupados

    public CentroMedico(int x, int y) {
        this.id = ++contador;
        this.x = x;
        this.y = y;
        this.equipos = new ArrayList<>();
        this.emergenciasAtendidas = 0;

        // Inicializar equipos médicos
        for (int i = 0; i < 5; i++) { // Tienes 5 equipos médicos
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

    // Este método, según tu lógica original, verifica si hay menos de 6 equipos ocupados.
    // Como solo hay 5 equipos, significa que está "disponible para intentar atender" si no todos los equipos están al límite (imposible de superar 5).
    public synchronized boolean puedeAtender() {
        return emergenciasAtendidas < 6; // Límite de 6 emergencias (efectivamente 5 por el número de equipos)
    }

    // Asigna un equipo e incrementa el contador de equipos ocupados
    public synchronized EquipoMedico asignarEquipo() {
        for (EquipoMedico equipo : equipos) {
            if (equipo.estaDisponible()) {
                equipo.asignar();
                emergenciasAtendidas++; // Contador de equipos ocupados se incrementa
                return equipo;
            }
        }
        return null; // No hay equipos disponibles
    }

    // Libera un equipo y decrementa el contador de equipos ocupados
    public synchronized void liberarEquipo(EquipoMedico equipo) {
        equipo.liberar();
        if (emergenciasAtendidas > 0) { // Asegurarse de no decrementar por debajo de 0
             emergenciasAtendidas--; // Contador de equipos ocupados se decrementa
        }
    }

    // MODIFICADO para permitir atención concurrente
    public synchronized void notificarLlegada(Recurso recurso, Emergencia emergencia) {
        System.out.printf("🚑 Ambulancia Recurso #%d ha llegado al centro médico #%d con la emergencia #%d\n",
                recurso.getId(), this.id, emergencia.getId());

        // Se intenta asignar un equipo. Si tiene éxito, emergenciasAtendidas (equipos ocupados) se incrementa dentro de asignarEquipo().
        EquipoMedico equipoAsignado = asignarEquipo(); 

        if (equipoAsignado != null) {
            System.out.printf("🩺 Equipo médico #%d asignado para atender emergencia #%d en centro #%d. (Equipos ocupados ahora: %d)\n",
                    equipoAsignado.getId(), emergencia.getId(), this.id, this.emergenciasAtendidas);

            // La ambulancia (Recurso) se libera INMEDIATAMENTE después de entregar al paciente.
            recurso.liberar();
            System.out.printf("🔄 Ambulancia Recurso #%d liberada (tras entregar paciente en centro #%d).\n", recurso.getId(), this.id);

            // Crear un NUEVO HILO para manejar la atención.
            // Esto permite que notificarLlegada() termine rápido y el CentroMedico
            // pueda procesar la llegada de otras emergencias en paralelo.
            final EquipoMedico finalEquipoAsignado = equipoAsignado;
            final Emergencia finalEmergencia = emergencia;
            // 'this' (la instancia de CentroMedico) es implícitamente final para la lambda.

            new Thread(() -> {
                try {
                    int tiempoAtencion = obtenerTiempoPorGravedad(finalEmergencia.getGravedad());
                    System.out.printf("⏳ Equipo #%d inicia atención de emergencia #%d en centro #%d por %d ms.\n",
                                      finalEquipoAsignado.getId(), finalEmergencia.getId(), this.id, tiempoAtencion);
                    Thread.sleep(tiempoAtencion); // Simulación del tiempo de atención en este hilo separado
                    System.out.printf("✅ Atención completada para emergencia #%d por equipo #%d en centro #%d.\n",
                                      finalEmergencia.getId(), finalEquipoAsignado.getId(), this.id);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Reestablecer el estado de interrupción
                    System.out.printf("❌ Error durante la atención de emergencia #%d por equipo #%d en centro #%d: %s\n",
                                      finalEmergencia.getId(), finalEquipoAsignado.getId(), this.id, e.getMessage());
                } finally {
                    // Este bloque se ejecuta siempre (éxito o error en la atención).
                    // Liberar el equipo médico. Esto también decrementará 'emergenciasAtendidas'.
                    liberarEquipo(finalEquipoAsignado); // Llama al método sincronizado para liberar el equipo
                    System.out.printf("🔄 Equipo médico #%d liberado en centro #%d. (Equipos ocupados ahora: %d)\n",
                                      finalEquipoAsignado.getId(), this.id, this.emergenciasAtendidas);
                }
            }).start(); // Iniciar el hilo de atención

        } else {
            // No hay equipos médicos disponibles en este momento.
            // emergenciasAtendidas no fue incrementado por asignarEquipo() porque devolvió null.
            System.out.printf("⏳ No hay equipos médicos disponibles en el centro médico #%d para emergencia #%d. (Equipos ocupados: %d)\n",
                              this.id, emergencia.getId(), this.emergenciasAtendidas);
            
            // Si el centro no puede asignar un equipo, la ambulancia (Recurso) debe ser liberada
            // para que no quede bloqueada con el paciente.
            recurso.liberar();
            System.out.printf("🔄 Ambulancia Recurso #%d liberada (centro #%d no pudo asignar equipo médico).\n", recurso.getId(), this.id);
        }
    }

    public double distanciaA(Emergencia e) {
        int dx = e.getX() - this.x;
        int dy = e.getY() - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        // Muestra los equipos ocupados (emergenciasAtendidas)
        return String.format("Centro Médico #%d en (%d,%d) - Equipos ocupados: %d/5", id, x, y, emergenciasAtendidas);
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