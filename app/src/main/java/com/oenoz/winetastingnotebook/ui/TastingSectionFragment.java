package com.oenoz.winetastingnotebook.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oenoz.winetastingnotebook.R;

public class TastingSectionFragment extends Fragment {
    private static final String ARG_SECTION_NAME = "sectionName";
    private static final String ARG_TASTING_SECTION_URI = "tastingSectionUri";

    private String mSectionName;
    private Uri mTastingSectionUri;

    private TastingAttributePageAdapter mAttributesPagerAdapter;
    private ViewPager mViewPager;


    private OnFragmentInteractionListener mListener;

    public TastingSectionFragment() {
        // Required empty public constructor
    }

    public static TastingSectionFragment newInstance(String sectionName, Uri tastingSectionUri) {
        TastingSectionFragment fragment = new TastingSectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_NAME, sectionName);
        args.putString(ARG_TASTING_SECTION_URI, tastingSectionUri.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSectionName = getArguments().getString(ARG_SECTION_NAME);
            mTastingSectionUri = Uri.parse(getArguments().getString(ARG_TASTING_SECTION_URI));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasting_section, container, false);

        mAttributesPagerAdapter = new TastingAttributePageAdapter(getContext(), getChildFragmentManager(), mTastingSectionUri);
        mViewPager = (ViewPager)view.findViewById(R.id.tastingAttributesViewPager);
        mViewPager.setAdapter(mAttributesPagerAdapter);
        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tastingAttributesTabLayout);
        tabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
