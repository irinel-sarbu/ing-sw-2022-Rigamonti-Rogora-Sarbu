package model.expert;

import com.sun.source.tree.ReturnTree;
import exceptions.EmptyNoEntryListException;
import exceptions.EmptyStudentListException;
import exceptions.StudentNotFoundException;
import model.board.Student;
import util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CharacterCard {
    private int cost;
    private CharacterType character;
    private boolean effectIsUsed;
    private List<Student> students;
    private Stack<NoEntryTile> noEntryTiles;


    public CharacterCard(CharacterType character) {
        this.cost = character.getBaseCost();
        this.character = character;
        switch (character) {
            case MONK, PRINCESS -> {
                students = new ArrayList<>(4);
            }
            case GRANNY_HERBS -> {
                noEntryTiles = new Stack<>();
            }
            case JESTER -> {
                students = new ArrayList<>(6);
            }
            default -> {
            }
        }
        resetEffect();
    }

    public CharacterCard(int cost, CharacterType character) {
        this.cost = cost;
        this.character = character;
        resetEffect();
    }

    public CharacterType getCharacter() {
        return character;
    }

    public void useEffect() {
        this.effectIsUsed = true;
    }

    public void resetEffect() {
        this.effectIsUsed = false;
    }

    public boolean getEffect() {
        return effectIsUsed;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void addStudent(Student student){
        students.add(student);
    }

    public List<Student> getStudents(){
        return students;
    }

    public Student removeStudent(int studentId) throws StudentNotFoundException, EmptyStudentListException {
        if (this.students.size() == 0) throw new EmptyStudentListException();
        for(Student student : students){
            if(student.getID()==studentId){
                students.remove(student);
                return student;
            }
        }
        throw new StudentNotFoundException();
    }

    public void addNoEntryTile(NoEntryTile noEntryTile){
        noEntryTiles.push(noEntryTile);
    }

    public Stack<NoEntryTile> getNoEntryTiles() {
        return noEntryTiles;
    }

    public NoEntryTile removeNoEntryTile() throws EmptyNoEntryListException {
        if (this.noEntryTiles.size() == 0) throw new EmptyNoEntryListException();
        return noEntryTiles.pop();
    }
}
