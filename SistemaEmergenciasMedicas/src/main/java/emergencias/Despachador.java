package emergencias;

import java.util.concurrent.BlockingQueue;

public class Despachador implements Runnable {
    private BlockingQueue<Emergencia> colaEmergencias;
    private Recurso recurso;

    public Despachador(BlockingQueue<Emergencia> colaEmergencias, Recurso recurso) {
        this.colaEmergencias = colaEmergencias;
        this.recurso = recurso;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Emergencia emergencia = colaEmergencias.take();
                if (recurso.isDisponible()) {
                    recurso.ocupar();
                    System.out.println("Despachando recurso a emergencia: " + emergencia.getId());
                    // Simular tiempo de respuesta
                    Thread.sleep(2000);
                    recurso.liberar();
                    System.out.println("Recurso liberado de emergencia: " + emergencia.getId());
                } else {
                    System.out.println("Recurso no disponible para emergencia: " + emergencia.getId());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
