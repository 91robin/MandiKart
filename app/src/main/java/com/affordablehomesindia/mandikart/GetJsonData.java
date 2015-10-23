package com.affordablehomesindia.mandikart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import models.BrandModel;
import models.CategoryModel;
import models.OrderModel;
import models.ProductDescriptionModel;
import models.ProductModel;
import models.SubCategoryModel;

public class GetJsonData {


    public static String getToken(String Json) throws JSONException {
        JSONObject jsonObject = new JSONObject(Json);
        String token = jsonObject.getString("token");
        System.out.println(token);
        return token;
    }

    public static List<CategoryModel> getcategory(String Json) throws JSONException {
        List<CategoryModel> model = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(Json);
        JSONArray jsonArray = jsonObject.getJSONArray("categories");
        for (int i = 0; i < jsonArray.length(); i++) {
            CategoryModel categoryModel = new CategoryModel();
            categoryModel.setName(jsonArray.getJSONObject(i).getString("name"));
            categoryModel.setId(jsonArray.getJSONObject(i).getString("id"));
            model.add(categoryModel);
        }
        return model;
    }

    public static List<BrandModel> getBrand(String Json) throws JSONException {
        List<BrandModel> model = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(Json);
        JSONArray jsonArray = jsonObject.getJSONArray("brands");
        for (int i = 0; i < jsonArray.length(); i++) {
            BrandModel brandModel = new BrandModel();
            brandModel.setName(jsonArray.getJSONObject(i).getString("name"));
            brandModel.setId(jsonArray.getJSONObject(i).getString("id"));
            model.add(brandModel);
        }
        return model;
    }

    public static List<SubCategoryModel> getSubcategory(String Json) throws JSONException {
        List<SubCategoryModel> model = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(Json);
        JSONArray jsonArray = jsonObject.getJSONArray("sub_categories");
        for (int i = 0; i < jsonArray.length(); i++) {
            SubCategoryModel subCategoryModel = new SubCategoryModel();
            subCategoryModel.setName(jsonArray.getJSONObject(i).getString("name"));
            subCategoryModel.setId(jsonArray.getJSONObject(i).getString("id"));
            subCategoryModel.setCategoryId(jsonArray.getJSONObject(i).getString("category_id"));
            model.add(subCategoryModel);
        }
        return model;
    }

    public static List<ProductModel> getProducts(String Json) throws JSONException {
        List<ProductModel> model = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(Json);
        JSONArray jsonArray = jsonObject.getJSONArray("products");
        for (int i = 0; i < jsonArray.length(); i++) {
            ProductModel productModel = new ProductModel();
            productModel.setName(jsonArray.getJSONObject(i).getString("name"));
            productModel.setId(jsonArray.getJSONObject(i).getString("id"));
            productModel.setBrand_name(jsonArray.getJSONObject(i).getString("brand_name"));
            productModel.setPrimary_image(jsonArray.getJSONObject(i).getString("primary_image"));
            productModel.setMrp(jsonArray.getJSONObject(i).getString("mrp"));
            productModel.setPrice(jsonArray.getJSONObject(i).getString("price"));
            productModel.setUnit_pack_size(jsonArray.getJSONObject(i).getString("unit_pack_size"));
            productModel.setShipper_pack_size(jsonArray.getJSONObject(i).getString("shipper_pack_size"));
            productModel.setUnit_name(jsonArray.getJSONObject(i).getString("unit_name"));
            model.add(productModel);
        }
        return model;
    }

    public static ProductDescriptionModel getProductDescription(String Json) throws JSONException {
        JSONObject jsonObject = new JSONObject(Json);
        ProductDescriptionModel productDescriptionModel = new ProductDescriptionModel();
        productDescriptionModel.setProductName(jsonObject.getString("name"));
        productDescriptionModel.setBrandName(jsonObject.getString("brand_name"));
        productDescriptionModel.setPrimaryImageUrl(jsonObject.getString("primary_image"));
        productDescriptionModel.setProductDescription(jsonObject.getString("description"));
        productDescriptionModel.setOtherImages(jsonObject.getString("other_images"));
        productDescriptionModel.setId(jsonObject.getString("id"));
        productDescriptionModel.setPrimaryThumbImage(jsonObject.getString("primary_thumb_image"));
        productDescriptionModel.setMrp(jsonObject.getString("mrp"));
        productDescriptionModel.setPrice(jsonObject.getString("price"));
        productDescriptionModel.setUnitPackSize(jsonObject.getString("unit_pack_size"));
        productDescriptionModel.setShiperPackSize(jsonObject.getString("shipper_pack_size"));
        productDescriptionModel.setUnitName(jsonObject.getString("unit_name"));
        return productDescriptionModel;
    }

    public static OrderModel getOrderDetail(String Json){
        JSONObject jsonObject = null;
        OrderModel model = new OrderModel();
        try {
            jsonObject = new JSONObject(Json);
            System.out.println(Json);
            model.setOrderNo(jsonObject.getString("order_no"));
            model.setDelivery_id(jsonObject.getString("delivery_id"));
            model.setPassword(jsonObject.getString("password"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return model;
    }


}
