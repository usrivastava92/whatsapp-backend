export class PROD_URLS {
    static PROFILE_SERVICE = "https://whatsapp-profile-service.herokuapp.com/profile-service";
    static GATEWAY_SERVICE = "wss://whatsapp-gateway-service.herokuapp.com/gateway-service";
    static SESSIONS_SERVICE = "";
    static DISCOVERY_SERVICE = "";
    static LOGIN_URL = `${PROD_URLS.PROFILE_SERVICE}/auth/login`;
    static GET_PROFILE_URL = `${PROD_URLS.PROFILE_SERVICE}/profile/get`;
    static CREATE_TOKEN_URL = `${PROD_URLS.PROFILE_SERVICE}/token/create`;
    static VALIDATE_TOKEN_URL = `${PROD_URLS.PROFILE_SERVICE}/token/validate`;
    static SOCKET_CONNECT_URL = `${PROD_URLS.GATEWAY_SERVICE}/socket`;
}

export class DEV_URLS {
    static PROFILE_SERVICE = "http://localhost:8082/profile-service";
    static GATEWAY_SERVICE = "ws://localhost:8080/gateway-service";
    static SESSIONS_SERVICE = "";
    static DISCOVERY_SERVICE = "";
    static LOGIN_URL = `${DEV_URLS.PROFILE_SERVICE}/auth/login`;
    static GET_PROFILE_URL = `${DEV_URLS.PROFILE_SERVICE}/profile/get`;
    static CREATE_TOKEN_URL = `${DEV_URLS.PROFILE_SERVICE}/token/create`;
    static VALIDATE_TOKEN_URL = `${DEV_URLS.PROFILE_SERVICE}/token/validate`;
    static SOCKET_CONNECT_URL = `${DEV_URLS.GATEWAY_SERVICE}/socket?token=`;
}

