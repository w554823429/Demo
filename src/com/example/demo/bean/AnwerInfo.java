package com.example.demo.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangyipeng on 16/6/30.
 */
public class AnwerInfo extends BaseInfo{


    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * answer : 2
         * explain : 鈥滆繚鍙嶉亾璺氦閫氬畨鍏ㄦ硶鈥濓紝杩濆弽娉曞緥娉曡鍗充负杩濇硶琛屼负銆傚畼鏂瑰凡鏃犺繚绔�/杩濊鐨勮娉曘��
         * optiona : 杩濈珷琛屼负
         * optionb : 杩濇硶琛屼负
         * optionc : 杩囧け琛屼负
         * optiond : 杩濊琛屼负
         * optione : null
         * optionf : null
         * optiong : null
         * optionh : null
         * question : 椹鹃┒鏈哄姩杞﹀湪閬撹矾涓婅繚鍙嶉亾璺氦閫氬畨鍏ㄦ硶鐨勮涓猴紝灞炰簬浠�涔堣涓猴紵
         * questionid : 1
         * mediatype : 0
         * mediacontent :
         * optiontype : 1
         */

        private List<SubDataBean> data;

        public List<SubDataBean> getData() {
            return data;
        }

        public void setData(List<SubDataBean> data) {
            this.data = data;
        }

        public static class SubDataBean implements Serializable{
            private String answer;
            private String explain;
            private String optiona;
            private String optionb;
            private String optionc;
            private String optiond;
            private Object optione;
            private Object optionf;
            private Object optiong;
            private Object optionh;
            private String question;
            private String questionid;
            private String mediatype;
            private String mediacontent;
            private String optiontype;
            
            private String videoPath;
            
            public String getVideoPath() {
                return videoPath;
            }

            public void setVideoPath(String videoPath) {
                this.videoPath = videoPath;
            }

            public String getAnswer() {
                return answer;
            }

            public void setAnswer(String answer) {
                this.answer = answer;
            }

            public String getExplain() {
                return explain;
            }

            public void setExplain(String explain) {
                this.explain = explain;
            }

            public String getOptiona() {
                return optiona;
            }

            public void setOptiona(String optiona) {
                this.optiona = optiona;
            }

            public String getOptionb() {
                return optionb;
            }

            public void setOptionb(String optionb) {
                this.optionb = optionb;
            }

            public String getOptionc() {
                return optionc;
            }

            public void setOptionc(String optionc) {
                this.optionc = optionc;
            }

            public String getOptiond() {
                return optiond;
            }

            public void setOptiond(String optiond) {
                this.optiond = optiond;
            }

            public Object getOptione() {
                return optione;
            }

            public void setOptione(Object optione) {
                this.optione = optione;
            }

            public Object getOptionf() {
                return optionf;
            }

            public void setOptionf(Object optionf) {
                this.optionf = optionf;
            }

            public Object getOptiong() {
                return optiong;
            }

            public void setOptiong(Object optiong) {
                this.optiong = optiong;
            }

            public Object getOptionh() {
                return optionh;
            }

            public void setOptionh(Object optionh) {
                this.optionh = optionh;
            }

            public String getQuestion() {
                return question;
            }

            public void setQuestion(String question) {
                this.question = question;
            }

            public String getQuestionid() {
                return questionid;
            }

            public void setQuestionid(String questionid) {
                this.questionid = questionid;
            }

            public String getMediatype() {
                return mediatype;
            }

            public void setMediatype(String mediatype) {
                this.mediatype = mediatype;
            }

            public String getMediacontent() {
                return mediacontent;
            }

            public void setMediacontent(String mediacontent) {
                this.mediacontent = mediacontent;
            }

            public String getOptiontype() {
                return optiontype;
            }

            public void setOptiontype(String optiontype) {
                this.optiontype = optiontype;
            }
        }
    }
}
