package model.board;

import util.Color;

import java.util.*;
import java.util.stream.Stream;

public class Bag {
    private final List<Student> studentList;

    public Bag(int num) {

        this.studentList = new ArrayList<>();
        /*
         * for (int y = 0; y < num; y++)
         * studentList.add(new Student(Color.YELLOW));
         * 
         * for (int b = 0; b < num; b++)
         * studentList.add(new Student(Color.BLUE));
         * 
         * for (int g = 0; g < num; g++)
         * studentList.add(new Student(Color.GREEN));
         * 
         * for (int r = 0; r < num; r++)
         * studentList.add(new Student(Color.RED));
         * 
         * for (int p = 0; p < num; p++)
         * studentList.add(new Student(Color.PINK));
         */
        for (Color color : Color.values()) { // TODO: vedere se sta cosa funziona
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

    private void shuffle() {
        Collections.shuffle(studentList);
    }

    public void put(Student... students) {
        studentList.addAll(Arrays.asList(students));
        shuffle();
    }

    public Student pull() {
        shuffle();
        return studentList.remove(0);
    }
}