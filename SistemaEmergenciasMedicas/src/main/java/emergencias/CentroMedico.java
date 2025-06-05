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

        public synchronized boolean puedeAtender() {
        return emergenciasAtendidas < 6; 
    }

    
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
        if (emergenciasAtendidas > 0) { 
             emergenciasAtendidas--; 
        }
    }

    
    public synchronized void notificarLlegada(Recurso recurso, Emergencia emergencia) {
        System.out.printf("🚑 Ambulancia Recurso #%d ha llegado al centro médico #%d con la emergencia #%d\n",
                recurso.getId(), this.id, emergencia.getId());

        
        EquipoMedico equipoAsignado = asignarEquipo(); 

        if (equipoAsignado != null) {
            System.out.printf("🩺 Equipo médico #%d asignado para atender emergencia #%d en centro #%d. (Equipos ocupados ahora: %d)\n",
                    equipoAsignado.getId(), emergencia.getId(), this.id, this.emergenciasAtendidas);

            
            recurso.liberar();
            System.out.printf("🔄 Ambulancia Recurso #%d liberada (tras entregar paciente en centro #%d).\n", recurso.getId(), this.id);

           
            final EquipoMedico finalEquipoAsignado = equipoAsignado;
            final Emergencia finalEmergencia = emergencia;
            

            new Thread(() -> {
                try {
                    int tiempoAtencion = obtenerTiempoPorGravedad(finalEmergencia.getGravedad());
                    System.out.printf("Equipo #%d inicia atención de emergencia #%d en centro #%d por %d ms.\n",
                                      finalEquipoAsignado.getId(), finalEmergencia.getId(), this.id, tiempoAtencion);
                    Thread.sleep(tiempoAtencion); // Simulación del tiempo de atención en este hilo separado
                    System.out.printf("Atención completada para emergencia #%d por equipo #%d en centro #%d.\n",
                                      finalEmergencia.getId(), finalEquipoAsignado.getId(), this.id);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Reeestablecer el estado de interrupción
                    System.out.printf("Error durante la atención de emergencia #%d por equipo #%d en centro #%d: %s\n",
                                      finalEmergencia.getId(), finalEquipoAsignado.getId(), this.id, e.getMessage());
                } finally {

                    liberarEquipo(finalEquipoAsignado); // Llama al método sincronizado para liberar el equipo
                    System.out.printf("🔄 Equipo médico #%d liberado en centro #%d. (Equipos ocupados ahora: %d)\n",
                                      finalEquipoAsignado.getId(), this.id, this.emergenciasAtendidas);
                }
            }).start();

        } else {
           
            System.out.printf("⏳ No hay equipos médicos disponibles en el centro médico #%d para emergencia #%d. (Equipos ocupados: %d)\n",
                              this.id, emergencia.getId(), this.emergenciasAtendidas);
            
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
                return 50000; // 50 segundos
            case GRAVE:
                return 20000; // 20 segundos
            case MODERADO:
                return 10000; // 10 segundos
            case LEVE:
                return 5000;  // 5 segundos
            default:
                return 5000; // valor por defecto
        }
    }
}