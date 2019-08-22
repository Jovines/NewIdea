package com.tree.newidea.bean;

import java.util.ArrayList;
import java.util.List;

public class NotepadBean {

    private List<DatesBean> dates = new ArrayList();

    public List<DatesBean> getDates() {
        return dates;
    }

    public void setDates(List<DatesBean> dates) {
        this.dates = dates;
    }

    public static class DatesBean {
        /**
         * date : 1999-06-13
         * texts : [{"title":"lalala ","text":"heiheiheih","isMardown":false}]
         */


        public DatesBean(String date) {
            this.date = date;
        }

        private String date;
        private List<TextsBean> texts = new ArrayList();

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<TextsBean> getTexts() {
            return texts;
        }

        public void setTexts(List<TextsBean> texts) {
            this.texts = texts;
        }

        public static class TextsBean {
            /**
             * title : lalala
             * text : heiheiheih
             * isMardown : false
             */

            private String title;
            private String text;
            private String date;
            private boolean isMardown = false;

            public String getTitle() {
                return title;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public boolean isIsMardown() {
                return isMardown;
            }

            public void setIsMardown(boolean isMardown) {
                this.isMardown = isMardown;
            }
        }
    }
}
