package emergencias;

public class Main {
    public static void main(String[] args) {
        SistemaMonitoreo sistema = new SistemaMonitoreo();

        // Registrar recursos (ambulancias)
        sistema.registrarRecurso(new Recurso(10, 70));
        sistema.registrarRecurso(new Recurso(50, 60));
        sistema.registrarRecurso(new Recurso(20, 50));
        sistema.registrarRecurso(new Recurso(30, 40));
        sistema.registrarRecurso(new Recurso(40, 30));
        sistema.registrarRecurso(new Recurso(70, 20));
        sistema.registrarRecurso(new Recurso(60, 10));

        // Registrar centros médicos
        sistema.registrarCentroMedico(new CentroMedico(10, 10));
        sistema.registrarCentroMedico(new CentroMedico(90, 40));
        sistema.registrarCentroMedico(new CentroMedico(30, 80));

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
