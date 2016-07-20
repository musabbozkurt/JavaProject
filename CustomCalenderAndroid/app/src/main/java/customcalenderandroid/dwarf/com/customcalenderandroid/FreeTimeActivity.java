package customcalenderandroid.dwarf.com.customcalenderandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

import java.util.ArrayList;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;

public class FreeTimeActivity extends AppCompatActivity {
    private ExpandableListView mExpandableList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_time);
        mExpandableList = (ExpandableListView)findViewById(R.id.expandableListView);

        ArrayList<ListItemParentActivity.Parent> arrayParents = new ArrayList<ListItemParentActivity.Parent>();
        ArrayList<String> arrayChildren = new ArrayList<String>();

        //here we set the parents and the children
        //for (int i = 0; i < 10; i++){
        //for each "i" create a new Parent object to set the title and the children
        ListItemParentActivity.Parent parent = new ListItemParentActivity.Parent();
        parent.setTitle("College Turkey ");

        arrayChildren = new ArrayList<String>();
        // for (int j = 0; j < 10; j++) {
        arrayChildren.add("Date : 7-05-2016 21:00-23:00 ");
        arrayChildren.add("Where : Zorlu Center - Performans Sanatları Merkezi - İstanbul  ");
        // }
        parent.setArrayChildren(arrayChildren);

        //in this array we add the Parent object. We will use the arrayParents at the setAdapter
        arrayParents.add(parent);
        ListItemParentActivity.Parent parent2 = new ListItemParentActivity.Parent();
        parent2.setTitle("Sara Baras Istanbul ");

        ArrayList<String> arrayChildren2 = new ArrayList<String>();
        // for (int j = 0; j < 10; j++) {
        arrayChildren2.add("Date : 7-05-2016 16:00-18:00 ");
        arrayChildren2.add("Where : Zorlu Center PSM - İstanbul  ");
        // }
        parent2.setArrayChildren(arrayChildren2);

        //in this array we add the Parent object. We will use the arrayParents at the setAdapter
        arrayParents.add(parent2);
        //   }

        //sets the adapter that provides data to the list.
        mExpandableList.setAdapter(new ListItemParentActivity.MyCustomAdapter(FreeTimeActivity.this,arrayParents));

    }

}
