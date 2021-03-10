package all.repos;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public enum RepositoryType {
    MEMORY("memory"),
    POSTGRES("postgres"),
    JPA("jpa");

    private final String type;
    private final static Map<String, RepositoryType> value2enumMap = initValue2EnumMap();

    RepositoryType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static RepositoryType getRepositoryTypeByString(String value) {
        return value2enumMap.getOrDefault(value, MEMORY);
    }

    public static String getStringByRepositoryType(RepositoryType type) {
        return type.getType();
    }

    private static Map<String, RepositoryType> initValue2EnumMap() {
        Map<String, RepositoryType> map = new HashMap<>();
        for (RepositoryType type: RepositoryType.values()) {
            map.put(type.getType(), type);
        }
        return Collections.unmodifiableMap(map);
    }
}
