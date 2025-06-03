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

                        // Obtener el centro médico más cercano a la emergencia
                        CentroMedico centroCercano = sistema.obtenerCentroMedicoCercano(emergencia);
                        if (centroCercano != null) {
                            // Calcular tiempo de viaje al centro médico
                            double distanciaCentro = mejor.distanciaA(centroCercano);
                            int tiempoViaje = (int) (distanciaCentro / 10 * 60); // Suponiendo 10 unidades de distancia por minuto

                            // Usar variables finales para la lambda
                            final Recurso finalRecurso = mejor; // Hacer la variable final
                            final Emergencia finalEmergencia = emergencia; // Hacer la variable final

                            new Thread(() -> {
                                try {
                                    Thread.sleep(tiempoViaje * 1000); // Convertir a milisegundos
                                    finalRecurso.moverA(centroCercano.getX(), centroCercano.getY());
                                    System.out.printf("🚑 Recurso #%d llegó al centro médico #%d\n", finalRecurso.getId(), centroCercano.getId());

                                    // Aquí solo se notifica al centro médico que ha llegado
                                    centroCercano.notificarLlegada(finalRecurso, finalEmergencia);
                                    
                                } catch (InterruptedException ex) {
                                    System.out.println("❌ Error al mover recurso: " + ex.getMessage());
                                }
                            }).start();
                        } else {
                            System.out.printf("⏳ No hay centros médicos disponibles para atender la emergencia #%d\n", emergencia.getId());
                            mejor.liberar(); // Liberar la ambulancia si no hay centro médico
                        }
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
}
