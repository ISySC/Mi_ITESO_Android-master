package mx.iteso.miiteso.miiteso.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by PC on 13/03/2019.
 */

public class ItemArea implements Serializable{
    String superArea;
    int promedio=0;
    int creditosTotales;
    int creditosActuales;
    ArrayList<ItemMateriaKardex> items = new ArrayList<>();
    public ItemArea(String superArea,int creditosTotales,int creditosActuales)
    {
        this.superArea = superArea;
        this.creditosTotales = creditosTotales;
        this.creditosActuales = creditosActuales;
    }

    public ItemArea(String superArea)
    {
        this.superArea = superArea;
    }

    public ArrayList<ItemMateriaKardex> getItems() {
        return items;
    }

    public void addItem(ItemMateriaKardex item)
    {
        items.add(item);
    }

    public String getSuperArea() {
        return superArea;
    }

    public int getCreditosActuales() {
        return creditosActuales;
    }

    public int getCreditosTotales() {
        return creditosTotales;
    }

}
