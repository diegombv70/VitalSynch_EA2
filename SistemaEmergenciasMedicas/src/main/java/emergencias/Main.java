package emergencias;

public class Main {
    public static void main(String[] args) {
        SistemaMonitoreo sistema = new SistemaMonitoreo();

        // Registrar recursos (ambulancias)
        sistema.registrarRecurso(new Recurso(10, 10));
        sistema.registrarRecurso(new Recurso(50, 50));
        sistema.registrarRecurso(new Recurso(20, 80));
        sistema.registrarRecurso(new Recurso(30, 60));
        sistema.registrarRecurso(new Recurso(40, 60));

        // Lanzar hilos
        Thread operador = new Thread(new Operador(sistema));
        Thread despachador = new Thread(new Despachador(sistema));

        operador.start();
        despachador.start();

        // Monitoreo periódico
        while (true) {
            try {
                sistema.mostrarEstado();
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
