package mx.iteso.miiteso.miiteso.classes;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by PC on 24/08/2018.
 */


public class ObservableInteger {
    private OnIntegerChangeListener listener;

    private int value;
    private ArrayList<Integer> values = new ArrayList<>();

    public ObservableInteger(int value)
    {
        this.value = value;
        values.add(value);
    }
    public void setOnIntegerChangeListener(OnIntegerChangeListener listener)
    {
        this.listener = listener;
    }

    public int get()
    {
        return values.get(values.size()-1);

    }

    public int getCount() {
        return values.size();
    }

    public int getPrevoius(){
        return values.get(values.size()-2);
    }

    public void set(int value) {
        if (value < 0) {
            this.value = 0;
            values.add(value);
            if (listener != null) {
                listener.onIntegerChanged(value);
            }
        }
        else if (value>=28)
            {
                this.value =28;
                values.add(value);
                if (listener != null) {
                    listener.onIntegerChanged(value);
                }
            }
        else {
            this.value = value;
            values.add(value);
            if (listener != null) {
                listener.onIntegerChanged(value);
            }
        }
    }

}

