package util.unused.characterCards;

import exceptions.EmptyStudentListException;
import exceptions.StudentNotFoundException;
import model.board.Student;
import model.expert.Character;
import util.CharacterType;

import java.util.ArrayList;
import java.util.List;

public class Princess extends Character {
    private final List<Student> students;

    public Princess() {
        super(2, CharacterType.PRINCESS);
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public void remove(int position) throws EmptyStudentListException, StudentNotFoundException {
        if (position > this.students.size() || position < 0) throw new StudentNotFoundException();
        if (this.students.size() == 0) throw new EmptyStudentListException();
        this.students.remove(position);
    }

    public List<Student> getStudents() {
        return students;
    }
}
