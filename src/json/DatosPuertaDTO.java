package json;

public class DatosPuertaDTO {
    private String id;
    private String origen;
    private String destino;
    private String codigoLlave;
    private boolean abierta;

    public DatosPuertaDTO() {
    }

    public DatosPuertaDTO(String id, String origen, String destino,
                          String codigoLlave, boolean abierta) {
        this.id = id;
        this.origen = origen;
        this.destino = destino;
        this.codigoLlave = codigoLlave;
        this.abierta = abierta;
    }

    public String getId() { return id; }
    public String getOrigen() { return origen; }
    public String getDestino() { return destino; }
    public String getCodigoLlave() { return codigoLlave; }
    public boolean isAbierta() { return abierta; }
}
