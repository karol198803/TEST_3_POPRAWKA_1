package zadanie2;

import java.util.Date;

public class Wizyta {
    private int idLekarza;
    private int idPacjenta;
    private Date dataWizyty;

    public Wizyta(int idLekarza, int idPacjenta, Date dataWizyty) {
        this.idLekarza = idLekarza;
        this.idPacjenta = idPacjenta;
        this.dataWizyty = dataWizyty;
    }

    public int getIdLekarza() {
        return idLekarza;
    }

    public int getIdPacjenta() {
        return idPacjenta;
    }

    public Date getDataWizyty() {
        return dataWizyty;
    }
}
