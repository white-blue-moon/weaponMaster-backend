package com.example.weaponMaster.modules.slack.constant;

public class SlackApi {

    public static final String OAUTH_URL        = "https://slack.com/api/oauth.v2.access";
    public static final String BOT_INSTALL_URL  = "https://slack.com/oauth/v2/authorize?client_id=%s&scope=%s&redirect_uri=%s&state=%s";
    public static final String DM_OPEN_URL      = "https://slack.com/api/conversations.open";
    public static final String SEND_MESSAGE_URL = "https://slack.com/api/chat.postMessage";
}
