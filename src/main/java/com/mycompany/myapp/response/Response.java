package com.mycompany.myapp.response;

/**
 *
 * @author dlugo
 */
public class Response {

    boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "Response{" + "success=" + success + '}';
    }
}
