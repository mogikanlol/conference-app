package com.nikolaev.submission;


import com.nikolaev.submission.status.dto.SubmissionStatusDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SubmissionRequest implements Serializable {

    private String title;

    private SubmissionStatusDto status;

    public SubmissionRequest() {}


    public SubmissionRequest(String title, SubmissionStatusDto status) {
        this.title = title;
        this.status = status;
    }

    @Override
    public String toString() {
        return "SubmissionRequest{" +
                "title='" + title + '\'' +
                '}';
    }
}
