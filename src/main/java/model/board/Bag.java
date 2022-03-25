package model.board;

import exceptions.EmptyStudentListException;
import util.Color;

import java.util.*;
import java.util.stream.Collectors;

public class Bag {
    private final List<Student> studentList;

    public Bag(int num) {
        this.studentList = new ArrayList<>();
        for (Color color : Color.values()) {
            studentList.addAll(Collections.nCopies(num, new Student(color)));
        }
    }

    public Bag(int yellow, int blue, int green, int red, int pink) {
        this.studentList = new ArrayList<>();

        for (int y = 0; y < yellow; y++)
            studentList.add(new Student(Color.YELLOW));

        for (int b = 0; b < blue; b++)
            studentList.add(new Student(Color.BLUE));

        for (int g = 0; g < green; g++)
            studentList.add(new Student(Color.GREEN));

        for (int r = 0; r < red; r++)
            studentList.add(new Student(Color.RED));

        for (int p = 0; p < pink; p++)
            studentList.add(new Student(Color.PINK));
    }

    public int getRemainingStudents() {
        return studentList.size();
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