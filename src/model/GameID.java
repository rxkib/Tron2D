/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author rakib
 */
import java.util.Objects;

public class GameID {
    final String mode;
    final int lvl;

    public GameID(String mode, int lvl) {
        this.mode = mode;
        this.lvl = lvl;
    }
// ensures that GameID objects with the same values for mode and lvl will have the same hash code
    @Override
    public int hashCode() {
        return Objects.hash(mode, lvl);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        GameID other = (GameID) obj; //casts the argument to a GameID type
        return lvl == other.lvl && Objects.equals(mode, other.mode);
    }
}


