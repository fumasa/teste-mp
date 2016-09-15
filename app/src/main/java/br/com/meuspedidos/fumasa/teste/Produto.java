package br.com.meuspedidos.fumasa.teste;

import org.json.JSONException;
import org.json.JSONObject;

public class Produto {
    private String name;
    private String description;
    private String photo;
    private Double price;
    private Long category_id;

    public static Produto fromJson(JSONObject json) {
        Produto p = new Produto();

        try {
            p.setName(json.getString("name"));
            p.setDescription(json.getString("description"));
            p.setPhoto(json.getString("photo"));
            p.setPrice(json.getDouble("price"));
            p.setCategory_id(json.isNull("category_id") ? null : json.getLong("category_id"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return p;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }
}
