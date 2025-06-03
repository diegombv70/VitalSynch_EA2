package emergencias;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

public class SistemaMonitoreo {
    private final PriorityBlockingQueue<Emergencia> emergencias = new PriorityBlockingQueue<>();
    private final List<Recurso> recursos = Collections.synchronizedList(new ArrayList<>());

    public void registrarEmergencia(Emergencia e) {
        emergencias.offer(e);
        System.out.println("🚨 Emergencia registrada: " + e);
    }

    public void registrarRecurso(Recurso r) {
        recursos.add(r);
        System.out.println("🚑 Recurso disponible: " + r);
    }

    public List<Recurso> getRecursos() {
        return recursos;
    }

    public Emergencia obtenerEmergenciaPrioritaria() {
        return emergencias.peek();
    }

    public Emergencia tomarEmergencia() {
        return emergencias.poll();
    }

    public void mostrarEstado() {
        System.out.println("\n===== ESTADO DEL SISTEMA =====");
        synchronized (emergencias) {
            for (Emergencia e : emergencias) {
                System.out.println(e);
            }
        }
        synchronized (recursos) {
            for (Recurso r : recursos) {
                System.out.println(r);
            }
        }
        System.out.println("================================\n");
    }
}
