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

    /**
     * CloudTile constructor
     *
     * @param cloudTileID specify {@link CloudTile#cloudTileID}
     * @param size        specify maximum students movable on the tile
     */
    public CloudTile(int cloudTileID, int size) {
        this.cloudTileID = cloudTileID;
        this.studentList = new ArrayList<>();
        this.maxSize = size;
    }

    /**
     * Check if cloudTile is empty
     *
     * @return true if cloud tile is empty, false otherwise
     */
    public boolean isEmpty() {
        return this.studentList.isEmpty();
    }

    /**
     * Put students inside on the cloud tile
     *
     * @param students list of students as parameters
     * @throws TooManyStudentsException if the cloud tile exceeds it's {@link CloudTile#maxSize}
     */
    public void put(Student... students) throws TooManyStudentsException {
        List<Student> incomingStudents = new ArrayList<>(Arrays.asList(students));
        if (studentList.size() + incomingStudents.size() > maxSize) throw new TooManyStudentsException();
        studentList.addAll(Arrays.asList(students));
    }

    /**
     * Get students on cloud tile as a list
     *
     * @return a {@link List} of {@link Student} containing students on cloud tile
     */
    public List<Student> getStudents() {
        return studentList;
    }

    /**
     * Remove students from cloud tile
     *
     * @return a {@link List} of {@link Student} containing all students removed from cloud tile
     */
    public List<Student> getAndRemoveStudents() {
        List<Student> students = new ArrayList<>(studentList);
        studentList.clear();
        return students;
    }

    /**
     * Generate a string containing cloud tile info
     *
     * @return a string containing students inside cloud tile
     */
    @Override
    public String toString() {
        String students = studentList.stream()
                .map(Student::toString)
                .collect(Collectors.joining(" ", "[", "]"));
        return "Cloud " + cloudTileID + students;
    }
}
