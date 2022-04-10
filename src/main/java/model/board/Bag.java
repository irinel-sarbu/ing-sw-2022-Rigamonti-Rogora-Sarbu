package model.board;

import exceptions.EmptyStudentListException;
import util.Color;

import java.util.*;
import java.util.stream.Collectors;

public class Bag {
    private final List<Student> studentList;
    private int studentIdCount = 0;

    /**
     * Bag generic constructor for a game
     *
     * @param num specify number of new students of each color to put in the bag
     */
    public Bag(int num) {
        this.studentList = new ArrayList<>();
        for (Color color : Color.values()) {
            for (int i = 0; i < num; i++) {
                this.studentList.add(new Student(studentIdCount, color));
                studentIdCount += 1;
            }
        }
    }

    /**
     * Bag specific constructor for a game
     *
     * @param yellow specify number of yellow students to put in the bag
     * @param blue   specify number of blue students to put in the bag
     * @param green  specify number of green students to put in the bag
     * @param red    specify number of red students to put in the bag
     * @param pink   specify number of pink students to put in the bag
     */
    public Bag(int yellow, int blue, int green, int red, int pink) {
        this.studentList = new ArrayList<>();

        for (int y = 0; y < yellow; y++) {
            this.studentList.add(new Student(studentIdCount, Color.YELLOW));
            studentIdCount += 1;
        }

        for (int b = 0; b < blue; b++) {
            this.studentList.add(new Student(studentIdCount, Color.BLUE));
            studentIdCount += 1;
        }

        for (int g = 0; g < green; g++){
            this.studentList.add(new Student(studentIdCount, Color.GREEN));
            studentIdCount += 1;
        }

        for (int r = 0; r < red; r++) {
            this.studentList.add(new Student(studentIdCount, Color.RED));
            studentIdCount += 1;
        }

        for (int p = 0; p < pink; p++) {
            this.studentList.add(new Student(studentIdCount, Color.PINK));
            studentIdCount += 1;
        }
    }

    /**
     * Getter for remaining students
     *
     * @return number of students still inside the bag
     */
    public int getRemainingStudents() {
        return studentList.size();
    }

    /**
     * Check if bag is empty
     *
     * @return true if the bag is empty, false otherwise
     */
    public boolean isEmpty() {
        return getRemainingStudents() == 0;
    }

    /**
     * Shuffle bag content to randomize student extraction
     */
    private void shuffle() {
        Collections.shuffle(studentList);
    }

    /**
     * Put given students into bag
     *
     * @param students list of students
     */
    public void put(Student... students) {
        studentList.addAll(Arrays.asList(students));
        shuffle();
    }

    public Student pull() throws EmptyStudentListException {
        if (studentList.size() == 0) throw new EmptyStudentListException();
        shuffle();
        return studentList.remove(0);
    }

    /**
     * Allow toString to print additional content using a parameter
     *
     * @param printContent specify to print exact number of students for each color
     * @return A string indicating remaining students and bag content if specified
     */
    public String toString(boolean printContent) {
        String stringContent = printContent ? studentList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Student::getColor, Collectors.counting()))
                .entrySet().stream()
                .map(map -> map.getKey().toString() + ":" + String.format("%2s", map.getValue().toString()))
                .collect(Collectors.joining(" ", "\n\t[", "]")) : "";
        return "Bag: " + studentList.size() + " students remaining" + stringContent;
    }

    /**
     * Overrides toString
     *
     * @return A string indicating number of remaining students by calling {@link Bag#toString(false)}
     */
    @Override
    public String toString() {
        return toString(false);
    }
}