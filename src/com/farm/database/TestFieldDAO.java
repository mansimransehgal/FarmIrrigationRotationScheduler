
package com.farm.database;

import com.farm.model.Field;

public class TestFieldDAO {

    public static void main(String[] args) {

        Field field = new Field(
                "F101",
                "North Field",
                2.5,
                500,
                "HIGH"
        );

        FieldDAO dao = new FieldDAO();

        dao.saveField(field);
    }
}
