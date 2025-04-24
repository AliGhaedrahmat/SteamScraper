import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Parser {
    static List<Game> games = new ArrayList<>();

    public List<Game> sortByName() {
        List<Game> sortedByName = new ArrayList<>(games);
        sortedByName.sort(Comparator.comparing(Game::getName));
        return sortedByName;
    }

    public List<Game> sortByRating() {
        List<Game> sortedByRating = new ArrayList<>(games);
        sortedByRating.sort(Comparator.comparing(Game::getRating).reversed());
        return sortedByRating;
    }

    public List<Game> sortByPrice() {
        List<Game> sortedByPrice = new ArrayList<>(games);
        sortedByPrice.sort(Comparator.comparing(Game::getPrice).reversed());
        return sortedByPrice;
    }

    public void setUp() {
        try {
            File input = new File("src/Resources/Video_Games.html");
            Document doc = Jsoup.parse(input, "UTF-8");
            Elements gameElements = doc.select(".game");

            for (Element gameElement : gameElements) {
                String name = gameElement.select(".game-name").text();
                String ratingText = gameElement.select(".game-rating").text().replace("Rating: ", "");
                String[] ratingParts = ratingText.split("/");
                double rating = 0.0;
                if (ratingParts.length > 0 && !ratingParts[0].isEmpty()) {
                    try {
                        rating = Double.parseDouble(ratingParts[0].trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid rating value: " + ratingText);
                    }
                }

                String priceText = gameElement.select(".game-price").text().replace("Price: ", "");
                int price = 0;
                if (!priceText.isEmpty()) {
                    try {
                        price = Integer.parseInt(priceText.replace("€", "").trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid price value: " + priceText);
                    }
                }

                games.add(new Game(name, rating, price));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
