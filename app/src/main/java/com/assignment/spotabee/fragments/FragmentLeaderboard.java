package com.assignment.spotabee.fragments;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.assignment.spotabee.R;
import com.assignment.spotabee.customutils.CustomQuickSort;
import com.assignment.spotabee.database.AppDatabase;
import com.assignment.spotabee.database.UserScore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentLeaderboard extends Fragment {

    private View rootView;
    private AppDatabase db;

    /**
     * On create of the fragment, loads the layout
     * of the page.
     *
     * @param inflater Creates the layout for the fragment.
     * @param container Assigns the overall container
     *                  that the fragment sits in.
     * @param savedInstanceState Save the state so that the
     *                           fragment can be opened and
     *                           shut without losing your
     *                           changes.
     * @return The finished view for the fragment.
     */
    @Nullable
    @Override
    public View onCreateView(final @NonNull LayoutInflater inflater,
                             final @Nullable ViewGroup container,
                             final @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        rootView = inflater.inflate(R.layout.fragment_menu_leaderboard, container, false);
        db = AppDatabase.getAppDatabase(getContext());

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                List<UserScore> userScores = db.descriptionDao().getAllUserScores();

                CustomQuickSort cqs = new CustomQuickSort();
                cqs.sort(userScores, 0, userScores.size()-1);
                doCustomAdapterExample(userScores);
            }
        });
        return rootView;
    }

    /**
     * Once the view is created, it sets the title
     * and will handle any other fragment methods.
     *
     * @param view The view return from 'onCreateView'
     * @param savedInstanceState What instance state the
     *                           fragment is currently on.
     */
    @Override
    public void onViewCreated(final @NonNull View view,
                              final @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Leaderboard");
    }

    private void doCustomAdapterExample(final List<UserScore> userScores){


//        ListViewCompat lv = rootView.findViewById(R.id.userScoreLv); //Make sure that your listview in your layout file has this id
//        lv.setAdapter(myCustomAdapter);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<UserScore> arrlistOfBooks= new ArrayList<UserScore>(userScores);
                LeaderBoardAdapter leaderBoardAdapter = new LeaderBoardAdapter(
                        getActivity(),
                        R.layout.gray_user_score_layout,
                        arrlistOfBooks
                );
                ListView testListView = rootView.findViewById(R.id.testListView);
                testListView.setAdapter(leaderBoardAdapter);
            }
        });


    }



    //Custom adapter used in Example 2
    private class LeaderBoardAdapter extends BaseAdapter {

        private Context context;
        private ArrayList data;
        private int layoutToUseForTheRows;

        LeaderBoardAdapter(Context context, int layout, ArrayList data) {
            this.context = context;
            this.layoutToUseForTheRows = layout;
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null){
                view = LayoutInflater.from(this.context).inflate(this.layoutToUseForTheRows, viewGroup, false);
            }


            UserScore userScore = (UserScore) this.getItem(i);

            try {
                AppCompatTextView topText = view.findViewById(R.id.user_name);
                topText.setText(userScore.getAccountName());

                AppCompatTextView bottomText = view.findViewById(R.id.user_score);
                String score = "Score: " + userScore.getScore();
                bottomText.setText(score);
            } catch (Exception e){
                Toast.makeText(getActivity(),
                        "Error",
                        Toast.LENGTH_SHORT).show();
            }

            return view;
        }
    }
}
