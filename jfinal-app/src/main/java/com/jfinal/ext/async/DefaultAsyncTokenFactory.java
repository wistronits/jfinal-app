package com.jfinal.ext.async;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.List;

@SuppressWarnings("all")
public class DefaultAsyncTokenFactory implements AsyncTokenFactory {

    private String tokenGroup = AsyncToken.DEFAULT_TOKEN_GROUP;
    private String tokenName;
    private List<IResponder> responders = Lists.newArrayList();
    private UncaughtExceptionHandler uncaughtExceptionHandler;

    public String getTokenGroup() {
        return tokenGroup;
    }

    public void setTokenGroup(String tokenGroup) {
        this.tokenGroup = tokenGroup;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public List<IResponder> getResponders() {
        return responders;
    }

    public void setResponders(List<IResponder> responders) {
        Preconditions.checkNotNull(responders, "responders must be not null");
        this.responders = responders;
    }

    public void addResponder(IResponder r) {
        this.responders.add(r);
    }

    public UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return uncaughtExceptionHandler;
    }

    public void setUncaughtExceptionHandler(UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    @SuppressWarnings("unchecked")
    public AsyncToken newToken() {
        AsyncToken t = new AsyncToken();

        t.setTokenGroup(tokenGroup);
        t.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        t.setTokenName(tokenName);

        for (IResponder r : responders) {
            t.addResponder(r);
        }

        return t;
    }

}
