package emergencias;

public class Emergencia {
    private String id;
    private String gravedad; // Crítico, Grave, Moderado, Leve
    private long tiempoEspera; // Tiempo en milisegundos
    private String ubicacion;

    public Emergencia(String id, String gravedad, long tiempoEspera, String ubicacion) {
        this.id = id;
        this.gravedad = gravedad;
        this.tiempoEspera = tiempoEspera;
        this.ubicacion = ubicacion;
    }

    public String getId() {
        return id;
    }

    public String getGravedad() {
        return gravedad;
    }

    public long getTiempoEspera() {
        return tiempoEspera;
    }

    public String getUbicacion() {
        return ubicacion;
    }
}
