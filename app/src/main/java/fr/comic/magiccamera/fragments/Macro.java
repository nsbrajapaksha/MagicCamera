package fr.comic.magiccamera.fragments;


import java.util.Queue;

import fr.comic.magiccamera.image.ImageEffect;

/**
 * Using a card patern, see  {@link MacroAdapter}.
 * But also a class to represent a macro of several effects.
 */
public class Macro {


    private String name;
    private String info;

    private Queue<ImageEffect> effects;

    /**
     * Constrcutor
     *
     * @param name    First line, name of the macro
     * @param info    Second line to print
     * @param effects FIFO of effects  to apply for the personal effect.
     */
    public Macro(String name, String info, Queue<ImageEffect> effects) {
        this.name = name;
        this.info = info;
        this.effects = effects;
    }


    /**
     * @return Name of the personal effect macro
     */
    String getName() {
        return name;
    }

    /**
     * @return Second line of the personal effect macro
     */
    String getInfoLine() {
        return info;
    }

    /**
     * @return FIFO of effects to apply
     */
    Queue<ImageEffect> getEffects() {
        return effects;
    }

    /**
     * Change name String
     *
     * @param name new Name
     */
    public void setName(String name) {
        this.name = name;
    }
}
