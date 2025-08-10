package net.txsla.itemarchivereimagined.DataTypes;

import net.txsla.itemarchivereimagined.b64;

public class Sound {
    private String sound;
    private float volume;
    private float pitch;
    public Sound(String sound) {
        this.sound = sound;
        this.volume = 1;
        this.pitch = 1;
    }
    public Sound(String sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    // Getters
    public String getSound() {return sound;}

    public float getVolume() {return volume;}

    public float getPitch() {return pitch;}
    // Setters
    public void setSound(String sound) {this.sound = sound;}

    public void setVolume(float volume) {this.volume = volume;}

    public void setPitch(float pitch) {this.pitch = pitch;}
    public org.bukkit.Sound getBukkitSound() {
        try {
            return org.bukkit.Sound.valueOf(this.sound.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // invalid name
        }
    }

    public String serialize() {
        return b64.encode(
                b64.encode(this.sound) + "¦" +
                    b64.encode(""+this.volume) + "¦" +
                    b64.encode(""+this.pitch)
        );
    }
    public Sound deserialize(String serialized) {
        String[] data = b64.decode(serialized).split("¦");
        float volume, pitch;
        try {
            volume = Float.parseFloat(b64.decode(data[1]));
            pitch = Float.parseFloat(b64.decode(data[2]));
        }catch (Exception e) {
            return new Sound(b64.decode(data[0]));
        }
        return new Sound(
                b64.decode(data[0]),
                volume,
                pitch
        );
    }
}
