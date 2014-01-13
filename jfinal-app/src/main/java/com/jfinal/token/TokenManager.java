/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
 */

package com.jfinal.token;

import com.jfinal.core.Const;
import com.jfinal.core.Controller;
import com.jfinal.kit.StringKit;

import java.util.*;

/**
 * TokenManager.
 */
public class TokenManager {

    private static ITokenCache tokenCache;
    private static Random random = new Random();

    private TokenManager() {

    }

    public static void init(ITokenCache tokenCache) {
        if (tokenCache == null)
            return;

        TokenManager.tokenCache = tokenCache;

        long halfTimeOut = Const.MIN_SECONDS_OF_TOKEN_TIME_OUT * 1000 / 2;    // Token最小过期时间的一半时间作为任务运行的间隔时间
        new Timer().schedule(new TimerTask() {
            public void run() {
                removeTimeOutToken();
            }
        },
                halfTimeOut,
                halfTimeOut);
    }

    /**
     * Create Token.
     *
     * @param controller       Controller
     * @param tokenName        token name
     * @param secondsOfTimeOut seconds of time out, for ITokenCache only.
     */
    public static void createToken(Controller controller, String tokenName, int secondsOfTimeOut) {
        if (tokenCache == null) {
            String tokenId = String.valueOf(random.nextLong());
            controller.setAttr(tokenName, tokenId);
            controller.setSessionAttr(tokenName, tokenId);
            createTokenHiddenField(controller, tokenName, tokenId);
        } else {
            createTokenUseTokenIdGenerator(controller, tokenName, secondsOfTimeOut);
        }
    }

    /**
     * Use ${token!} in view for generate hidden input field.
     */
    private static void createTokenHiddenField(Controller controller, String tokenName, String tokenId) {
        controller.setAttr("token", "<input type='hidden' name='" + tokenName + "' value='" + tokenId + "' />");
    }

    private static void createTokenUseTokenIdGenerator(Controller controller, String tokenName, int secondsOfTimeOut) {
        if (secondsOfTimeOut < Const.MIN_SECONDS_OF_TOKEN_TIME_OUT)
            secondsOfTimeOut = Const.MIN_SECONDS_OF_TOKEN_TIME_OUT;

        String tokenId;
        Token token;
        int safeCounter = 8;
        do {
            if (safeCounter-- == 0)
                throw new RuntimeException("Can not create tokenId.");
            tokenId = String.valueOf(random.nextLong());
            token = new Token(tokenId, System.currentTimeMillis() + (secondsOfTimeOut * 1000));
        } while (tokenCache.contains(token));

        controller.setAttr(tokenName, tokenId);
        tokenCache.put(token);
        createTokenHiddenField(controller, tokenName, tokenId);
    }

    /**
     * Check token to prevent resubmit.
     *
     * @param tokenName the token name used in view's form
     * @return true if token is correct
     */
    public static synchronized boolean validateToken(Controller controller, String tokenName) {
        String clientTokenId = controller.getPara(tokenName);
        if (tokenCache == null) {
            String serverTokenId = controller.getSessionAttr(tokenName);
            controller.removeSessionAttr(tokenName);        // important!
            return StringKit.notBlank(clientTokenId) && clientTokenId.equals(serverTokenId);
        } else {
            Token token = new Token(clientTokenId);
            boolean result = tokenCache.contains(token);
            tokenCache.remove(token);
            return result;
        }
    }

    private static void removeTimeOutToken() {
        List<Token> tokenInCache = tokenCache.getAll();
        if (tokenInCache == null)
            return;

        List<Token> timeOutTokens = new ArrayList<Token>();
        long currentTime = System.currentTimeMillis();
        // find and save all time out tokens
        for (Token token : tokenInCache)
            if (token.getExpirationTime() <= currentTime)
                timeOutTokens.add(token);

        // remove all time out tokens
        for (Token token : timeOutTokens)
            tokenCache.remove(token);
    }
}





