package com.yelinaung.programmerexcuses;

import com.yelinaung.programmerexcuses.model.Excuse;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Ye Lin Aung on 14/09/14.
 */
public interface ExcuseService {
  @GET("/") void getExcuse(Callback<Excuse> excuseCallback);
}
