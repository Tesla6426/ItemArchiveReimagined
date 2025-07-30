package net.txsla.itemarchivereimagined;

import net.txsla.itemarchivereimagined.DataTypes.Archive;
import net.txsla.itemarchivereimagined.DataTypes.Vault;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Storage {
    // this class is to separate the storage of vaults and archives from the rest of the code simply for readability
    List<Archive> archives = new ArrayList<>();
    Map<String, Vault> vaults = new ConcurrentHashMap<>();
    String examplePage = "";
    String examplePlaceholder1 = "";
    String examplePlaceholder2 = "";
}
