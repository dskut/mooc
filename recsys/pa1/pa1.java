import java.io.*;
import java.util.*;
import java.util.Map.Entry;

class Movie {
	public int id;
	public String title;
	
	public Movie(int id, String title) {
		this.id = id;
		this.title = title;
	}
}

class UserMovieRating {
	public int userId;
	public int movieId;
	public double rating;
	
	public UserMovieRating(int userId, int movieId, double rating) {
		this.userId = userId;
		this.movieId = movieId;
		this.rating = rating;
	}
}

class MovieScore {
	public int movieId;
	public double rating;
	
	public MovieScore(int movieId, double rating) {
		this.movieId = movieId;
		this.rating = rating;
	}
}

class MovieRecommendations {
	public int movieId;
	public MovieScore recommendations[];
	
	public MovieRecommendations(int movieId, MovieScore recommendations[]) {
		this.movieId = movieId;
		this.recommendations = recommendations;
	}
}

class RatingsMatrix {
	private Map<Integer, Map<Integer, Double>> userRatings;
	private Map<Integer, Map<Integer, Double>> movieRatings;
	
	public RatingsMatrix() {
		userRatings = new HashMap<Integer, Map<Integer, Double> >();
		movieRatings = new HashMap<Integer, Map<Integer, Double> >();
	}
	
	public void add(UserMovieRating rating) {
		if (userRatings.containsKey(rating.userId)) {
			userRatings.get(rating.userId).put(rating.movieId, rating.rating);
		} else {
			Map<Integer, Double> newMap = new HashMap<Integer, Double>();
			newMap.put(rating.movieId, rating.rating);
			userRatings.put(rating.userId, newMap);
		}
		
		if (movieRatings.containsKey(rating.movieId)) {
			movieRatings.get(rating.movieId).put(rating.userId, rating.rating);
		} else {
			Map<Integer, Double> newMap = new HashMap<Integer, Double>();
			newMap.put(rating.userId, rating.rating);
			movieRatings.put(rating.movieId, newMap);
		}
	}

	public int getCountByMovie(int inputMovie) {
		return movieRatings.get(inputMovie).size();
	}

	public Set<Integer> getMovies() {
		return movieRatings.keySet();
	}

	public int intersectionByMovie(int first, int second) {
		Set<Integer> firstUsers = movieRatings.get(first).keySet();
		Set<Integer> secondUsers = movieRatings.get(second).keySet();
		Set<Integer> inter = new HashSet<Integer>(firstUsers);
		inter.retainAll(secondUsers);
		return inter.size();
	}

	public int getIgnoreCountByMovie(int movieId) {
		return userRatings.size() - getCountByMovie(movieId);
	}

	public int intersectionByMovieIgnore(int ignoredMovieId, int movieId) {
		Set<Integer> allUsers = userRatings.keySet();
		Set<Integer> firstUsers = movieRatings.get(ignoredMovieId).keySet();
		Set<Integer> secondUsers = movieRatings.get(movieId).keySet();
		Set<Integer> res = new HashSet<Integer>(allUsers);
		res.removeAll(firstUsers);
		res.retainAll(secondUsers);
		return res.size();
	}
}

class NonPersRecommender {
	private static final int predictionsCount = 5;
	
    public static void main(String[] args) {
    	final String moviesPath = "recsys_data_movie-titles.csv";
    	final String ratingsPath = "recsys_data_ratings.csv";
    	final String outSimplePath = "simple.txt";
    	final String outAdvancedPath = "advanced.txt";
    	final int inputMovies[] = new int[] {278, 272, 603};
    	//final int inputMovies[] = new int[] {11, 121, 8587};
        try {
        	List<Movie> movies = readMovies(moviesPath);
        	List<UserMovieRating> ratings = readRatings(ratingsPath);
        	RatingsMatrix ratingsMatrix = createRatingsMatrix(ratings);
        	List<MovieRecommendations> simplePredictions = simplePredict(ratingsMatrix, inputMovies);
        	printPredictions(simplePredictions, outSimplePath);
        	List<MovieRecommendations> advancedPredictions = advancedPredict(ratingsMatrix, inputMovies);
        	printPredictions(advancedPredictions, outAdvancedPath);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

	private static RatingsMatrix createRatingsMatrix(List<UserMovieRating> ratings) {
		RatingsMatrix res = new RatingsMatrix();
		for (UserMovieRating rating: ratings) {
			res.add(rating);
		}
		return res;
	}

	private static MovieScore[] simpleRecommend(RatingsMatrix ratings, int inputMovie) {
		int inputMovieRatingsCount = ratings.getCountByMovie(inputMovie);
		List<MovieScore> scores = new ArrayList<MovieScore>();
		
		for (int movieId: ratings.getMovies()) {
			if (movieId == inputMovie) {
				continue;
			}			
			int intersection = ratings.intersectionByMovie(movieId, inputMovie);
			double score = new Double(intersection) / inputMovieRatingsCount;
			MovieScore movieScore = new MovieScore(movieId, score);
			scores.add(movieScore);
		}
		
		return selectTop(scores, predictionsCount);
	}

	private static MovieScore[] advancedRecommend(RatingsMatrix ratings, int inputMovie) {
		int inputMovieRatingsCount = ratings.getCountByMovie(inputMovie);
		int inputMovieIgnoreCount = ratings.getIgnoreCountByMovie(inputMovie);
		List<MovieScore> scores = new ArrayList<MovieScore>();
		
		for (int movieId: ratings.getMovies()) {
			if (movieId == inputMovie) {
				continue;
			}			
			int intersection = ratings.intersectionByMovie(movieId, inputMovie);
			double numerator = new Double(intersection) / inputMovieRatingsCount;
			double notIntersection = ratings.intersectionByMovieIgnore(inputMovie, movieId);
			double denominator = new Double(notIntersection) / inputMovieIgnoreCount;
			double score = numerator / denominator;
			MovieScore movieScore = new MovieScore(movieId, score);
			scores.add(movieScore);
		}
		
		return selectTop(scores, predictionsCount);
	}
	
	private static MovieScore[] selectTop(List<MovieScore> scores, int count) {
		Collections.sort(scores, new Comparator<MovieScore>() {
			public int compare(MovieScore first, MovieScore second) {
				return new Double(second.rating).compareTo(new Double(first.rating));
			}
		});
		return scores.subList(0, count).toArray(new MovieScore[count]);
	}
	
	private static List<MovieRecommendations> simplePredict(RatingsMatrix ratings, int[] inputMovies) {
		List<MovieRecommendations> res = new ArrayList<MovieRecommendations>();
		for (int inputMovie: inputMovies) {
			MovieScore recommendations[] = simpleRecommend(ratings, inputMovie);
			MovieRecommendations rec = new MovieRecommendations(inputMovie, recommendations);
			res.add(rec);
		}
		return res;
	}

	private static List<MovieRecommendations> advancedPredict(RatingsMatrix ratings, int[] inputMovies) {
		List<MovieRecommendations> res = new ArrayList<MovieRecommendations>();
		for (int inputMovie: inputMovies) {
			MovieScore recommendations[] = advancedRecommend(ratings, inputMovie);
			MovieRecommendations rec = new MovieRecommendations(inputMovie, recommendations);
			res.add(rec);
		}
		return res;
	}

	private static List<UserMovieRating> readRatings(String path) throws IOException {
		List<UserMovieRating> res = new ArrayList<UserMovieRating>();
		BufferedReader br = new BufferedReader(new FileReader(path));
		while (br.ready()) {
			String line = br.readLine();
			String pieces[] = line.split(",");
			int userId = Integer.parseInt(pieces[0]);
			int movieId = Integer.parseInt(pieces[1]);
			float rating = Float.parseFloat(pieces[2]);
			res.add(new UserMovieRating(userId, movieId, rating));
		}
		br.close();
		return res;
	}

	private static List<Movie> readMovies(String path) throws IOException {
		List<Movie> res = new ArrayList<Movie>();
		BufferedReader br = new BufferedReader(new FileReader(path));
		while (br.ready()) {
			String line = br.readLine();
			String pieces[] = line.split("[,\"]");
			int movieId = Integer.parseInt(pieces[0]);
			String title = pieces[1];
			res.add(new Movie(movieId, title));
		}
		br.close();
		return res;
	}

	private static void printPredictions(List<MovieRecommendations> predictions, String outPath) 
			throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(outPath, "UTF-8");
        for (MovieRecommendations prediction: predictions) {
	        StringBuilder sb = new StringBuilder();
	        sb.append(prediction.movieId);
	        for (MovieScore movieScore: prediction.recommendations) {
	            sb.append(',');
	            sb.append(movieScore.movieId);
	            sb.append(',');
	            sb.append(Math.round(movieScore.rating*100)/100.0);
	        }
	        writer.println(sb.toString());
        }
        writer.close();
	}
}
