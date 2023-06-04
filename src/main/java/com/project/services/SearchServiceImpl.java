package com.project.services;

import com.project.utils.ResultQuery;
import java.io.IOException;

public interface SearchServiceImpl {
    ResultQuery searchFromQuery(String query) throws IOException;
}