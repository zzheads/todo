package com.zzheads.exc;//

// course-reviews
// com.zzheads.courses.exc created by zzheads on 15.08.2016.
//
public class DaoException extends Exception {

    private final Exception originalException;

    public DaoException(Exception originalException, String msg) {
        super(msg);
        this.originalException = originalException;
    }
}
