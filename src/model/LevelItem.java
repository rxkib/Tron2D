/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author rakib
 */
public enum LevelItem {
    WALL('#'),
    EMPTY(' '),
    RTopToLeft('q'),
    RTopToRight('q'),
    RDownToLeft('q'),
    RDownToRight('q'),
    CTopToLeft('q'),
    CTopToRight('q'),
    CDownToLeft('q'),
    CDownToRight('q'),
    GTopToLeft('q'),
    GTopToRight('q'),
    GDownToLeft('q'),
    GDownToRight('q'),
    MTopToLeft('q'),
    MTopToRight('q'),
    MDownToLeft('q'),
    MDownToRight('q'),
    MHorizontal('q'),
    MVertical('q'),
    GHorizontal('q'),
    GVertical('q'),
    CHorizontal('q'),
    CVertical('q'),
    RHorizontal('q'),
    RVertical('q'),
    DEFAULT('?');

    final char orientation;

    LevelItem(char or) {
        orientation = or;
    }

    public char getOrientation() {
        return orientation;
    }
}
