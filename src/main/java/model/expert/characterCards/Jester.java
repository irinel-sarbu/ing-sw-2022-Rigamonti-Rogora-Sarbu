package model.expert.characterCards;

import model.board.Student;
import model.expert.Character;
import util.CharacterName;

import java.util.ArrayList;
import java.util.List;

public class Jester extends Character {

    List<Student> studentList;

    public Jester() {
        super(1, CharacterName.JESTER);
        studentList = new ArrayList<>();
    }
}
