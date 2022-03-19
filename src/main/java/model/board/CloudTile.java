package model.board;

import util.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CloudTile {
    private final List<Student> studentList;
    private int maxSize;

    public CloudTile(int num) {
        this.studentList = new ArrayList<>();
        this.maxSize = num;
    }

    public void put(Student... students) {
        studentList.addAll(Arrays.asList(students));
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void removeStudents(){
        studentList.clear();
    }
}
