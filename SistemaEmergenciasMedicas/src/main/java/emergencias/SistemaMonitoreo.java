package emergencias;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

public class SistemaMonitoreo {
    private final PriorityBlockingQueue<Emergencia> emergencias = new PriorityBlockingQueue<>();
    private final List<Recurso> recursos = Collections.synchronizedList(new ArrayList<>());
    private final List<CentroMedico> centrosMedicos = Collections.synchronizedList(new ArrayList<>());

    public void registrarEmergencia(Emergencia e) {
        emergencias.offer(e);
        System.out.println("Emergencia registrada: " + e);
    }

    public void registrarRecurso(Recurso r) {
        recursos.add(r);
        System.out.println("Recurso disponible: " + r);
    }

    public void registrarCentroMedico(CentroMedico centro) {
        centrosMedicos.add(centro);
        System.out.println("Centro Médico registrado: " + centro);
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

    public CentroMedico obtenerCentroMedicoCercano(Emergencia emergencia) {
        CentroMedico mejorCentro = null;
        double menorDistancia = Double.MAX_VALUE;

        for (CentroMedico centro : centrosMedicos) {
            if (centro.puedeAtender()) { // Verificar si puede atender
                double distancia = centro.distanciaA(emergencia);
                if (distancia < menorDistancia) {
                    menorDistancia = distancia;
                    mejorCentro = centro;
                }
            }
        }
        return mejorCentro;
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
        synchronized (centrosMedicos) {
            for (CentroMedico c : centrosMedicos) {
                System.out.println(c);
            }
        }
        System.out.println("================================\n");
    }
}
