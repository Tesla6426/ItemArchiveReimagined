package net.txsla.itemarchivereimagined.DataTypes;

import net.txsla.itemarchivereimagined.ItemConverter;
import net.txsla.itemarchivereimagined.b64;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Placeholder {
    private String id;

    /*
     *  Action Values
     *   0 = nothing
     *   1 = static               - cancels event when player clicks on item
     *   2 = next_page
     *   3 = prev_page
     *   4 = execute_command      - executes the command as the player who clicked
     *   5 = submission_populator - populates the slot with the next submitted item
     * */
    private int action;
    private String action_data;
    private ItemStack item;
    public Placeholder(String id) {
        this.id = id;
        this.item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        this.action = 0;
        this.action_data = "none";
    }
    public Placeholder(String id, int action, String action_data, ItemStack item) {
        this.id = id;
        this.item = item;
        this.action = action;
        this.action_data = action_data;
    }
    public void setItem(ItemStack item) {this.item = item;}
    public void setAction(int action) {this.action = action;}
    public void setAction_data(String action_data) {this.action_data = action_data;}

    public String id() {return this.id;}





    public String serialize() {
        return b64.encode(
                b64.encode(this.id) + "¦" +
                    b64.encode("" + this.action) + "¦" +
                    b64.encode(action_data) + "¦" +
                    b64.encode(ItemConverter.toString(this.item))
                );
    }
    public static Placeholder deserialize(String serialized_placeholder) {
        String[] data = b64.decode(serialized_placeholder).split("¦");
        return new Placeholder(
                b64.decode(data[0]),
                Integer.parseInt(b64.decode(data[1])),
                b64.decode(data[2]),
                ItemConverter.toItemStack(b64.decode(data[3]))
        );
    }
}
