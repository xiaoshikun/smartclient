package com.aspire;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaos
 * @create 2018-02-03-11:17
 */
@Configuration
public class HttpClient {

    @Value("${http.pool.maxTotal}")
    private Integer maxTotal;

    @Value("${http.pool.defaultMaxPerRoute}")
    private Integer defaultMaxPerRoute;

    @Value("${http.request.connectTimeout}")
    private Integer connectTimeout;

    @Value("${http.request.connectionRequestTimeout}")
    private Integer connectionRequestTimeout;

    @Value("${http.request.socketTimeout}")
    private Integer socketTimeout;

    @Value("${http.request.staleConnectionCheckEnabled}")
    private boolean staleConnectionCheckEnabled;

    //定义httpclient连接池
    @Bean(name = "httpClientConnectionManager")
    public PoolingHttpClientConnectionManager getHttpClientConnectionManager(){
        PoolingHttpClientConnectionManager httpClientConnectionManager = new PoolingHttpClientConnectionManager();
        httpClientConnectionManager.setMaxTotal(maxTotal);
        httpClientConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        return httpClientConnectionManager;
    }

    //定义 HttpClient工厂，这里使用HttpClientBuilder构建
    @Bean(name = "httpClientBuilder")
    public HttpClientBuilder httpClientBuilder(@Qualifier("httpClientConnectionManager") PoolingHttpClientConnectionManager poolingHttpClientConnectionManager){
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
        return httpClientBuilder;
    }

    //得到httpClient的实例
    @Bean
    public CloseableHttpClient getCloseableHttpClient(@Qualifier("httpClientBuilder") HttpClientBuilder httpClientBuilder){
        return httpClientBuilder.build();
    }

    //定义requestConfig的工厂
    @Bean(name="requestConfigBuilder")
    public RequestConfig.Builder requestConfigBuilder(){
        RequestConfig.Builder builder = RequestConfig.custom();
        return builder.setConnectTimeout(connectTimeout)
                                 .setConnectionRequestTimeout(connectionRequestTimeout)
                                 .setSocketTimeout(socketTimeout)
                                 .setStaleConnectionCheckEnabled(staleConnectionCheckEnabled);
    }

    //得到requestConfig实例
    @Bean
    public RequestConfig getRequestConfig(@Qualifier("requestConfigBuilder") RequestConfig.Builder requestConfigBuilder){
            return requestConfigBuilder.build();
    }



}
