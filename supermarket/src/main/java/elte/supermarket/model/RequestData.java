
package elte.supermarket.model;

import com.querydsl.core.annotations.PropertyType;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryTransient;
import com.querydsl.core.annotations.QueryType;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Xavier
 */

@QueryEntity
//@QueryEmbeddable
//@QuerySupertype
public class RequestData {
        static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

        private String id;
        private String subOrd;
        @QueryType(PropertyType.STRING)
        private String name;
        private String address;
        private String phone;
        private String country;
        private Date expDelivery;
        private String comments;
        private String deliverAddress;
        @QueryType(PropertyType.DATE)
        private Date date;
        @QueryType(PropertyType.NUMERIC)
        private short budget;
        private String category;
        private Boolean frecuent;
        @QueryTransient 
        private int itemNumber;
        @QueryTransient 
        private Object items;

    public RequestData() {
    }
    
    
    

    public RequestData(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Boolean getFrecuent() {
        return frecuent;
    }

    public void setFrecuent(Boolean frecuent) {
        this.frecuent = frecuent;
    }

    public short getBudget() {
        return budget;
    }

    public void setBudget(short budget) {
        this.budget = budget;
    }

    public int getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getSubOrd() {
        return subOrd;
    }

    public void setSubOrd(String subOrd) {
        this.subOrd = subOrd;
    }

    public Date getExpDelivery() {
        return expDelivery;
    }

    public void setExpDelivery(Date expDelivery) {
        this.expDelivery = expDelivery;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public RequestData(String id) {
        this.id = id;
    }
    
    
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDeliverAddress() {
        return deliverAddress;
    }

    public void setDeliverAddress(String deliverAddress) {
        this.deliverAddress = deliverAddress;
    }

    public Object getItems() {
        return items;
    }

    public void setItems(Object items) {
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RequestData{" + "id=" + id + ", address=" + address + ", country=" + country + ",expDelivery=" + dateFormat.format(expDelivery) + ", category=" + category + ", itemNumber=" + itemNumber + ", items=" + items + '}';
    }
}
        