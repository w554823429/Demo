package com.example.demo.fragment;


import com.example.demo.R;
import com.example.demo.bean.AnwerInfo;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReadFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private AnwerInfo.DataBean.SubDataBean  subDataBean;
    private View view;


    public ReadFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment ReadFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReadFragment newInstance(AnwerInfo.DataBean.SubDataBean subDataBean) {
        ReadFragment fragment = new ReadFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, subDataBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            subDataBean = (AnwerInfo.DataBean.SubDataBean) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_read, container, false);

        initView();
        // Inflate the layout for this fragment
        return view;
    }

    private void initView() {
        TextView tv_question = (TextView) view.findViewById(R.id.tv_question);

        tv_question.setText(subDataBean.getQuestionid()+". "+subDataBean.getQuestion()
                +"\n\nA."+subDataBean.getOptiona()
                +"\nB."+subDataBean.getOptionb()
                +"\nC."+subDataBean.getOptionc()
                +"\nD."+subDataBean.getOptiond()
                +"\n\n\n绛旀瑙ｆ瀽锛�"+subDataBean.getExplain()
        );
        if(!TextUtils.isEmpty(subDataBean.getVideoPath())){
        	VideoView vv_video=(VideoView) view.findViewById(R.id.vv_video);
            // 播放在线视频
               Uri mVideoUri = Uri.parse(subDataBean.getVideoPath());
               vv_video.setVideoPath(mVideoUri.toString());

               vv_video.start();
               vv_video.requestFocus();
        }

    }

}
