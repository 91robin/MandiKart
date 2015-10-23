package models;

/**
 * Created by IntruSoft on 10/14/2015.
 */
public class ProductDescriptionModel {

    String primaryImageUrl;
    String productName;
    String productDescription;
    String otherImages;
    String id;
    String brandName;
    String primaryThumbImage;
    String mrp;
    String price;
    String unitPackSize;
    String shiperPackSize;
    String unitName;


    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getPrimaryImageUrl() {
        return primaryImageUrl;
    }

    public void setPrimaryImageUrl(String primaryImageUrl) {
        this.primaryImageUrl = primaryImageUrl;
    }

    public String getOtherImages() {
        return otherImages;
    }

    public void setOtherImages(String otherImages) {
        this.otherImages = otherImages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getPrimaryThumbImage() {
        return primaryThumbImage;
    }

    public void setPrimaryThumbImage(String primaryThumbImage) {
        this.primaryThumbImage = primaryThumbImage;
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

    public String getUnitPackSize() {
        return unitPackSize;
    }

    public void setUnitPackSize(String unitPackSize) {
        this.unitPackSize = unitPackSize;
    }

    public String getShiperPackSize() {
        return shiperPackSize;
    }

    public void setShiperPackSize(String shiperPackSize) {
        this.shiperPackSize = shiperPackSize;
    }
}
