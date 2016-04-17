package ua.pp.myshko.bomberman.server.objects.interfaces;


/**
 * @author M. Chernenko
 */
public interface WeaponRepository {

    GameObject useWeapon();

    void destroy();

    int getCount();

    void increaseCount();
}
