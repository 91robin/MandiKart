package models;

/**
 * Created by IntruSoft on 10/13/2015.
 */
public class ProductModel {


    String id;
    String name;
    String brand_name;
    String primary_image;
    String mrp;
    String price;
    String unit_pack_size;
    String shipper_pack_size;
    String unit_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getPrimary_image() {
        return primary_image;
    }

    public void setPrimary_image(String primary_image) {
        this.primary_image = primary_image;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnit_pack_size() {
        return unit_pack_size;
    }

    public void setUnit_pack_size(String unit_pack_size) {
        this.unit_pack_size = unit_pack_size;
    }

    public String getShipper_pack_size() {
        return shipper_pack_size;
    }

    public void setShipper_pack_size(String shipper_pack_size) {
        this.shipper_pack_size = shipper_pack_size;
    }

    public String getUnit_name() {
        return unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }
}
