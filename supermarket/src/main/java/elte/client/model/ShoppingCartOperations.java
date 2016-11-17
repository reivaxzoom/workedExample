package elte.client.model;

import org.javamoney.moneta.FastMoney;

/**
 *
 * @author Xavier
 */
public interface ShoppingCartOperations {

    /**
     * Put an item into the list
     * @param item 
     * @return 
     */
    boolean add(Item item);
    /**
     * remove a selected item from the list
     * @param item
     * @return 
     */
    boolean remove(Item item);
    /**
     * calculates the sum of the price of the items
     * @return 
     */
    FastMoney getTotal();
    int itemNumber();
    
}
