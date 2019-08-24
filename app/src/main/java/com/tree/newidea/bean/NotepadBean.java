package com.tree.newidea.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NotepadBean implements Serializable {

    private List<DatesBean> dates = new ArrayList();

    public List<DatesBean> getDates() {
        return dates;
    }

    public void setDates(List<DatesBean> dates) {
        this.dates = dates;
    }

    public static class DatesBean implements Serializable{
        /**
         * date : 1999-06-13
         * texts : [{"title":"lalala ","text":"heiheiheih","isMarkdown":false}]
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

        public static class TextsBean implements Serializable{
            /**
             * title : lalala
             * text : heiheiheih
             * isMarkdown : false
             * isUndo : false
             * date :djkdjkj
             */

            private String title;
            private String text;
            private String date;
            private boolean isUndo = false;
            private boolean isMarkdown = false;


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
                return isMarkdown;
            }

            public void setIsMardown(boolean isMardown) {
                this.isMarkdown = isMardown;
            }

            public Boolean getIsUndo() {
                return isUndo;
            }

            public void setUndo(boolean undo) {
                isUndo = undo;
            }


        }
    }
}
