package model.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CloudTile {
    private final int cloudTileID;
    private final List<Student> studentList;
    private int maxSize;

    public CloudTile(int cloudTileID, int num) {
        this.cloudTileID = cloudTileID;
        this.studentList = new ArrayList<>();
        this.maxSize = num;
    }

    public void put(Student... students) {
        studentList.addAll(Arrays.asList(students));
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void removeStudents() {
        studentList.clear();
    }

    @Override
    public String toString() {
        String students = studentList.stream()
                .map(Student::toString)
                .collect(Collectors.joining(" ", "[", "]"));
        return "Cloud " + cloudTileID + students;
    }
}
