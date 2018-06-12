package com.victory.qingteng.qingtenggaoxiao.model.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryName;

public interface GetApi {

    @GET("query")
    Observable<String> getArticle(@Query("article") String id);

    @GET("query")
    Observable<String> getArticleList(@QueryName String queryName);

    @GET("query")
    Observable<String> getExamQqgp(@Query("exam_qqgp") String id);

    @GET("query")
    Observable<String> getSchoolQqgp(@Query("school_qqgp") String id);

    @GET("query")
    Observable<String> getVersion(@QueryName String name);

    @GET("query")
    Observable<String> getWikiContent(@Query("exam_wiki_content") String id);

    @GET("query")
    Observable<String> getWikiList(@Query("exam_wiki_list") String id);
}
