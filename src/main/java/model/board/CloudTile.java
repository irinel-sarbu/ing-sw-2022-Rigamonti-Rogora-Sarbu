package model.board;

import exceptions.TooManyStudentsException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CloudTile implements Serializable {
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
        return "Cloud_" + cloudTileID + ": " + students;
    }

    public static String allToString(List<CloudTile> boards) {
        List<String[]> splitCards = boards.stream()
                .map(CloudTile::toCard)
                .map(bs -> bs.split("\n"))
                .toList();

        StringBuilder cards = new StringBuilder();

        for (int y = 0; y < splitCards.get(0).length; y++) {
            for (String[] splitCard : splitCards) {
                cards.append(String.format("%-9s ", splitCard[y]));
            }
            cards.append("\n");
        }

        return cards.toString();
    }

    public String toCard() {
        StringBuilder card = new StringBuilder();
        card.append(" ┌─────┐ \n");
        card.append("┌┘").append("  ").append(cloudTileID).append("  ").append("└┐\n");
        card.append("│ ").append(buildRow(true)).append(" │\n");
        card.append("└┐").append(buildRow(false)).append("┌┘\n");
        card.append(" └─────┘ \n");

        return card.toString();
    }

    private String buildRow(boolean firstRow) {
        int students = studentList.size();
        StringBuilder row = new StringBuilder();
        switch (students) {
            case 0 -> row.append("     \n");
            case 3 -> {
                if (firstRow)
                    row.append(" ").append(studentList.get(0)).append(" ").append(studentList.get(1)).append(" ");
                else
                    row.append("  ").append(studentList.get(2)).append("  ");
            }
            case 4 -> {
                if (firstRow)
                    row.append(" ").append(studentList.get(0)).append(" ").append(studentList.get(1)).append(" ");
                else
                    row.append(" ").append(studentList.get(2)).append(" ").append(studentList.get(3)).append(" ");
            }
        }

        return row.toString();
    }
}
