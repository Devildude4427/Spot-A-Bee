package com.assignment.spotabee.customutils;

import com.assignment.spotabee.database.UserScore;

import java.util.Collections;
import java.util.List;

// Java program for implementation of QuickSort
public class CustomQuickSort
{
    /* This function takes last element as pivot,
       places the pivot element at its correct
       position in sorted array, and places all
       smaller (smaller than pivot) to left of
       pivot and all greater elements to right
       of pivot */
    int partition(List<UserScore> arr, int low, int high)
    {
        int pivot = arr.get(high).getScore();
        int i = (low-1); // index of smaller element
        for (int j=low; j<high; j++)
        {
            // If current element is smaller than or
            // equal to pivot
            if (arr.get(j).getScore() <= pivot)
            {
                i++;

                // swap arr[i] and arr[j]
                UserScore temp = arr.get(i);
                Collections.swap(arr, i, j);
                temp = arr.get(j);
            }
        }

        // swap arr[i+1] and arr[high] (or pivot)
        UserScore temp = arr.get(i+1);
        Collections.swap(arr, i+1, high);
        temp = arr.get(high);

        return i+1;
    }


    /* The main function that implements QuickSort()
      arr[] --> Array to be sorted,
      low  --> Starting index,
      high  --> Ending index */
    public void sort(List<UserScore> arr, int low, int high)
    {
        if (low < high)
        {
            /* pi is partitioning index, arr[pi] is
              now at right place */
            int pi = partition(arr, low, high);

            // Recursively sort elements before
            // partition and after partition
            sort(arr, low, pi-1);
            sort(arr, pi+1, high);
        }
    }

    /* A utility function to print array of size n */
    static void printArray(int arr[])
    {
        int n = arr.length;
        for (int i=0; i<n; ++i)
            System.out.print(arr[i]+" ");
        System.out.println();
    }

//    // Driver program
//    public static void main(String args[])
//    {
//        int arr[] = {10, 7, 8, 9, 1, 5};
//        int n = arr.length;
//
//        CustomQuickSort ob = new CustomQuickSort();
//        ob.sort(arr, 0, n-1);
//
//        System.out.println("sorted array");
//        printArray(arr);
//    }
}
/*This code is contributed by Rajat Mishra */