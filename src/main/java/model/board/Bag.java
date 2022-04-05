package model.board;

import exceptions.EmptyStudentListException;
import util.Color;

import java.util.*;
import java.util.stream.Collectors;

public class Bag {
    private final List<Student> studentList;
    private int studentIdCount = 0;

    public Bag(int num) {
        this.studentList = new ArrayList<>();
        for (Color color : Color.values()) {
            for (int i = 0; i < num; i++) {
                this.studentList.add(new Student(studentIdCount, color));
                studentIdCount += 1;
            }
        }
    }

    public Bag(int yellow, int blue, int green, int red, int pink) {
        this.studentList = new ArrayList<>();

        for (int y = 0; y < yellow; y++) {
            this.studentList.add(new Student(studentIdCount, Color.YELLOW));
            studentIdCount += 1;
        }

        for (int b = 0; b < blue; b++){
            this.studentList.add(new Student(studentIdCount, Color.BLUE));
            studentIdCount += 1;
        }

        for (int g = 0; g < green; g++){
            this.studentList.add(new Student(studentIdCount, Color.GREEN));
            studentIdCount += 1;
        }

        for (int r = 0; r < red; r++){
            this.studentList.add(new Student(studentIdCount, Color.RED));
            studentIdCount += 1;
        }

        for (int p = 0; p < pink; p++) {
            this.studentList.add(new Student(studentIdCount, Color.PINK));
            studentIdCount += 1;
        }
    }

    public int getRemainingStudents() {
        return studentList.size();
    }

    public boolean isEmpty() {
        return getRemainingStudents() == 0;
    }

    private void shuffle() {
        Collections.shuffle(studentList);
    }

    public void put(Student... students) {
        studentList.addAll(Arrays.asList(students));
        shuffle();
    }

    public Student pull() throws EmptyStudentListException {
        if (studentList.size() == 0) throw new EmptyStudentListException();
        shuffle();
        return studentList.remove(0);
    }

    public String toString(boolean printContent) {
        String stringContent = printContent ? studentList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Student::getColor, Collectors.counting()))
                .entrySet().stream()
                .map(map -> map.getKey().toString() + ":" + String.format("%2s", map.getValue().toString()))
                .collect(Collectors.joining(" ", "\n\t[", "]")) : "";
        return "Bag: " + studentList.size() + " students remaining" + stringContent;
    }

    @Override
    public String toString() {
        return toString(false);
    }
}