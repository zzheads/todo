package com.zzheads.exc;//

// course-reviews
// com.zzheads.courses.exc created by zzheads on 16.08.2016.
//
public class ApiError extends RuntimeException {
    private final int status;

    public ApiError(int status, String msg) {
        super(msg);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
