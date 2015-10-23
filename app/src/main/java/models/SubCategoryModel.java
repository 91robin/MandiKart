package models;

/**
 * Created by IntruSoft on 10/12/2015.
 */
public class SubCategoryModel {


    String id;
    String name;
    String categoryId;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

}
