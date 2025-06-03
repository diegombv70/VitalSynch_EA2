package emergencias;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static void main(String[] args) {
        BlockingQueue<Emergencia> colaEmergencias = new ArrayBlockingQueue<>(10);
        Recurso recurso = new Recurso("Ambulancia");

        Thread operadorThread = new Thread(new Operador(colaEmergencias));
        Thread despachadorThread = new Thread(new Despachador(colaEmergencias, recurso));
        Thread monitoreoThread = new Thread(new SistemaMonitoreo(colaEmergencias));

        operadorThread.start();
        despachadorThread.start();
        monitoreoThread.start();
    }
}
