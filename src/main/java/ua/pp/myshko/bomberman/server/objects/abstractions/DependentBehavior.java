package ua.pp.myshko.bomberman.server.objects.abstractions;

import ua.pp.myshko.bomberman.server.objects.interfaces.Dependent;
import ua.pp.myshko.bomberman.server.objects.interfaces.Identifiable;

/**
 * @author M. Chernenko
 */
public class DependentBehavior implements Dependent {

    private Identifiable owner;

    public DependentBehavior(Identifiable owner) {
        this.owner = owner;
    }

    @Override
    public Identifiable getOwner() {
        return owner;
    }

    @Override
    public void setOwner(Identifiable gameObject) {
        this.owner = gameObject;
    }
}
