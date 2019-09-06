package com.sogard.data.network


/**
 * Base constant values usable in the network layer.
 */
object AppNetworkConfiguration {

    //TODO: These could be defined in build/flavor config.
    /**
     * BASE URL used for all AUTHENTICATED REQUESTS.
     */
    const val BASE_URL: String = "https://oauth.reddit.com"

    /**
     * BASE URL used for AUTHENTICATION PURPOSES.
     */
    const val BASE_URL_PUBLIC = "https://www.reddit.com"

    /**
     *  Application Only OAuth grant type
     */
    const val GRANT_TYPE_DEFAULT = "https://oauth.reddit.com/grants/installed_client"

    /**
     * Basic Auth app token required to authenticate with Reddit API.
     */
    //TODO: This token should be encrypted and saved in a local storage. It can be easily hacked with reverse
    // engineering techniques. For the scope of this app (at least at this point), security is not central.
    const val BASIC_AUTH_APP_TOKEN = "Basic YjVDYTNRVDRfWHh0ZlE6"
}
