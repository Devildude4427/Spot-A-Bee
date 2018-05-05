package com.assignment.spotabee;


import com.assignment.spotabee.customutils.CustomQuickSort;
import com.assignment.spotabee.database.UserScore;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;


import static junit.framework.Assert.assertEquals;


public class TestCustomQuickSort {
    List<UserScore> unsortedUserScores;
    UserScore onePoint;
    UserScore sixPoits;
    UserScore thirtyPoints;
    UserScore sixtyPoints;


    @Before
    public void initialise(){

       onePoint = new UserScore("Test User 1", 1);
       sixPoits = new UserScore("Test User 2", 6);
       thirtyPoints = new UserScore("Test User 3", 30);
       sixtyPoints = new UserScore("Test User 4", 60);

        unsortedUserScores = Arrays.asList(thirtyPoints, sixPoits, sixtyPoints, onePoint);
    }

    // Tests that our CustomQuickSort orders UserScore objects from largest to smallest.
    // The UserScore with the largest score value should be placed at the beginning of the List
    @Test
    public void testCustomQuickSort(){
        CustomQuickSort customQuickSort = new CustomQuickSort();
        customQuickSort.sort(unsortedUserScores, 0, unsortedUserScores.size()-1);

        assertEquals(60, unsortedUserScores.get(0).getScore());
        assertEquals(30, unsortedUserScores.get(1).getScore());
        assertEquals(6, unsortedUserScores.get(2).getScore());
        assertEquals(1, unsortedUserScores.get(3).getScore());
    }
}
