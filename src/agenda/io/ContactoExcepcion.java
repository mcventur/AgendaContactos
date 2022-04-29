package agenda.io;

import java.io.IOException;

public class ContactoExcepcion extends IOException {
    private String mensaje;

    public ContactoExcepcion(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String getMessage() {
        return mensaje;
    }

    @Override
    public String toString() {
        return mensaje;
    }
}
