package emergencias;

public class Recurso {
    private String tipo; // Ambulancia, Médico, Equipamiento
    private boolean disponible;

    public Recurso(String tipo) {
        this.tipo = tipo;
        this.disponible = true;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void ocupar() {
        this.disponible = false;
    }

    public void liberar() {
        this.disponible = true;
    }
}
