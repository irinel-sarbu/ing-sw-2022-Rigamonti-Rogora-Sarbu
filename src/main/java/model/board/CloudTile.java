package model.board;

import exceptions.TooManyStudentsException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CloudTile {
    private final int cloudTileID;
    private final List<Student> studentList;
    private final int maxSize;

    public CloudTile(int cloudTileID, int size) {
        this.cloudTileID = cloudTileID;
        this.studentList = new ArrayList<>();
        this.maxSize = size;
    }

    public boolean isEmpty() {
        return this.studentList.isEmpty();
    }

    public void put(Student... students) throws TooManyStudentsException {
        List<Student> incomingStudents = new ArrayList<>(Arrays.asList(students));
        if (studentList.size() + incomingStudents.size() > maxSize) throw new TooManyStudentsException();
        studentList.addAll(Arrays.asList(students));
    }

    public List<Student> getStudents() {
        return studentList;
    }

    public List<Student> getAndRemoveStudents() {
        List<Student> students = new ArrayList<>(studentList);
        studentList.clear();
        return students;
    }

    @Override
    public String toString() {
        String students = studentList.stream()
                .map(Student::toString)
                .collect(Collectors.joining(" ", "[", "]"));
        return "Cloud " + cloudTileID + students;
    }
}
