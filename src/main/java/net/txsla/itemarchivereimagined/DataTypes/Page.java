package net.txsla.itemarchivereimagined.DataTypes;

public class Page {
    private int width, height;
    private String name;// name displayed at the top of the page
    private int vault_populators; // used to quickly get the amount of populators on the page to efficiently find the starting index for the next page
    // format is an array of placeholder IDs used to properly load an archive
    private String[] format; // format.length should always equal width*height
}
