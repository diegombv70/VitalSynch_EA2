package emergencias;

public class Main {
    public static void main(String[] args) {
        SistemaMonitoreo sistema = new SistemaMonitoreo();

        // Registrar recursos (ambulancias)
        sistema.registrarRecurso(new Recurso(10, 70));
        sistema.registrarRecurso(new Recurso(20, 60));
        sistema.registrarRecurso(new Recurso(30, 50));
        sistema.registrarRecurso(new Recurso(70, 10));
        sistema.registrarRecurso(new Recurso(40, 40));
        sistema.registrarRecurso(new Recurso(50, 30));
        sistema.registrarRecurso(new Recurso(60, 20));
        sistema.registrarRecurso(new Recurso(70, 10));


        // Registrar centros médicos
        sistema.registrarCentroMedico(new CentroMedico(10, 10));
        sistema.registrarCentroMedico(new CentroMedico(90, 40));
        sistema.registrarCentroMedico(new CentroMedico(30, 80));

        // Lanzar hilos
        Thread operador = new Thread(new Operador(sistema));
        operador.start();
        
        int numeroDeDespachadores = 12; // Puedes ajustar este número según necesites (ej. igual al número de ambulancias o menos)
                System.out.println("🚀 Iniciando " + numeroDeDespachadores + " hilos Despachadores...");
                for (int i = 0; i < numeroDeDespachadores; i++) {
                    Thread despachadorThread = new Thread(new Despachador(sistema), "Despachador-" + (i + 1));
                    despachadorThread.start();
                }

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
