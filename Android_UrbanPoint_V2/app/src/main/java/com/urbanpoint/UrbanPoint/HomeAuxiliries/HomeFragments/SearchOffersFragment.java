package com.urbanpoint.UrbanPoint.HomeAuxiliries.HomeFragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.urbanpoint.UrbanPoint.CommonFragments.OfferDetailFragment;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.DModelHomeGrdVw;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.HomeFragments.WebServices.SearchOffers_Webhit_Get_getOffers;
import com.urbanpoint.UrbanPoint.HomeAuxiliries.SearchOffersAdapter;
import com.urbanpoint.UrbanPoint.R;
import com.urbanpoint.UrbanPoint.Utils.AppConfig;
import com.urbanpoint.UrbanPoint.Utils.AppConstt;
import com.urbanpoint.UrbanPoint.Utils.CustomAlert;
import com.urbanpoint.UrbanPoint.Utils.INavBarUpdateUpdateListener;
import com.urbanpoint.UrbanPoint.Utils.IWebCallbacks;
import com.urbanpoint.UrbanPoint.Utils.ProgressDilogue;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchOffersFragment extends Fragment implements View.OnClickListener {

    private ListView lsvNewOffers;
    private List<DModelHomeGrdVw> lstNewOffers;
    private SearchOffersAdapter searchOffersAdapter;
    private TextView txvNotFound;
    private String strSearchKey;
    private EditText edtSearch;
    private Button btnSearch;
    private LinearLayout llClose;
    private View lsvFooterView;
    private int page;
    private boolean shouldGetMoreOffers, isAlreadyfetchingOffers;
    CustomAlert customAlert;
    ProgressDilogue progressDilogue;

    INavBarUpdateUpdateListener iNavBarUpdateUpdateListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_offers, null);
        initialize();
        bindViews(view);

        try {
            iNavBarUpdateUpdateListener = (INavBarUpdateUpdateListener) getActivity();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        iNavBarUpdateUpdateListener.setNavBarTitle(getActivity().getResources().getString(R.string.frg_search_offers));
        iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
        iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
        iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);

        onCreateFunctions();
        return view;
    }


    private void initialize() {
        page = 1;
        strSearchKey = "";
        shouldGetMoreOffers = true;
        isAlreadyfetchingOffers = false;
        customAlert = new CustomAlert();
        progressDilogue = new ProgressDilogue();
        lstNewOffers = new ArrayList<>();
    }

    private void bindViews(View frg) {
        txvNotFound = frg.findViewById(R.id.frg_search_offers_txv_nt_found);
        lsvNewOffers = frg.findViewById(R.id.frg_search_offers_lst_view);
        edtSearch = frg.findViewById(R.id.frg_search_edt_search);
        llClose = frg.findViewById(R.id.frg_search_ll_close);
        btnSearch = frg.findViewById(R.id.frg_search_btn_search);
        btnSearch.setOnClickListener(this);
        llClose.setOnClickListener(this);
        lsvFooterView = ((LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.lsv_footer, null, false);

    }

    private void onCreateFunctions() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    llClose.setVisibility(View.VISIBLE);
                } else {
                    llClose.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    requestNewOffers(page, strSearchKey, true);
                    AppConfig.getInstance().closeKeyboard(getActivity());
                    return true;
                }
                return false;

            }
        });

        lsvNewOffers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int offerId = Integer.parseInt(lstNewOffers.get(position).getStrProductId());
                Bundle bundle = new Bundle();
                bundle.putInt(AppConstt.BundleStrings.offerId, offerId);
                bundle.putString(AppConstt.BundleStrings.offerName, lstNewOffers.get(position).getStrOfferName());
                navToOfferDetailFragment(bundle);
            }
        });

        lsvNewOffers.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (shouldGetMoreOffers && !isAlreadyfetchingOffers) {
                    if ((lsvNewOffers.getLastVisiblePosition() == (searchOffersAdapter.getCount() - 1))) {
                        page++;
                        lsvNewOffers.addFooterView(lsvFooterView);
                        searchOffersAdapter.notifyDataSetChanged();
                        lsvNewOffers.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
                        requestNewOffers(page, strSearchKey, false);
                    }
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frg_search_btn_search:
                page = 1;
                strSearchKey = edtSearch.getText().toString();
                shouldGetMoreOffers = true;
                requestNewOffers(page, strSearchKey, true);
                break;

            case R.id.frg_search_ll_close:
                edtSearch.setText("");
                break;
        }
    }

    private void navToOfferDetailFragment(Bundle _bundle) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment frg = new OfferDetailFragment();
        frg.setArguments(_bundle);
        ft.add(R.id.content_frame, frg, AppConstt.FRGTAG.OfferDetailFragment);
        ft.addToBackStack(AppConstt.FRGTAG.OfferDetailFragment);
        ft.hide(this);
        ft.commit();

    }

    private void requestNewOffers(int _page, String _searchKey, boolean _shouldClearLst) {
        if (_shouldClearLst) {
            progressDilogue.startiOSLoader(getActivity(), R.drawable.image_for_rotation, getString(R.string.please_wait), false);
            lstNewOffers.clear();
            searchOffersAdapter = null;
        }
        isAlreadyfetchingOffers = true;
        SearchOffers_Webhit_Get_getOffers searchOffers_webhit_get_getOffers = new SearchOffers_Webhit_Get_getOffers();
        searchOffers_webhit_get_getOffers.requestSearchOffers(getContext(), new IWebCallbacks() {
            @Override
            public void onWebResult(boolean isSuccess, String strMsg) {
                progressDilogue.stopiOSLoader();
                isAlreadyfetchingOffers = false;
                if (isSuccess) {
                    updateList();
                } else if (strMsg.equalsIgnoreCase(AppConstt.ServerStatus.DATABASE_NOT_FOUND + "")) {
                    shouldGetMoreOffers = false;
                    if (lstNewOffers.size() == 0) {
                        lsvNewOffers.setVisibility(View.GONE);
                        txvNotFound.setVisibility(View.VISIBLE);
                    }
                } else {
                    lsvNewOffers.setVisibility(View.GONE);
                    txvNotFound.setVisibility(View.VISIBLE);
                    customAlert.showCustomAlertDialog(getActivity(), null, strMsg, null, null, false, null);
                }
            }

            @Override
            public void onWebException(Exception ex) {
                progressDilogue.stopiOSLoader();
                isAlreadyfetchingOffers = false;
                lsvNewOffers.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.VISIBLE);
                customAlert.showCustomAlertDialog(getActivity(), null, ex.getMessage(), null, null, false, null);

            }

            @Override
            public void onWebLogout() {
                progressDilogue.stopiOSLoader();
                lsvNewOffers.setVisibility(View.GONE);
                txvNotFound.setVisibility(View.VISIBLE);
                isAlreadyfetchingOffers = false;
                customAlert.showCustomAlertDialog(getActivity(), null, getString(R.string.logout_message), null, null, false, null);
                iNavBarUpdateUpdateListener.navToLogin();
            }
        }, _page, _searchKey);
    }

    private void updateList() {
        if (SearchOffers_Webhit_Get_getOffers.responseObject != null &&
                SearchOffers_Webhit_Get_getOffers.responseObject.getData() != null &&
                SearchOffers_Webhit_Get_getOffers.responseObject.getData().size() > 0) {

            if (SearchOffers_Webhit_Get_getOffers.responseObject.getData().size() < 20) {
                shouldGetMoreOffers = false;
            }

            for (int i = 0; i < SearchOffers_Webhit_Get_getOffers.responseObject.getData().size(); i++) {
                String festival;
                if (SearchOffers_Webhit_Get_getOffers.responseObject.getData().get(i).getSpecialType() != null) {
                    festival = SearchOffers_Webhit_Get_getOffers.responseObject.getData().get(i).getSpecialType();
                } else {
                    festival = "";
                }
                String strImageUrl = "";
                if (SearchOffers_Webhit_Get_getOffers.responseObject.getData().get(i).getImage() != null) {
                    strImageUrl = SearchOffers_Webhit_Get_getOffers.responseObject.getData().get(i).getImage();
                }
                String strName = SearchOffers_Webhit_Get_getOffers.responseObject.getData().get(i).getName();
                String strAddress= SearchOffers_Webhit_Get_getOffers.responseObject.getData().get(i).getAddress();

                lstNewOffers.add(new DModelHomeGrdVw(
                        strImageUrl,
                        SearchOffers_Webhit_Get_getOffers.responseObject.getData().get(i).getTitle(),
                        SearchOffers_Webhit_Get_getOffers.responseObject.getData().get(i).getId(),
                        strName,
                        strAddress,
                        SearchOffers_Webhit_Get_getOffers.responseObject.getData().get(i).getCategory_logo(),
                        festival
                ));
            }

            lsvNewOffers.setVisibility(View.VISIBLE);
            txvNotFound.setVisibility(View.GONE);
            lsvNewOffers.setTranscriptMode(ListView.TRANSCRIPT_MODE_DISABLED);

            if (searchOffersAdapter != null) {
                lsvNewOffers.removeFooterView(lsvFooterView);
                searchOffersAdapter.notifyDataSetChanged();
            } else {
                searchOffersAdapter = new SearchOffersAdapter(getContext(), lstNewOffers, AppConfig.getInstance().mUser.isSubscribed());
                lsvNewOffers.setAdapter(searchOffersAdapter);
            }
        } else {
            lsvNewOffers.setVisibility(View.GONE);
            txvNotFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHiddenChanged(boolean isHidden) {
        super.onHiddenChanged(isHidden);
        if (!isHidden) {
            iNavBarUpdateUpdateListener.setNavBarTitle(getResources().getString(R.string.frg_search_offers));
            iNavBarUpdateUpdateListener.setBackBtnVisibility(View.VISIBLE);
            iNavBarUpdateUpdateListener.setCancelBtnVisibility(View.GONE);
            iNavBarUpdateUpdateListener.setToolBarbackgroudVisibility(View.VISIBLE);
        }
    }
}
