package recap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Mapping {
    static class Movie{
        String name;
        int year;
        String genre;
        double rating;

        public String getName() {
            return name;
        }

        public int getYear() {
            return year;
        }

        public String getGenre() {
            return genre;
        }

        public double getRating() {
            return rating;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public Movie(String name, int year, String genre, double rating) {
            this.name = name;
            this.year = year;
            this.genre = genre;
            this.rating = rating;
        }

        @Override
        public String toString() {
            return  name;
        }
    }

    public static void main(String[] args) throws IOException {
        List<Movie> movies = Files.lines(Paths.get("movieList.txt"))//rovnou streamuju
                //.skip(1) - můžu skipnout řádek
                .map(line -> line.split(";"))
                .map(split ->new Movie(
                        split[0],
                        Integer.parseInt(split[1]),
                        split[2],
                        Double.parseDouble(split[3])
                )).toList();

        movies.stream()
                .sorted(Comparator.comparingDouble(Movie::getRating).reversed())//reversed abych dal nejhorších 5
                .limit(5)//top 5
                .forEach(m -> System.out.println(m.getName() + " [" + m.getRating() + "/10]"));

        long newer = movies.stream()//počet filmů po 2000
                .filter(movie -> movie.getYear() > 2000)
                .count();

        double avgRating = movies.stream()
                .filter(m -> m.getGenre().equals("Horror"))
                .mapToDouble(Movie::getRating)
                .average()
                .orElse(0);

        movies.stream()
                .filter(m -> m.getYear() < 2000)
                .sorted(Comparator.comparingInt(Movie::getYear).reversed())
                .limit(10)
                .forEach(System.out::println);

        HashMap<String, List<Movie>> genreMap = new HashMap<>(); //celé na potítku
        for(Movie movie : movies){
            if (genreMap.containsKey(movie.getGenre())){
                genreMap.get(movie.getGenre()).add(movie);
            }else {
                ArrayList<Movie> newGenre = new ArrayList<>();
                newGenre.add(movie);
                genreMap.put(movie.getGenre(), newGenre);
            }
        }
        for (String genre : genreMap.keySet()){
            System.out.println(genre);
            for (Movie m : genreMap.get(genre)){
                System.out.println("\t" + m.getName());
            }
        }

        Map<String, List<Movie>> genreMapToo = movies.stream()//ekvivalent
                .collect(Collectors.groupingBy(Movie::getGenre));
    }
}
