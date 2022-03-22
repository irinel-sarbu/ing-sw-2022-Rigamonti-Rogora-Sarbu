package util.unused.characterCards;

import exceptions.EmptyStudentListException;
import exceptions.StudentNotFoundException;
import model.board.Student;
import model.expert.Character;
import util.CharacterName;

import java.util.ArrayList;
import java.util.List;

public class Jester extends Character {

    List<Student> studentList;

    public Jester() {
        super(1, CharacterName.JESTER);
        this.studentList = new ArrayList<>();
    }

    public List<Student> getStudents() {
        return new ArrayList<>(this.studentList);
    }

    public Student removeStudent(int studentPosition) throws StudentNotFoundException, EmptyStudentListException {
        if (this.studentList.size() == 0) throw new EmptyStudentListException();
        if (studentPosition >= this.studentList.size()) throw new StudentNotFoundException();
        return studentList.remove(studentPosition);
    }

    public void addStudent(Student student) {
        this.studentList.add(student);
    }

}