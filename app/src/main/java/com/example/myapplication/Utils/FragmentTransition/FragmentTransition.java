package com.example.myapplication.Utils.FragmentTransition;

import android.app.Activity;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.annotation.AnimRes;
import androidx.annotation.IdRes;

import com.example.myapplication.Activities.MainActivity.MainActivity;
import com.example.myapplication.R;

public class FragmentTransition {

    public static void StartActivity(Activity baseActivity, Class targetActivityClass){
        Intent intent = new Intent(baseActivity.getBaseContext(), targetActivityClass);
        baseActivity.startActivity(intent);
    }

    public static FragmentTransaction Transition(FragmentManager fragmentManager, Fragment fragment, @AnimRes int enter, @AnimRes int exit, @IdRes int containerViewId, String tag){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(enter, exit, enter, exit);
        transaction.addToBackStack(null);
        transaction.add(containerViewId, fragment, tag).commit();

        return transaction;
    }

    public static FragmentTransaction TransitionActivityResult(FragmentManager fragmentManager, Fragment fragment, Fragment targetFragment, @AnimRes int enter, @AnimRes int exit, @IdRes int containerViewId, int requestCode, String tag){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(enter, exit, enter, exit);
        fragment.setTargetFragment(targetFragment, requestCode);
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.add(containerViewId, fragment, tag).commit();

        return transaction;
    }

    public static void OpenFragment(FragmentManager fragmentManager, Fragment fragment, @IdRes int containerViewId, String tag){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(containerViewId, fragment,"");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static FragmentTransaction CloseTransition(FragmentManager fragmentManager, Fragment fragment, @AnimRes int enter, @AnimRes int exit, @IdRes int containerViewId){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(enter, exit, enter, exit)
               // .replace(containerViewId, new Fragment())
                .remove(fragment)
                .commit();

        return transaction;
    }
}
