package emergencias;

import java.util.concurrent.BlockingQueue;

public class SistemaMonitoreo implements Runnable {
    private BlockingQueue<Emergencia> colaEmergencias;

    public SistemaMonitoreo(BlockingQueue<Emergencia> colaEmergencias) {
        this.colaEmergencias = colaEmergencias;
    }

    @Override
    public void run() {
        while (true) {
            // Aquí se puede implementar la lógica de monitoreo
            System.out.println("Monitoreando estado de emergencias...");
            try {
                Thread.sleep(5000); // Simular tiempo de monitoreo
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
