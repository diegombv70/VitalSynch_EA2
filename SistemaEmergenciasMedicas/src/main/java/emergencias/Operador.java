package emergencias;

public class Operador implements Runnable {
    private final SistemaMonitoreo sistema;

    public Operador(SistemaMonitoreo sistema) {
        this.sistema = sistema;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Emergencia.Gravedad gravedad = Emergencia.Gravedad.values()[(int) (Math.random() * 4)];
                int x = (int) (Math.random() * 100);
                int y = (int) (Math.random() * 100);

                Emergencia emergencia = new Emergencia(gravedad, x, y);
                sistema.registrarEmergencia(emergencia);

                Thread.sleep(3000); // simula tiempo entre llamadas
            }
        } catch (InterruptedException e) {
            System.out.println("🛑 Operador detenido.");
        }
    }
}
