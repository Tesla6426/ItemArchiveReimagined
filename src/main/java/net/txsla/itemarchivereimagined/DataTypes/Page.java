package net.txsla.itemarchivereimagined.DataTypes;

import net.txsla.itemarchivereimagined.b64;

public class Page {
    public Page(String name) {
        this.name = name;
        this.size = 54;
        this.vault_populators = 0;
        this.format = null;
    }
    public Page(String name, int vault_populators, int size, String[] format) {
        this.name = name;
        this.size = size;
        this.vault_populators = vault_populators;
        this.format = format;
    }
    public String getName() {return this.name;}
    public int getSize() {return this.size;}
    public int getVault_populators() {return this.vault_populators;}
    public String[] getFormat() {return format;};
    public void setName(String name) {this.name = name;}
    public void setSize(int size) {this.size = size;}
    public void setVault_populators(int vault_populators) {this.vault_populators = vault_populators;}
    public void setFormat(String[] format) {this.format = format;}
    private int size;
    private String name;// name displayed at the top of the page
    private int vault_populators; // used to quickly get the amount of populators on the page to efficiently find the starting index for the next page
    // format is an array of placeholder IDs used to properly load an archive
    private String[] format; // format.length should always equal width*height




    // format
    // b64< b64<name>| b64<vault_populators>| b64<width>| b64<height>| b64<format<sep|>>>
    public String serialize() {
        String out = b64.encode(this.name) + "¦" +
                b64.encode(""+this.vault_populators) + "¦" +
                b64.encode(""+this.size) + "¦";
        // encode format array
        StringBuilder format_encode = new StringBuilder();
        for (String s : this.format) format_encode.append(s).append("¦");
        out += b64.encode(format_encode.toString());

        return b64.encode(out);
    }
    public static Page deserialize(String serialised) {
        String[] data = b64.decode(serialised).split("¦");
        return new Page(
                b64.decode(data[0]),
                Integer.parseInt(b64.decode(data[1])),
                Integer.parseInt(b64.decode(data[2])),
                b64.decode(data[3]).split("¦")
        );
    }
}
