package elte.client.model;

import org.javamoney.moneta.FastMoney;

/**
 *
 * @author Xavier
 */
public interface ShoppingCartOperations {

    boolean add(Item item);
    boolean remove(Item item);
    FastMoney getTotal();
    int itemNumber();
    
}
