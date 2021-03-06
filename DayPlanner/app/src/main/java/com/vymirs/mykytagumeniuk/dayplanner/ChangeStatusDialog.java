package com.vymirs.mykytagumeniuk.dayplanner;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;


/**
 * Created by Nikita on 11/29/2016.
 */

public class ChangeStatusDialog extends DialogFragment {
    int id;
    int chosenStatus = -1;
    ArrayList<Task> tasksList;
    static ChangeStatusDialog newInstance(int id, ArrayList<Task> tasksList) {
        ChangeStatusDialog ChangeStatusDialog = new ChangeStatusDialog();
        Bundle args = new Bundle();
        args.putInt("id", id);
        args.putParcelableArrayList("tasksList",tasksList);
        ChangeStatusDialog.setArguments(args);
        return ChangeStatusDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        tasksList = getArguments().getParcelableArrayList("tasksList");
        id = getArguments().getInt("id");
        switch (tasksList.get(id).getStatus()) {
            case COMPLETED:
                chosenStatus = 0;
                break;
            case IN_PROGRESS:
                chosenStatus = 1;
                break;
            case UNCOMPLETED:
                chosenStatus = 2;
                break;
        }
        CharSequence[] statuses = {getString(R.string.completed), getString(R.string.in_progress),getString(R.string.uncompleted) };
        MaterialDialog changeStatusDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.choose_status)
                .positiveText("Ok")
                .items(statuses)
                .itemsCallbackSingleChoice(chosenStatus, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                tasksList.get(id).setStatus(Task.Status.COMPLETED);
                                MainActivityListView.tasksListAdapter.notifyDataSetChanged();
                                MainActivityListView.dbQueryManager.insertTasksToDB();
                                MainActivityListView.dbQueryManager.readTasksFromDB();
                                break;
                            case 1:
                                tasksList.get(id).setStatus(Task.Status.IN_PROGRESS);
                                MainActivityListView.tasksListAdapter.notifyDataSetChanged();
                                MainActivityListView.dbQueryManager.insertTasksToDB();
                                MainActivityListView.dbQueryManager.readTasksFromDB();
                                break;
                            case 2:
                                tasksList.get(id).setStatus(Task.Status.UNCOMPLETED);
                                MainActivityListView.tasksListAdapter.notifyDataSetChanged();
                                MainActivityListView.dbQueryManager.insertTasksToDB();
                                MainActivityListView.dbQueryManager.readTasksFromDB();
                                break;
                        }
                        return true;
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                       MainActivityListView.dbQueryManager.insertTasksToDB();
                       MainActivityListView.dbQueryManager.readTasksFromDB();
                    }
                })
                .show();
        return changeStatusDialog;
    }

    public static void showChangeStatusDialog(Activity activity, final int id,ArrayList<Task> tasksList) {
        DialogFragment changeStatusDialogFragment = ChangeStatusDialog.newInstance(id,tasksList);
        changeStatusDialogFragment.show(activity.getFragmentManager(), "ChangeStatusDialogFragment");
    }

}
