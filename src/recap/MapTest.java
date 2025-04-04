package recap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MapTest {
    static class Student{
        String name;
        String subject;
        double score;
        int timeSpent;

        public Student(String name, String subject, double score, int timeSpent) {
            this.name = name;
            this.subject = subject;
            this.score = score;
            this.timeSpent = timeSpent;
        }

        public String getName() {
            return name;
        }

        public String getSubject() {
            return subject;
        }

        public double getScore() {
            return score;
        }

        public int getTimeSpent() {
            return timeSpent;
        }

        @Override
        public String toString() {
            return name + "/" + subject + "/" + score + "/" + timeSpent;
        }
    }

    public static void main(String[] args) throws IOException {
        List<Student> students = Files.lines(Paths.get("students.csv"))
                .skip(1)
                .map(line -> line.split(","))
                .map(split ->new Student(
                        split[0],
                       split[1],
                        Double.parseDouble(split[2]),
                        Integer.parseInt(split[3])
                )).toList();

        students.stream()//1
                .filter(s -> s.getSubject().equals("Biology") && s.getTimeSpent() < 50 && s.getScore() > 80)
                .forEach(System.out::println);

        long chemAll = students.stream()
                .filter(s -> s.getSubject().equals("Chemistry"))
                .count();
        System.out.println(chemAll);

        long chemCount = students.stream()
                .filter(s -> s.getSubject().equals("Chemistry") && s.getScore() < 30)
                .count();
        System.out.println(chemCount);

        double avg = ((double)chemCount/chemAll)*100.0;
        System.out.println(avg);//2

        List<Student> distinctItems = students.stream()//3
                .distinct()
                .collect(Collectors.toList());
        System.out.println(students.size()-distinctItems.size());


        HashMap<String, List<Integer>> studentMap = new HashMap<>();
        for(Student student : students){
            if (studentMap.containsKey(student.getSubject())){
                studentMap.get(student.getSubject()).add(student.getTimeSpent());
            }else {
                ArrayList<Integer> newSubject = new ArrayList<>();
                newSubject.add(student.getTimeSpent());
                studentMap.put(student.getSubject(), newSubject );
            }
        }
       students.stream().collect(Collectors.groupingBy(Student::getSubject, Collectors.averagingInt(Student::getTimeSpent) ))
       students.stream().collect(Collectors.groupingBy(Student::getSubject, Collectors.counting()) );

        double avgTime;

        for (String subject : studentMap.keySet()){
            System.out.println(subject);
            avgTime = studentMap.get(subject).stream()
                    .mapToInt(Integer::intValue)
                    .average()
                    .orElse(0);
            System.out.print(avgTime);
        }










    }
}
