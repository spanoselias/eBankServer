package dto.enums;


import java.util.HashMap;
import java.util.Map;

public enum Currency {
    USD(1), EUR(2), GBP(3);

    private int id;
    private final static Map map = new HashMap<>(2);

    Currency(int id) {
        this.id = id;
    }

    static {
        for (Currency currency : Currency.values()) {
            map.put(currency.getId(), currency);
        }
    }

    public static Currency valueOf(int id) {
        return (Currency) map.get(id);
    }

    public int getId() {
        return id;
    }
}
