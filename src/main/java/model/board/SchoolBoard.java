package model.board;

import util.Color;
import util.TowerColor;

import java.util.*;

public class SchoolBoard {
    private final List<Student> entrance;
    private int[] table; //yellow = 0, blue = 1, green = 2, red = 3, pink = 4;
    private final List<Professor> professors;

    public SchoolBoard() {
        this.entrance = new ArrayList<>();
        table = new int[5];
        professors = new ArrayList<>();
    }
}
