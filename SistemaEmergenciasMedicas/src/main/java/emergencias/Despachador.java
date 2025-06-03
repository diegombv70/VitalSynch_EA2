package emergencias;

public class Despachador implements Runnable {
    private final SistemaMonitoreo sistema;

    public Despachador(SistemaMonitoreo sistema) {
        this.sistema = sistema;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Emergencia emergencia = sistema.tomarEmergencia();
                if (emergencia == null) {
                    Thread.sleep(1000);
                    continue;
                }

                Recurso mejor = null;
                double menorDistancia = Double.MAX_VALUE;

                synchronized (sistema.getRecursos()) {
                    for (Recurso r : sistema.getRecursos()) {
                        if (r.estaDisponible()) {
                            double dist = r.distanciaA(emergencia);
                            if (dist < menorDistancia) {
                                menorDistancia = dist;
                                mejor = r;
                            }
                        }
                    }

                    if (mejor != null) {
                        mejor.asignar();
                        mejor.moverA(emergencia.getX(), emergencia.getY());
                        emergencia.marcarAtendida();

                        System.out.printf("✅ Emergencia #%d atendida por recurso #%d\n",
                                emergencia.getId(), mejor.getId());

                        // ⏳ Liberación automática según gravedad
                        int tiempoAtencion = obtenerTiempoPorGravedad(emergencia.getGravedad());
                        Recurso recursoAsignado = mejor;

                        new Thread(() -> {
                            try {
                                Thread.sleep(tiempoAtencion);
                                recursoAsignado.liberar();
                                System.out.printf("🔄 Recurso #%d liberado tras atender emergencia #%d\n",
                                        recursoAsignado.getId(), emergencia.getId());
                            } catch (InterruptedException ex) {
                                System.out.println("❌ Error al liberar recurso: " + ex.getMessage());
                            }
                        }).start();

                    } else {
                        System.out.printf("⏳ No hay recursos disponibles para emergencia #%d\n", emergencia.getId());
                        sistema.registrarEmergencia(emergencia); // la volvemos a poner en cola
                    }
                }

                Thread.sleep(2000); // espera entre asignaciones
            }
        } catch (InterruptedException e) {
            System.out.println("🛑 Despachador detenido.");
        }
    }

private int obtenerTiempoPorGravedad(Emergencia.Gravedad gravedad) {
    switch (gravedad) {
        case CRITICO:
            return 15000;
        case GRAVE:
            return 12000;
        case MODERADO:
            return 10000;
        case LEVE:
            return 5000;
        default:
            return 5000; // valor por defecto o lanza excepción
    }
}
}