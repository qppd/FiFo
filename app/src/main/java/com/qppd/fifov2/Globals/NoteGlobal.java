package com.qppd.fifov2.Globals;

import com.qppd.fifov2.Classes.Note;

public class NoteGlobal {

    private static Note note;
    private static boolean isView;

    public static Note getNote() {
        return note;
    }

    public static void setNote(Note note) {
        NoteGlobal.note = note;
    }

    public static boolean IsView() {
        return isView;
    }

    public static void setIsView(boolean isView) {
        NoteGlobal.isView = isView;
    }
}
