import java.io.*;
import java.util.ArrayList;
import java.util.List;

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

class NonPersRecommender {
	private static final int predictionsCount = 5;
	
    public static void main(String[] args) {
    	final String moviesPath = "recsys_data_movie-titles.csv";
    	final String ratingsPath = "recsys_data_ratings.csv";
    	final String outSimplePath = "simple.txt";
    	final String outAdvancedPath = "advanced.txt";
    	final int inputMovies[] = new int[] {278, 272, 603};
        try {
        	List<Movie> movies = readMovies(moviesPath);
        	List<UserMovieRating> ratings = readRatings(ratingsPath);
        	List<MovieRecommendations> simplePredictions = simplePredict(movies, ratings, inputMovies);
        	printPredictions(simplePredictions, outSimplePath);
        	List<MovieRecommendations> advancedPredictions = advancedPredict(movies, ratings, inputMovies);
        	printPredictions(advancedPredictions, outAdvancedPath);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

	private static MovieScore[] simpleRecommend(List<Movie> movies,
		List<UserMovieRating> ratings, int inputMovie) 
	{
		MovieScore res[] = new MovieScore[predictionsCount];
		for (int i = 0; i < predictionsCount; ++i) {
			res[i] = new MovieScore(inputMovie, (i+1)*0.5);
		}
		return res;
	}
	
	private static MovieScore[] advancedRecommend(List<Movie> movies,
		List<UserMovieRating> ratings, int inputMovie) 
	{
		MovieScore res[] = new MovieScore[predictionsCount];
		for (int i = 0; i < predictionsCount; ++i) {
			res[i] = new MovieScore(inputMovie, (i+1)*0.5);
		}
		return res;
	}
	
	private static List<MovieRecommendations> simplePredict(List<Movie> movies, 
			List<UserMovieRating> ratings, int[] inputMovies) 
	{
		List<MovieRecommendations> res = new ArrayList<MovieRecommendations>();
		for (int inputMovie: inputMovies) {
			MovieScore recommendations[] = simpleRecommend(movies, ratings, inputMovie);
			MovieRecommendations rec = new MovieRecommendations(inputMovie, recommendations);
			res.add(rec);
		}
		return res;
	}

	private static List<MovieRecommendations> advancedPredict(List<Movie> movies,	
		List<UserMovieRating> ratings, int[] inputMovies) 
	{
		List<MovieRecommendations> res = new ArrayList<MovieRecommendations>();
		for (int inputMovie: inputMovies) {
			MovieScore recommendations[] = advancedRecommend(movies, ratings, inputMovie);
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
	            sb.append(movieScore.rating);
	        }
	        writer.println(sb.toString());
        }
        writer.close();
	}
}
