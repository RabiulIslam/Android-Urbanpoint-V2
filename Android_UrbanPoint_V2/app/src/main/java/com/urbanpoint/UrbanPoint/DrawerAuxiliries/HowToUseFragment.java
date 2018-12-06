package com.urbanpoint.UrbanPoint.DrawerAuxiliries;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class HowToUseFragment extends Fragment {

    INavBarUpdateUpdateListener iNavBarUpdateUpdateListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_how_to_use, null);
        try {
            iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_how_to_use));
        iNavBarUpdateUpdateListener.setBackBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onHiddenChanged(boolean isHidden) {
        super.onHiddenChanged(isHidden);
        if (!isHidden){
            iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_how_to_use));
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);

        }

    }
}
