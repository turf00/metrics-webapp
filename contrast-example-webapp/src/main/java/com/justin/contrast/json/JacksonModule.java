package com.justin.contrast.json;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.justin.contrast.domain.Account;
import com.justin.contrast.domain.Transaction;
import com.justin.contrast.metric.Metric;
import com.justin.contrast.metric.MetricStatLong;
import com.justin.contrast.metric.MetricStats;

public class JacksonModule extends SimpleModule {
    public JacksonModule() {
        super("JacksonModule", new Version(0,0,1,null, "com.justin", "contrast-example-webapp"));
        setMixInAnnotation(Account.class, AccountMixIn.class);
        setMixInAnnotation(Metric.class, MetricMixIn.class);
        setMixInAnnotation(Transaction.class, TransactionMixIn.class);
        setMixInAnnotation(MetricStats.class, MetricStatsMixIn.class);
        setMixInAnnotation(MetricStatLong.class, MetricStatLongMixIn.class);
    }

}
