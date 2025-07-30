package net.txsla.itemarchivereimagined;

import net.txsla.itemarchivereimagined.DataTypes.Item;
import net.txsla.itemarchivereimagined.DataTypes.Page;
import net.txsla.itemarchivereimagined.DataTypes.Placeholder;

public class deserialize {
    public static Placeholder placeholder(String serialized) {
        String[] data = b64.decode(serialized).split("¦");
        return new Placeholder(
                b64.decode(data[0]),
                Integer.parseInt(b64.decode(data[1])),
                b64.decode(data[2]),
                ItemConverter.toItemStack(b64.decode(data[3]))
        );
    }
    public static Page page(String serialised) {
        String[] data = b64.decode(serialised).split("¦");
        return new Page(
                b64.decode(data[0]),
                Integer.parseInt(b64.decode(data[1])),
                Integer.parseInt(b64.decode(data[2])),
                Integer.parseInt(b64.decode(data[3])),
                b64.decode(data[4]).split("¦")
        );
    }
    public static Item item(String serialized) {
        String[] out = serialized.split("¦"); // out+data
        String[] data = b64.decode(out[4]).split("¦"); // data
        return new Item(
                b64.decode(data[0]),
                b64.decode(out[3]),
                b64.decode(data[1]),
                b64.decode(data[2]),
                Double.parseDouble(b64.decode(data[3])),
                Double.parseDouble(b64.decode(data[4])),
                b64.decode(data[5]),
                ItemConverter.toItemStack(b64.decode(data[6])),
                b64.decode(data[7])
        );
    }
}
