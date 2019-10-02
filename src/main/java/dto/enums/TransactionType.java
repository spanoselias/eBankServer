package dto.enums;


import java.util.HashMap;
import java.util.Map;

public enum TransactionType {

    DEPOSIT(1),
    WITHDRAWAL(2);

    private int id;
    private static Map map = new HashMap<>(2);

    TransactionType(int id) {
        this.id = id;
    }

    static {
        for (TransactionType trancType : TransactionType.values()) {
            map.put(trancType.getId(), trancType);
        }
    }

    public static TransactionType valueOf(int id) {
        return (TransactionType) map.get(id);
    }

    public int getId() {
        return id;
    }
}
