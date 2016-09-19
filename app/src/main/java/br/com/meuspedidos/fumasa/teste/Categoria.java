package br.com.meuspedidos.fumasa.teste;

import org.json.JSONException;
import org.json.JSONObject;

public class Categoria {
    private Long id;
    private String name;

    public static Categoria fromJson(JSONObject json) {
        Categoria p = new Categoria();

        try {
            p.setId(json.getLong("id"));
            p.setName(json.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return p;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
