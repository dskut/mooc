package edu.umn.cs.recsys.uu;

import java.util.*;

import org.grouplens.lenskit.basic.AbstractItemScorer;
import org.grouplens.lenskit.data.dao.UserDAO;
import org.grouplens.lenskit.data.dao.UserEventDAO;
import org.grouplens.lenskit.data.event.Rating;
import org.grouplens.lenskit.data.history.History;
import org.grouplens.lenskit.data.history.RatingVectorUserHistorySummarizer;
import org.grouplens.lenskit.data.history.UserHistory;
import org.grouplens.lenskit.vectors.ImmutableSparseVector;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;
import org.grouplens.lenskit.vectors.VectorEntry;
import org.grouplens.lenskit.vectors.similarity.CosineVectorSimilarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import it.unimi.dsi.fastutil.longs.LongSet;


class UserSimilarity {
    public long user;
    public double similarity;
    
    public UserSimilarity(long user, double similarity) {
        this.user = user;
        this.similarity = similarity;
    }
    
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "%d: %.4f", user, similarity);
    }
}

/**
 * User-user item scorer.
 * @author <a href="http://www.grouplens.org">GroupLens Research</a>
 */
public class SimpleUserUserItemScorer extends AbstractItemScorer {
    private static final Logger logger = LoggerFactory.getLogger("");
    private static final int neighborsCount = 30;
    private final UserEventDAO userEventDao;
    private final UserDAO userDao;
    
    @Inject
    public SimpleUserUserItemScorer(UserEventDAO uedao, UserDAO udao) {
        userEventDao = uedao;
        userDao = udao;
    }

    @Override
    public void score(long user, @Nonnull MutableSparseVector scores) {    
        logger.debug("user = " + user);
        
        SparseVector userVector = getUserRatingVector(user);
        double userMean = userVector.mean();
        
        logger.debug("userMean = " + userMean);
        
        SparseVector userSimilarities = getUserSimilarities(userVector);
        
        for (VectorEntry e: scores.fast(VectorEntry.State.EITHER)) {
            long item = e.getKey();
            logger.debug("item = " + item);
            
            List<UserSimilarity> neighbors = getNeighbors(userSimilarities, user, item);
            logger.debug("neighbors = " + neighbors.size() + ", " + neighbors);
            
            SparseVector neighborsSimilarities = makeSparseVector(neighbors);
            logger.debug("neighborsSimilarities = " + neighborsSimilarities);
            
            SparseVector neighborRatings = getRatingOffsets(neighbors, item);
            logger.debug("neighborRatings = " + neighborRatings);
            
            double neighborsWeightedRatingsSum = neighborsSimilarities.dot(neighborRatings);
            logger.debug("neighborsWeightedRatingsSum = " + neighborsWeightedRatingsSum);
            
            double neighborsSimilaritySum = getSumOfAbsValues(neighborsSimilarities);           
            logger.debug("neighborsSimilaritySum = " + neighborsSimilaritySum);
            
            double itemScore = userMean + neighborsWeightedRatingsSum / neighborsSimilaritySum;
            logger.debug("itemScore = " + itemScore);
            
            scores.set(item, itemScore);
        }
    }

    /**
     * Get a user's rating vector.
     * @param user The user ID.
     * @return The rating vector.
     */
    private SparseVector getUserRatingVector(long user) {
        UserHistory<Rating> history = userEventDao.getEventsForUser(user, Rating.class);
        if (history == null) {
            history = History.forUser(user);
        }
        return RatingVectorUserHistorySummarizer.makeRatingVector(history);
    }
    
    /**
     * Compute vector of similarities for the input user's ratings vector.
     * In this vector for each element the key is the user and the value is the similarity.
     * Similarity implemented as a cosine similarity between mean-centered ratings vector.
     * @param userVector vector of ratings for input user.
     * @return vector of similarities.
     */
    private SparseVector getUserSimilarities(SparseVector userVector) {
        Map<Long, Double> result = new HashMap<Long, Double>();
        SparseVector userVectorMeanCentered = getMeanCentered(userVector);
        LongSet userIds = userDao.getUserIds();
        for (Long user: userIds) {
            SparseVector vec = getUserRatingVector(user);
            SparseVector vecMeanCentered = getMeanCentered(vec);
            CosineVectorSimilarity cosineSim = new CosineVectorSimilarity();
            double sim = cosineSim.similarity(userVectorMeanCentered, vecMeanCentered);
            result.put(user, sim);
        }       
        return ImmutableSparseVector.create(result);
    }
    
    /**
     * Create a new vector where the mean of the vector is subtracted from each element.
     * @param vec input vector.
     * @return mean-centered vector.
     */
    private SparseVector getMeanCentered(SparseVector vec) {
        Map<Long, Double> result = new HashMap<Long, Double>();
        double mean = vec.mean();
        for (VectorEntry e: vec.fast(VectorEntry.State.SET)) {
            result.put(e.getKey(), e.getValue() - mean);
        }
        return ImmutableSparseVector.create(result);
    }

    /**
     * Retrieve the nearest neighbors who have rated the item.
     * @param userSimilarities vector of similarities between the input user
     * and all other users.
     * @param user the input user ID.
     * @param item the input item ID.
     * @return list of UserSimilarity objects (a pair of user ID and similarity value).
     */
    private List<UserSimilarity> getNeighbors(SparseVector userSimilarities, long user, long item) {    
        List<UserSimilarity> sortedSimilarities = sortUserSimilarities(userSimilarities);
        List<UserSimilarity> filteredSimilarities = filterUserSimilarities(sortedSimilarities, item);
        return getTopUsers(filteredSimilarities, user);
    }

    /**
     * Sort the user similarities vector by the value of similarity 
     * (the closer the user - the smaller the index of the value).
     * @param userSimilarities vector of similarity values.
     * @return list of UserSimilarity objects (a pair of user ID and similarity value).
     */
    private List<UserSimilarity> sortUserSimilarities(SparseVector userSimilarities) {
        List<UserSimilarity> list = makeUserSimilaritiesList(userSimilarities);
        Collections.sort(list, new Comparator<UserSimilarity>() {
            public int compare(UserSimilarity left, UserSimilarity right) {
                return (int)Math.signum(right.similarity - left.similarity);
            }
        });
        return list;
    }

    /**
     * Filter the list of user-similarities pairs, leaving only users 
     * who have rated the input item.
     * @param userSimilarities vector of similarity values.
     * @param item the item ID.
     * @return list of UserSimilarity objects (a pair of user ID and similarity value).
     */
    private List<UserSimilarity> filterUserSimilarities(List<UserSimilarity> userSimilarities, long item) {
        List<UserSimilarity> result = new ArrayList<UserSimilarity>();
        for (UserSimilarity userSim: userSimilarities) {
            if (existsRating(userSim.user, item)) {
                result.add(userSim);
            }
        }
        return result;
    }
    
    /**
     * Retrieve top N closes neighbors, excluding the input user himself.
     * @param userSimilarities
     * @param user input user ID.
     * @return list of UserSimilarity objects (a pair of user ID and similarity value).
     */
    private List<UserSimilarity> getTopUsers(List<UserSimilarity> userSimilarities, long user) {
        List<UserSimilarity> result = new ArrayList<UserSimilarity>(neighborsCount);
        for (UserSimilarity userSim: userSimilarities) {
            if (result.size() >= neighborsCount) {
                break;
            }
            if (userSim.user == user) {
                continue;
            }
            result.add(userSim);
        }
        return result;
    }
    
    /**
     * Compute vector of offsets of each user's rating on particular item from his average rating.
     * Only user field from UserSimilarity objects is used in this method.
     * @param userSimilarities list of UserSimilarity objects.
     * @param item the item ID, for which to compute ratings.
     * @return vector of ratings for the input item.
     */
    private SparseVector getRatingOffsets(List<UserSimilarity> userSimilarities, long item) {
        Map<Long, Double> result = new HashMap<Long, Double>();
        for (UserSimilarity userSimilarity: userSimilarities) {
            long user = userSimilarity.user;
            SparseVector userRatings = getUserRatingVector(user);
            double ratingOffset = userRatings.get(item) - userRatings.mean();
            result.put(user, ratingOffset);
        }
        return ImmutableSparseVector.create(result);
    }

    /**
     * Convert list of UserSimilarity objects to SparseVector with user similarities.
     * @param userSimilarities list of UserSimilarity objects (a pair of user ID and similarity value).
     * @return vector of similarity values.
     */
    private SparseVector makeSparseVector(List<UserSimilarity> userSimilarities) {
        Map<Long, Double> result = new HashMap<Long, Double>();
        for (UserSimilarity userSimilarity: userSimilarities) {
            result.put(userSimilarity.user, userSimilarity.similarity);
        }
        return ImmutableSparseVector.create(result);
    }
    
    /**
     * Convert SparseVector with user similarities to list of UserSimilarity objects.
     * @param userSimilarities vector of similarity values.
     * @return list of UserSimilarity objects (a pair of user ID and similarity value).
     */
    private List<UserSimilarity> makeUserSimilaritiesList(SparseVector userSimilarities) {
        List<UserSimilarity> result = new ArrayList<UserSimilarity>();
        for (VectorEntry e: userSimilarities.fast(VectorEntry.State.SET)) {
            result.add(new UserSimilarity(e.getKey(), e.getValue()));
        }
        return result;
    }

    /**
     * Compute the sum of absolute values of the elements in the vector.
     * @param vec input vector.
     * @return sum of absolute values.
     */
    private double getSumOfAbsValues(SparseVector vec) {
        double result = 0;
        for (VectorEntry e: vec.fast(VectorEntry.State.SET)) {
            result += Math.abs(e.getValue());
        }
        return result;
    }
    
    /**
     * Check if user has rated the item.
     * @param user the user ID.
     * @param item the item ID.
     * @return true if user rated item, false otherwise.
     */
    private boolean existsRating(long user, long item) {
        return getUserRatingVector(user).containsKey(item);
    }
}
