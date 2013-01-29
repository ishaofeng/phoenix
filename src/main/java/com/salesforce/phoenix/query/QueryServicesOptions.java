/*******************************************************************************
 * Copyright (c) 2013, Salesforce.com, Inc.
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *     Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *     Neither the name of Salesforce.com nor the names of its contributors may 
 *     be used to endorse or promote products derived from this software without 
 *     specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package com.salesforce.phoenix.query;

import static com.salesforce.phoenix.query.QueryServices.*;

import org.apache.hadoop.conf.Configuration;

import com.salesforce.phoenix.util.DateUtil;


/**
 * Options for {@link QueryServices}.
 * 
 * @author syyang
 * @since 0.1
 */
public class QueryServicesOptions {
    
    private static final int DEFAULT_KEEP_ALIVE_MS = 1000;
    private static final int DEFAULT_THREAD_POOL_SIZE = 20;
    private static final int DEFAULT_QUEUE_SIZE = 250;
    private static final int DEFAULT_THREAD_TIMEOUT_MS = 60000; // 1min
    private static final int DEFAULT_SPOOL_THRESHOLD_BYTES = 1024 * 1024 * 50; // 50m
    private static final int DEFAULT_MAX_HTABLE_POOL_SIZE = 1000;
    private static final long DEFAULT_MAX_MEMORY_BYTES = 1024 * 1024 * 400; // 400m
    private static final int DEFAULT_MAX_MEMORY_WAIT_MS = 5000;
    private static final int DEFAULT_MAX_ORG_MEMORY_PERC = 30;
    private static final long DEFAULT_MAX_HASH_CACHE_SIZE = 1024*1024*100;  // 100 Mb
    public static final int DEFAULT_TARGET_QUERY_CONCURRENCY = 8;
    public static final int DEFAULT_MAX_QUERY_CONCURRRENCY = 12;
    public static final String DEFAULT_DATE_FORMAT = DateUtil.DEFAULT_DATE_FORMAT;
    public static final int DEFAULT_STATS_UPDATE_FREQ_MS = 15 * 60000; // 15min
    public static final int DEFAULT_MAX_STATS_AGE_MS = 24 * 60 * 60000; // 1 day
    public static final boolean DEFAULT_CALL_QUEUE_ROUND_ROBIN = true; 
    public static final int DEFAULT_MAX_MUTATION_SIZE = 500000;
    /**
     * Default value used for the batch size of an UPSERT/SELECT command.
     */
    public final static int DEFAULT_UPSERT_BATCH_SIZE = 10000;
    
    private final Configuration config;
    
    private QueryServicesOptions(Configuration config) {
        this.config = config;
    }

    public static QueryServicesOptions withDefaults(Configuration config) {
        return new QueryServicesOptions(config)
            .setIfUnset(KEEP_ALIVE_MS_ATTRIB, DEFAULT_KEEP_ALIVE_MS)
            .setIfUnset(THREAD_POOL_SIZE_ATTRIB, DEFAULT_THREAD_POOL_SIZE)
            .setIfUnset(QUEUE_SIZE_ATTRIB, DEFAULT_QUEUE_SIZE)
            .setIfUnset(THREAD_TIMEOUT_MS_ATTRIB, DEFAULT_THREAD_TIMEOUT_MS)
            .setIfUnset(SPOOL_THRESHOLD_BYTES_ATTRIB, DEFAULT_SPOOL_THRESHOLD_BYTES)
            .setIfUnset(MAX_HTABLE_POOL_SIZE_ATTRIB, DEFAULT_MAX_HTABLE_POOL_SIZE)
            .setIfUnset(MAX_MEMORY_BYTES_ATTRIB, DEFAULT_MAX_MEMORY_BYTES)
            .setIfUnset(MAX_MEMORY_WAIT_MS_ATTRIB, DEFAULT_MAX_MEMORY_WAIT_MS)
            .setIfUnset(MAX_ORG_MEMORY_PERC_ATTRIB, DEFAULT_MAX_ORG_MEMORY_PERC)
            .setIfUnset(MAX_HASH_CACHE_SIZE_ATTRIB, DEFAULT_MAX_HASH_CACHE_SIZE)
            .setIfUnset(SCAN_CACHE_SIZE_ATTRIB, DEFAULT_SCAN_CACHE_SIZE)
            .setIfUnset(TARGET_QUERY_CONCURRENCY_ATTRIB, DEFAULT_TARGET_QUERY_CONCURRENCY)
            .setIfUnset(MAX_QUERY_CONCURRENCY_ATTRIB, DEFAULT_MAX_QUERY_CONCURRRENCY)
            .setIfUnset(DATE_FORMAT_ATTRIB, DEFAULT_DATE_FORMAT)
            .setIfUnset(STATS_UPDATE_FREQ_MS_ATTRIB, DEFAULT_STATS_UPDATE_FREQ_MS)
            .setIfUnset(CALL_QUEUE_ROUND_ROBIN_ATTRIB, DEFAULT_CALL_QUEUE_ROUND_ROBIN)
            .setIfUnset(MAX_MUTATION_SIZE_ATTRIB, DEFAULT_MAX_MUTATION_SIZE)
            .setIfUnset(UPSERT_BATCH_SIZE_ATTRIB, DEFAULT_UPSERT_BATCH_SIZE)
            ;
    }
    
    public Configuration getConfiguration() {
        return config;
    }

    private QueryServicesOptions setIfUnset(String name, int value) {
        config.setIfUnset(name, Integer.toString(value));
        return this;
    }
    
    private QueryServicesOptions setIfUnset(String name, boolean value) {
        config.setIfUnset(name, Boolean.toString(value));
        return this;
    }
    
    private QueryServicesOptions setIfUnset(String name, long value) {
        config.setIfUnset(name, Long.toString(value));
        return this;
    }
    
    private QueryServicesOptions setIfUnset(String name, String value) {
        config.setIfUnset(name, value);
        return this;
    }
    
    public QueryServicesOptions setKeepAliveMs(int keepAliveMs) {
        return set(KEEP_ALIVE_MS_ATTRIB, keepAliveMs);
    }
    
    public QueryServicesOptions setThreadPoolSize(int threadPoolSize) {
        return set(THREAD_POOL_SIZE_ATTRIB, threadPoolSize);
    }
    
    public QueryServicesOptions setQueueSize(int queueSize) {
        config.setInt(QUEUE_SIZE_ATTRIB, queueSize);
        return this;
    }
    
    public QueryServicesOptions setThreadTimeoutMs(int threadTimeoutMs) {
        return set(THREAD_TIMEOUT_MS_ATTRIB, threadTimeoutMs);
    }
    
    public QueryServicesOptions setSpoolThresholdBytes(int spoolThresholdBytes) {
        return set(SPOOL_THRESHOLD_BYTES_ATTRIB, spoolThresholdBytes);
    }
    
    public QueryServicesOptions setMaxHTablePoolSize(int maxHTablePoolSize) {
        return set(MAX_HTABLE_POOL_SIZE_ATTRIB, maxHTablePoolSize);
    }
    
    public QueryServicesOptions setMaxMemoryBytes(long maxMemoryBytes) {
        return set(MAX_MEMORY_BYTES_ATTRIB, maxMemoryBytes);
    }
    
    public QueryServicesOptions setMaxMemoryWaitMs(int maxMemoryWaitMs) {
        return set(MAX_MEMORY_WAIT_MS_ATTRIB, maxMemoryWaitMs);
    }
    
    public QueryServicesOptions setMaxOrgMemoryPerc(int maxOrgMemoryPerc) {
        return set(MAX_ORG_MEMORY_PERC_ATTRIB, maxOrgMemoryPerc);
    }
    
    public QueryServicesOptions setMaxHashCacheSize(long maxHashCacheSize) {
        return set(MAX_HASH_CACHE_SIZE_ATTRIB, maxHashCacheSize);
    }

    public QueryServicesOptions setScanFetchSize(int scanFetchSize) {
        return set(SCAN_CACHE_SIZE_ATTRIB, scanFetchSize);
    }
    
    public QueryServicesOptions setMaxQueryConcurrency(int maxQueryConcurrency) {
        return set(MAX_QUERY_CONCURRENCY_ATTRIB, maxQueryConcurrency);
    }

    public QueryServicesOptions setTargetQueryConcurrency(int targetQueryConcurrency) {
        return set(TARGET_QUERY_CONCURRENCY_ATTRIB, targetQueryConcurrency);
    }
    
    public QueryServicesOptions setDateFormat(String dateFormat) {
        return set(DATE_FORMAT_ATTRIB, dateFormat);
    }
    
    public QueryServicesOptions setStatsUpdateFrequencyMs(int frequencyMs) {
        return set(STATS_UPDATE_FREQ_MS_ATTRIB, frequencyMs);
    }
    
    public QueryServicesOptions setCallQueueRoundRobin(boolean isRoundRobin) {
        return set(CALL_QUEUE_PRODUCER_ATTRIB_NAME, isRoundRobin);
    }
    
    public QueryServicesOptions setMaxMutateSize(int maxMutateSize) {
        return set(MAX_MUTATION_SIZE_ATTRIB, maxMutateSize);
    }
    
    public QueryServicesOptions setUpsertBatchSize(int upsertBatchSize) {
        return set(UPSERT_BATCH_SIZE_ATTRIB, upsertBatchSize);
    }
    
    private QueryServicesOptions set(String name, boolean value) {
        config.set(name, Boolean.toString(value));
        return this;
    }
    
    private QueryServicesOptions set(String name, int value) {
        config.set(name, Integer.toString(value));
        return this;
    }
    
    private QueryServicesOptions set(String name, String value) {
        config.set(name, value);
        return this;
    }
    
    private QueryServicesOptions set(String name, long value) {
        config.set(name, Long.toString(value));
        return this;
    }

    public int getKeepAliveMs() {
        return config.getInt(KEEP_ALIVE_MS_ATTRIB, DEFAULT_KEEP_ALIVE_MS);
    }
    
    public int getThreadPoolSize() {
        return config.getInt(THREAD_POOL_SIZE_ATTRIB, DEFAULT_THREAD_POOL_SIZE);
    }
    
    public int getQueueSize() {
        return config.getInt(QUEUE_SIZE_ATTRIB, DEFAULT_QUEUE_SIZE);
    }
    
    public long getMaxMemoryBytesMs() {
        return config.getLong(MAX_MEMORY_BYTES_ATTRIB, DEFAULT_MAX_MEMORY_BYTES);
    }
    
    public int getMaxMemoryWaitMs() {
        return config.getInt(MAX_MEMORY_BYTES_ATTRIB, DEFAULT_MAX_MEMORY_WAIT_MS);
    }

    public int getMaxMutateSize() {
        return config.getInt(MAX_MUTATION_SIZE_ATTRIB, DEFAULT_MAX_MUTATION_SIZE);
    }

    public int getUpsertBatchSize() {
        return config.getInt(UPSERT_BATCH_SIZE_ATTRIB, DEFAULT_UPSERT_BATCH_SIZE);
    }
}