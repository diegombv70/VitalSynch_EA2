package emergencias;

import java.util.concurrent.BlockingQueue;

public class Operador implements Runnable {
    private BlockingQueue<Emergencia> colaEmergencias;

    public Operador(BlockingQueue<Emergencia> colaEmergencias) {
        this.colaEmergencias = colaEmergencias;
    }

    @Override
    public void run() {
        // Simulación de recepción de llamadas
        while (true) {
            // Aquí se simularía la recepción de una nueva emergencia
            Emergencia emergencia = new Emergencia("E1", "Crítico", 0, "Ubicación A");
            try {
                colaEmergencias.put(emergencia);
                System.out.println("Emergencia recibida: " + emergencia.getId());
                Thread.sleep(1000); // Simular tiempo entre llamadas
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
