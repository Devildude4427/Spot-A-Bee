package com.assignment.spotabee;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;


// Taken from https://www.mkyong.com/android/android-spinner-drop-down-list-example/
public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//        EditText location = view.findViewById(R.id.locationField);
//        location.setText(parent.getSelectedItemPosition());

        Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
