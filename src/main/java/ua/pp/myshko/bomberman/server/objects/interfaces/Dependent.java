package ua.pp.myshko.bomberman.server.objects.interfaces;

/**
 * @author M. Chernenko
 */
public interface Dependent {

    Identifiable getOwner();
    void setOwner(Identifiable gameObject);

}
